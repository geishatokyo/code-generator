package com.geishatokyo.codegen.replacer

import org.specs2.mutable.Specification
import com.geishatokyo.codegen.util.FileUtil
import java.io.File

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

      s.trim ===
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
        """.stripMargin.trim


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

    "merge insert" in{
      val merger = new Merger
      val base =
        """
          |##insert here
        """.stripMargin
      val first = merger.merge(base,
        """
          |##replace here
          |new line
          |##end
        """.stripMargin)
      first.trim ===
        """
          |new line
          |##insert here
        """.stripMargin.trim

      val second = merger.merge(first,
        """
          |##replace here
          |new line2
          |##end
        """.stripMargin
      )


      second.trim() ===
        """
          |new line
          |new line2
          |##insert here
        """.stripMargin.trim

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

  "InsteadOf" should{
    "insert" in {

      val merger = new Merger
      val s = merger.merge("""
        |/*##insteadOf
        |自動生成が気に入らない場合は
        |##insert*/
        |
        |変わりにこれを挿入できます。
        |
        |//##end
        |
      """.stripMargin,
       """こっちは自動生成されたコード
        |自動生成が気に入らない場合は
        |なんとかしろ
        |
       """.stripMargin)

      s ===
       """こっちは自動生成されたコード
        |/*##insteadOf
        |自動生成が気に入らない場合は
        |##insert*/
        |
        |変わりにこれを挿入できます。
        |
        |//##end
        |なんとかしろ
        |
       """.stripMargin


    }

    "insert2" in {

      val merger = new Merger
      val s = merger.merge("""
         | ##hold
         | aaa
         | ##end
         |/*##insteadOf
         |  自動生成が気に入らない場合は""".stripMargin + "\n" +
         """|##insert*/
         |
         |変わりにこれを挿入できます。
         |
         |//##end
         | ##hold
         | aaa
         | ##end
         |""".stripMargin,
        """
          | ##hold
          | ##end
          | こっちは自動生成されたコード
          |  自動生成が気に入らない場合は""".stripMargin + "\r\n" +
          """ ##hold
            | aaa
            | ##end
            |なんとかしろ""".stripMargin)

      s ===
        """
          | ##hold
          | aaa
          | ##end
          | こっちは自動生成されたコード
          |/*##insteadOf
          |  自動生成が気に入らない場合は
          |##insert*/
          |
          |変わりにこれを挿入できます。
          |
          |//##end
          | ##hold
          | aaa
          | ##end
          |なんとかしろ""".stripMargin


    }

    "insert2" in {

      val merger = new Merger

      import com.geishatokyo.codegen.util.RichFile._

      val s = merger.merge(
        file("sample/Equipment_base.scala").readAsString(),
        file("sample/Equipment_replace.scala").readAsString())

      println("###########################")
      println(s)
      println("###########################")

      s.indexOf("##insteadOf") must greaterThan(0)


    }
  }

  def file(path : String) = {
    new File(getClass.getClassLoader.getResource(path).getFile)
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
