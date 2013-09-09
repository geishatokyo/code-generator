package com.geishatokyo.codegen.generator

/**
 * 
 * User: takeshita
 * DateTime: 13/09/06 14:56
 */
trait CodeGenerator[T] {


  def generate(models : T)(implicit context : Context) : List[GeneratedCode]


}
