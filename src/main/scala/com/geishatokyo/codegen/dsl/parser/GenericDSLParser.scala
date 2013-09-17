package com.geishatokyo.codegen.dsl.parser

import util.parsing.combinator.RegexParsers

/**
 * 
 * User: takeshita
 * DateTime: 13/09/02 11:00
 */
trait GenericDSLParser extends RegexParsers{


  def definitionDef = "@@" ~ defType ~ defName ~ opt(":" ~> definitionOptionDefs) ~ lineBreak ~
    definitionBody ^^ {
    case "@@" ~ defType ~ defName ~ options ~ _ ~ body => {
      AnyDefinition(defType,defName,options.getOrElse(Nil),body)
    }
  }

  def defType = string
  def defName = string

  // def options
  def definitionOptionDefs : Parser[List[OptionValue]] =
    ((optionDef ~ definitionOptionDefs) ^^ {case h ~ tails => h :: tails}) |
    (guard(lineBreak) ^^^ { Nil })



  def optionDef : Parser[OptionValue] = argsOption | noArgsOption

  def noArgsOption : Parser[OptionValue] = string <~ opt("(" ~ ")") ^^ {
    case name => NoArgsOption(name)
  }

  def argsOption : Parser[OptionValue] = string ~ "(" ~ rep1(optionDef,"," ~> optionDef) ~ ")" ^^ {
    case name ~ "(" ~ args ~ ")" => ArgsOption(name,args)
  }

  // def body
  def definitionBody = rep( marker | field | const)

  def marker = simpleMarker | complexMarker

  def simpleMarker = "@" ~> string <~ lineBreak ^^ {
    case name => {
      SimpleMarker(name)
    }
  }
  def complexMarker = "@" ~> rep1(string, "," ~> string) ~ markerOptions ^^{
    case labels ~ options => ComplexMarker(labels,options)
  }


  def markerOptions : Parser[List[OptionValue]] = (lineBreak ^^^ {Nil}) |
    (optionDef ~ fieldOptions) ^^ {
      case option ~ left => option :: left
    }



  def field = string ~ ":" ~ dataType ~ fieldOptions ^^{
    case name ~ ":" ~ dataType ~ options => {
      Field(name,dataType,options)
    }
  }

  def fieldOptions : Parser[List[OptionValue]] = (lineBreak ^^^ {Nil}) |
    (optionDef ~ fieldOptions) ^^ {
      case option ~ left => option :: left
    }

  def dataType : Parser[DataType] = listData | genericData | simpleData

  def listData = (chars <~ ("[" ~ "]") ^^ {
    case name => ListData(SimpleData(name))
  }) | ((("List" ~ "[") ~> dataType <~ "]") ^^ {
    case data => ListData(data)
  })

  def genericData = chars ~ "[" ~ rep1(dataType,"," ~> dataType) ~ "]" ^^ {
    case name ~ "[" ~ types ~ "]" => {
      GenericData(name,types)
    }
  }

  def simpleData = chars ^^ {
    case name => SimpleData(name)
  }


  def const = string ~ "=" ~ string ^^ {
    case name ~ "=" ~ value => {
      Const(name,value)
    }
  }

  def string = ("\"" ~> fullCodeParser("\"") <~ "\"") |
    ("{{{" ~> fullCodeParser("}}}") <~ "}}}" ) |
    chars


  def chars : Parser[String] = "[-a-zA-Z_0-9]+".r
  def digits : Parser[String] = "-?[0-9]+".r

  def commentLines = rep("#" ~ fullCodeParser("\n"))

  def lineBreak = Parser[String]( (input : Input) => {
    var ret = input

    while(ret.first != '\n' && whiteSpace.findPrefixOf(ret.first + "").isDefined){
      ret = ret.rest
    }

    if (ret.first == '\n') Success("",ret.rest)
    else Failure("Need line break",input)
  }) ~ commentLines


  def fullCodeParser(stopStr : String) = Parser[String]((input : Input) => {
    val builder = new StringBuilder()
    var currentInput = input
    var returnInput = input
    val offset = stopStr.length
    for (i <- 0 until offset){
      builder.append(currentInput.first)
      currentInput = currentInput.rest
    }
    while( builder.substring(builder.length - offset) != stopStr){
      builder.append(currentInput.first)
      currentInput = currentInput.rest
      returnInput = returnInput.rest
    }
    builder.delete(builder.length - offset,builder.length)
    Success(builder.toString(),returnInput)
  })

  def expr = rep(commentLines ~> definitionDef) <~ commentLines

  def parse(str : String) : List[Definition] = {

    parseAll(expr,str) match{
      case Success(tree,_) => tree
      case e : NoSuccess => {
        println("Fail to parse:" + e)
        Nil
      }
    }

  }


}
