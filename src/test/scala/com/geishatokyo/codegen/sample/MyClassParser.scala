package com.geishatokyo.codegen.sample

import com.geishatokyo.codegen.dsl.parser.model.ClassDSLParser
import com.geishatokyo.codegen.dsl.parser.{Definition, GenericDSLParser}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */

object MyClassParser extends ClassDSLParser with GenericDSLParser{
  def definitionsDef = {
    classDefinitionDef | enumDefinitionDef

  }
}

