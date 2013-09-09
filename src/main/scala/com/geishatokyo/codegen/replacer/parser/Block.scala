package com.geishatokyo.codegen.replacer.parser

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 12:15
 */
trait Block


case class StringBlock(lines : List[String]) extends Block

case class HoldBlock(name : String, blocks : List[Block]) extends Block
case class ReplaceBlock(name : String,blocks : List[Block]) extends Block

