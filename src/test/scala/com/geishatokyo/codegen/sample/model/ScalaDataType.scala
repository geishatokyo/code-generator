package com.geishatokyo.codegen.sample.model

import com.geishatokyo.codegen.dsl.parser.{ListData, SimpleData, DataType}
import com.geishatokyo.codegen.util.StringUtil

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/28
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
object ScalaDataType {

  def dataTypeToString(dt : DataType) : String = dt match{
    case SimpleData("BigString") => "String"
    case SimpleData(name) => name
    case ListData(dataType) => "List[" + dataTypeToString(dataType) + "]"
  }


  def dataTypeToParamName(dt : DataType) : String = dt match{
    case SimpleData(name) => StringUtil.decapitalize(name)
    case ListData(dataType) => {
      "listOf" + dataTypeToParamName(dt)
    }

  }
}
