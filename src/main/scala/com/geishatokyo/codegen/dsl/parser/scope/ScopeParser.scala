package com.geishatokyo.codegen.dsl.parser.scope

import com.geishatokyo.codegen.dsl.parser.{ScopeDefinition, GenericDSLParser}

/**
 * Created by takeshita on 14/01/12.
 */
trait ScopeParser {
  self : GenericDSLParser =>

  def scopeDefinitionDef = "@scope" ~> rep1(string,opt(",") ~> string) ^^{
    case scopes => ScopeDefinition(scopes)
  }




}
