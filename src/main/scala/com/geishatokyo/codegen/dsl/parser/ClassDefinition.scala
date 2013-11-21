package com.geishatokyo.codegen.dsl.parser

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/10/04
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */

case class ClassDefinition(
  name : String,options : List[OptionValue],
  body : List[BodyElement]) extends Definition

case class EnumDefinition(
  name : String,options : List[OptionValue],
  body : List[BodyElement]) extends Definition




// ########## options ###############

trait OptionValue{
  def value : String
  def args : List[OptionValue]
}

case class NoArgsOption(value : String) extends OptionValue{
  val args = Nil
}

case class ArgsOption(value : String, args : List[OptionValue]) extends OptionValue

// ########## body ##################

trait BodyElement

trait Marker extends BodyElement{
}
case class SimpleMarker(label : String) extends Marker

case class ComplexMarker(labels : List[String],options : List[OptionValue]) extends Marker

case class Field(name : String, dataType : DataType,options : List[OptionValue]) extends BodyElement

case class Const(name : String,value : String) extends BodyElement

case class Index(name : Option[String],fields : List[String], ordering : Option[String]) extends Marker
case class Unique(name : Option[String],fields : List[String],ordering : Option[String]) extends Marker

case class Scope(names : List[String]) extends Marker

// ########### data type ################

trait DataType

case class SimpleData(name : String) extends DataType

case class ListData(dataType : DataType) extends DataType

case class GenericData(name : String,generics : List[DataType]) extends DataType



