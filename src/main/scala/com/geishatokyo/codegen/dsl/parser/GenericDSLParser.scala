package com.geishatokyo.codegen.dsl.parser

import util.parsing.combinator.RegexParsers
import com.geishatokyo.codegen.util.Logger

/**
 * 
 * User: takeshita
 * DateTime: 13/09/02 11:00
 */
trait GenericDSLParser extends DSLParser with RegexParsers{


  def definitionsDef : Parser[Definition]


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

  def expr = rep(commentLines ~> definitionsDef) <~ commentLines


}
