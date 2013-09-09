package com.geishatokyo.codegen.replacer

import org.specs2.mutable.Specification

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 16:15
 */
class MergerTest extends Specification {

  "Correct format" should{
    "merge hold type" in {
      val merger = new Merger

      val s = merger.merge(

        """Only hold marker is left.
          |
          |##hold
          |  This message is left.
          |  ##replace
          |    But inside of replace is replaced.
          |  ##end
          |  Do you understand?
          |##end
          |
          |OK?
          |
        """.stripMargin,
        """
          | Out side of hold
          |
          |##hold
          |  ##replace
          |    Replaced
          |  ##end
          |##end
          |
          |Great!
        """.stripMargin)

      s ===
        """
          | Out side of hold
          |
          |##hold
          |  This message is left.
          |  ##replace
          |    Replaced
          |  ##end
          |  Do you understand?
          |##end
          |
          |Great!
        """.stripMargin


    }
    "merge replace type" in {
      val merger = new Merger

      val s = merger.merge(

        """If top level marker is replace,
          |
          |##replace
          |  only inside of replace marker is replaces.
          |  ##hold
          |    But nested hold block is preserved.
          |  ##end
          |  Do you understand?
          |##end
          |
          |Yeah!
          |
        """.stripMargin,
        """
          |Discarded
          |
          |##replace
          |  This message is replaced.
          |
          |  ##hold
          |    Discarded
          |  ##end
          |
          |  Good!
          |##end
          |
          |Discarded
          |
          |Great!
        """.stripMargin)

      s ===
        """If top level marker is replace,
          |
          |##replace
          |  This message is replaced.
          |
          |  ##hold
          |    But nested hold block is preserved.
          |  ##end
          |
          |  Good!
          |##end
          |
          |Yeah!
          |
        """.stripMargin
    }


  }

  "Named marker" should{
    "be safety" in {

      val merger = new Merger
      val s = merger.merge(
        """
          |##hold h1
          |  place holder 1
          |##end
          |
          |##hold h2
          |  place holder 2
          |##end
          |
        """.stripMargin,
        """
          |a
          |##hold h2
          |  hold name is reversed
          |##end
          |b
          |##hold h1
          |  BBBBB
          |##end
          |b
        """.stripMargin)

      s ===
        """
          |a
          |##hold h2
          |  place holder 2
          |##end
          |b
          |##hold h1
          |  place holder 1
          |##end
          |b
        """.stripMargin

    }
  }


  "Incorrect format" should{
    "fail to merge mixed top level" in{

      val merger = new Merger

      merger.merge(
        """
          |##hold
          |  must use one of hold or replace on top level.
          |##end
          |
          |##replace
          |  such template throws exception
          |##end
        """.stripMargin,
      "hoge") must throwA[Exception]


    }
  }

}
