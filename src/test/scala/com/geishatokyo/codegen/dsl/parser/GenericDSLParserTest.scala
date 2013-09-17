package com.geishatokyo.codegen.dsl.parser

import org.specs2.mutable.Specification

/**
 * 
 * User: takeshita
 * DateTime: 13/09/05 11:17
 */
class GenericDSLParserTest extends Specification {

  "GenericDSLParser" should{

    "parse" in{
      val defs = GenericDSLParserImpl.parse(
        """
          |
          |#comment
          |@@class "hoge b" : Resource Bind(hoge) Test()
          |  @aaa
          |  field : Int
          |  field2 : Float op1
          |  bbb = hoge
          |  @code server {{{
          |    hoge
          |    fuga
          |  }}}
          |@@class Fuga
          |  id : Long
          |
          |@@enum bbb : A
          |  test : String
          |  array : List[Hoge]
          |  fuga : Map[String,Int]
          |  @hoge,fuga Option(a) {{{ code heare }}}
          |  array2 : Hoge[]
          |
        """.stripMargin)

      defs.size === 3

      println(defs)

    }
  }

}

object GenericDSLParserImpl extends GenericDSLParser
