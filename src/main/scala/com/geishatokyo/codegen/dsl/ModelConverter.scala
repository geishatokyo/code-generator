package com.geishatokyo.codegen.dsl

import parser.Definition

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 17:35
 */
trait ModelConverter[T] {

  def convert(definitions : List[Definition]) : List[T]

}
