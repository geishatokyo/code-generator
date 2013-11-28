package com.geishatokyo.codegen.dsl.parser.api

import com.geishatokyo.codegen.dsl.parser._
import com.geishatokyo.codegen.dsl.parser.APIParam
import com.geishatokyo.codegen.dsl.parser.AnonymousParamElement
import com.geishatokyo.codegen.dsl.parser.NamedParamElement
import com.geishatokyo.codegen.dsl.parser.APIDefinition
import com.geishatokyo.codegen.dsl.parser.StringPathElement

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/25
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
trait APIDSLParser extends DSLParser {
  self : GenericDSLParser =>

  def apiDefinition = "@" ~> string ~ pathDef ~ lineBreak ~ apiBodyDef ^^{
    case method ~ url ~ _ ~ bodies => APIDefinition(method,url,bodies)
  }


  def apiBodyDef = rep(apiParamDef)

  def apiParamDef = string ~ ":" ~ apiBodyParamsDef ^^{
    case label ~ ":" ~ params => APIParam(label,params)
  }

  def apiBodyParamsDef : Parser[List[ParamElement]] = apiBodyParamDef <~ lineBreak ^^ {
    case param => List(param)
  } |
  apiBodyParamDef ~ "," ~ apiBodyParamsDef ^^ {
    case head ~ "," ~ tails => head :: tails
  }
  def apiBodyParamDef = namedParamDef | anonymousParamDef

  def namedParamDef = "(" ~> string ~ ":" ~ dataType ~ opt(defaultValueDef) <~ ")" ^^ {
    case name ~ ":" ~ dataType ~ defaultValue => NamedParamElement(name,dataType,defaultValue)
  }
  def defaultValueDef = "=" ~> string

  def anonymousParamDef = dataType ^^ {
    case dataType => AnonymousParamElement(dataType)
  }




  def urlChars : Parser[String] = "[-_a-zA-Z]+".r

  def pathDef : Parser[List[PathElement]] = opt("/") ~> rep1(pathElementDef <~ "/") ~ pathElementDef ^^ {
    case paths ~ lastPath => paths :+ lastPath
  }

  def pathElementDef : Parser[PathElement] = stringPathElemDef | typedPathElementDef

  def stringPathElemDef = urlChars ^^ {
    case str => StringPathElement(str)
  }

  def typedPathElementDef = "(" ~> chars ~ ":" ~ dataType <~ ")" ^^ {
    case name ~ ":" ~ dataType => TypedPathElement(name,dataType)
  }



}
