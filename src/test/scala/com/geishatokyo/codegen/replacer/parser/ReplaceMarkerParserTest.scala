package com.geishatokyo.codegen.replacer.parser

import org.specs2.mutable.Specification

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 15:54
 */
class ReplaceMarkerParserTest extends Specification{

  "Parser" should {

    "parse" in{

      val blocks = new ReplaceMarkerParser().parse(
        """
          |hoge
          |##hold aaa
          |  ##replace
          |
          |  dddd
          |
          |    ##hold bbb
          |      Can nest hold -> replace repeatedly.
          |    ##end
          |
          |  ##end
          |eee
          |//##end
          |
          |//##replace ddd
          |fjeiwo
          |  ##hold bbb
          |  ##end
          |##end
          |
          |
          |bbb
        """.stripMargin.lines)

      println(blocks)

      blocks must haveSize(5)


    }
  }


}
