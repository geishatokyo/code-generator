package com.geishatokyo.codegen.dsl.parser

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/25
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
case class APIDefinition(
  method : String,url : List[PathElement],
  params : List[APIParam]) extends Definition{


}

case class APIParam(label : String, params : List[ParamElement]) extends BodyElement


trait PathElement
case class StringPathElement(path : String) extends PathElement
case class TypedPathElement(name : String,dataType : DataType) extends PathElement

trait ParamElement
case class NamedParamElement(name : String , dataType : DataType,default : Option[String]) extends ParamElement
case class AnonymousParamElement(dataType : DataType) extends ParamElement


