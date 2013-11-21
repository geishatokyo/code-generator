package com.geishatokyo.codegen.dsl.parser

import scala.util.parsing.combinator.RegexParsers

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/25
 * Time: 20:09
 * To change this template use File | Settings | File Templates.
 */
trait DSLParser {

  self : RegexParsers =>

  def expr : Parser[List[Definition]]

  def parse(str : String) : List[Definition] = {

    parseAll(expr,str) match{
      case Success(tree,_) => tree
      case e : NoSuccess => {
        println("Fail to parse:" + e)
        Nil
      }
    }

  }

}
