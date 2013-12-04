package com.geishatokyo.codegen.sample.model

import com.geishatokyo.codegen.generator.{GeneratedCode, Context, CodeGenerator}
import com.geishatokyo.codegen.dsl.parser._
import com.geishatokyo.codegen.generator.Context
import com.geishatokyo.codegen.generator.GeneratedCode
import com.geishatokyo.codegen.dsl.parser.Scope
import com.geishatokyo.codegen.dsl.parser.ClassDefinition
import com.geishatokyo.codegen.dsl.parser.Field
import com.geishatokyo.codegen.sample.model.ClassDefWrapper

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
class ScalaCaseClassGenerator extends CodeGenerator[ClassDefWrapper] {
  def generate(models: List[ClassDefWrapper])(implicit context: Context): List[GeneratedCode] = {
    models.map( model => {
      GeneratedCode("CaseClass",model.name,toCaseClass(model))
    })
  }


  def dataTypeToString(dt : DataType) : String = dt match{
    case SimpleData("BigString") => "String"
    case SimpleData(name) => name
    case ListData(dataType) => "List[" + dataTypeToString(dataType) + "]"
  }

  def toCaseClass(model : ClassDefWrapper) = {

    val fields = model.getFieldsForScope("scala")

    s"""

// This class is just sample.
case class ${model.name}(
${
  fields.map(f => {
    s"  ${f.name} : ${dataTypeToString(f.dataType)}"
  }).mkString(",\n")
}
  ){
}
"""


  }
}
