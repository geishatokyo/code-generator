package com.geishatokyo.codegen.sample.api

import com.geishatokyo.codegen.dsl.parser._
import com.geishatokyo.codegen.dsl.parser.APIDefinition
import com.geishatokyo.codegen.dsl.parser.TypedPathElement
import com.geishatokyo.codegen.dsl.parser.StringPathElement
import com.geishatokyo.codegen.sample.model.ScalaDataType

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/28
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
case class APIWrapper(apiDef : APIDefinition) {

  def group = apiDef.url.head match{
    case StringPathElement(path) => path
  }

  def methodName = {
    apiDef.method.toLowerCase +  apiDef.url.tail.collect({
      case StringPathElement(path) => path
    }).map(_.capitalize).mkString
  }

  def methodParams = {

    val paramList =  apiDef.url.collect({
      case TypedPathElement(name , dt) => {
        name + " : " + dataTypeToScalaClass(dt)
      }
    }) :::
    apiDef.params.find(_.label == "getParam").map(getParam => {
      getParam.params.collect({
        case NamedParamElement(name,dt,dflt) => {
          name + " : " + dataTypeToScalaClass(dt) + dflt.map(v => {
            " = " + v
          }).getOrElse("")
        }
        case AnonymousParamElement(dt) => {
          val scalaClass = dataTypeToScalaClass(dt)
          val paramName = ScalaDataType.dataTypeToParamName(dt)
          paramName + " : " + scalaClass
        }

      })
    }).getOrElse(Nil)


    paramList.mkString(",")
  }

  def dataTypeToScalaClass(dt : DataType) = {
    ScalaDataType.dataTypeToString(dt)

  }



}
