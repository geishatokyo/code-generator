package com.geishatokyo.codegen.dsl.parser.model

import com.geishatokyo.codegen.dsl.parser._
import com.geishatokyo.codegen.dsl.parser.ArgsOption
import com.geishatokyo.codegen.dsl.parser.NoArgsOption

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
trait ClassDSLParser {
  self : GenericDSLParser =>

  def classDefinitionDef = ("@class" ~> ClassDef.nameDef ~ opt(":" ~> ClassDef.classOptionDefs) <~ lineBreak) ~
    ClassDef.bodyElementDefs ^^{
    case name ~ options ~ body => ClassDefinition(name,options.getOrElse(Nil),body)
  }

  def enumDefinitionDef = ("@enum" ~> ClassDef.nameDef ~ opt(":" ~> ClassDef.classOptionDefs) <~ lineBreak) ~
    ClassDef.bodyElementDefs ^^{
    case name ~ options ~ body => EnumDefinition(name,options.getOrElse(Nil),body)
  }

  def classOption : Parser[OptionValue] = failure("")
  def fieldOption : Parser[OptionValue] = failure("")
  def markerOption : Parser[OptionValue] = failure("")
  def classBodyElement : Parser[BodyElement] =
    ClassDef.scope | ClassDef.index | ClassDef.unique |
    ClassDef.marker | ClassDef.field | ClassDef.const


  object ClassDef{

    def nameDef = string

    def optionDefs( optionParser : Parser[OptionValue]) : Parser[List[OptionValue]] =
      (guard(lineBreak) ^^^ {Nil}) |
        ( _optionDef(optionParser) ~ optionDefs(optionParser)) ^^ {
          case option ~ left => option :: left
        }

    private def _optionDef( optionParser : Parser[OptionValue]) = {
      optionParser  | argsOption(optionParser) | noArgsOption
    }

    // def options
    def classOptionDefs : Parser[List[OptionValue]] = optionDefs(classOption)

    def noArgsOption : Parser[OptionValue] = string <~ opt("(" ~ ")") ^^ {
      case name => NoArgsOption(name)
    }

    def argsOption(optionParser : Parser[OptionValue]) : Parser[OptionValue] =
      string ~ "(" ~ rep1(noArgsOption,"," ~> noArgsOption) ~ ")" ^^ {
      case name ~ "(" ~ args ~ ")" => ArgsOption(name,args)
    }

    // def body
    def bodyElementDefs = rep(classBodyElement)

    def scope = "+" ~ "scope" ~> rep1(string,opt(",") ~> string) <~ lineBreak ^^ {
      case scopes => Scope(scopes)

    }

    def index = "+" ~ "index" ~> opt("(" ~> string <~ ")") ~ rep1(string,"," ~> string) ~ opt("desc" | "asc") ^^ {
      case name ~ fields ~ ordering => Index(name ,fields,ordering)
    }

    def unique = "+" ~ "unique" ~>  opt("(" ~> string <~ ")") ~ rep1(string,"," ~> string) ~ opt("desc" | "asc") ^^ {
      case name ~ fields ~ ordering => Unique(name,fields,ordering)
    }

    def marker = simpleMarker | complexMarker

    def simpleMarker = "+" ~> string <~ lineBreak ^^ {
      case name => {
        SimpleMarker(name)
      }
    }

    def complexMarker = "+" ~> rep1(string, "," ~> string) ~ markerOptions ^^{
      case labels ~ options => ComplexMarker(labels,options)
    }


    def markerOptions : Parser[List[OptionValue]] = optionDefs(markerOption)



    def field = string ~ ":" ~ dataType ~ fieldOptionDefs ^^{
      case name ~ ":" ~ dataType ~ options => {
        Field(name,dataType,options)
      }
    }

    def fieldOptionDefs : Parser[List[OptionValue]] = optionDefs(fieldOption)

    def const = string ~ "=" ~ string ^^ {
      case name ~ "=" ~ value => {
        Const(name,value)
      }
    }

  }

}
