package com.geishatokyo.codegen.dsl.parser.api

import org.specs2.mutable.Specification
import com.geishatokyo.codegen.dsl.parser.GenericDSLParser

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/09/25
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
class APIDSLParserTest extends Specification {

  object DSLParser extends APIDSLParser with GenericDSLParser{
    def definitionsDef = apiDefinition
  }

  "APIDSLParser" should{
    "parse" in{

      val dsl =
        """
          |@get user/(userId : Long)
          |  query : ( start : Int = 2)
          |  body : User
          |  return : User
          |
          |@post user/create
          |  query : ( start : Int)
          |  body : (nickname : String),(password : String = "")
          |  return : Unit
          |
        """.stripMargin

      val parsed = DSLParser.parse(dsl)

      println(parsed)

      ok

    }
  }


}
