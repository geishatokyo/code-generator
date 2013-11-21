package com.geishatokyo.codegen.converter

import com.geishatokyo.codegen.dsl.parser.Definition

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 17:35
 */
trait ModelConverter[T] {

  def convert(definitions : List[Definition]) : List[T]

}
