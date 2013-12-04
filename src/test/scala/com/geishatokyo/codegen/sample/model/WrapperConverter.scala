package com.geishatokyo.codegen.sample.model

import com.geishatokyo.codegen.converter.ModelConverter
import com.geishatokyo.codegen.dsl.parser.{ClassDefinition, Definition}
import com.geishatokyo.codegen.sample.model.ClassDefWrapper

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
class WrapperConverter extends ModelConverter[ClassDefWrapper] {
  def convert(definitions: List[Definition]): List[ClassDefWrapper] = {
    definitions collect{
      case c : ClassDefinition => new ClassDefWrapper(c)
    }
  }
}
