package com.geishatokyo.codegen.dsl.parser
/**
 * 
 * User: takeshita
 * DateTime: 13/09/02 11:02
 */
trait Definition {
  def name : String
  def options : List[OptionValue]
}

case class AnyDefinition(typeName : String,
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



// ########### data type ################

trait DataType

case class SimpleData(name : String) extends DataType

case class ListData(dataType : DataType) extends DataType

case class GenericData(name : String,generics : List[DataType]) extends DataType



