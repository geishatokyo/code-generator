package com.geishatokyo.codegen.sample.api

import com.geishatokyo.codegen.converter.ModelConverter
import com.geishatokyo.codegen.dsl.parser.{APIDefinition, Definition}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/28
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
class APIWrapperConverter extends ModelConverter[APIWrapper] {
  def convert(definitions: List[Definition]): List[APIWrapper] = {
    definitions.collect({
      case apiDef : APIDefinition => {
        APIWrapper(apiDef)
      }
    })
  }
}
