package com.geishatokyo.codegen.dsl.parser.model

import org.specs2.mutable.Specification
import com.geishatokyo.codegen.dsl.parser._
import com.geishatokyo.codegen.dsl.parser.ClassDefinition
import com.geishatokyo.codegen.dsl.parser.Field

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
class ClassDSLParserTest extends Specification {
  "GenericDSLParser" should{

    "parse" in{
      val defs = ClassDSLParserImpl.parse(
        """
          |
          |#comment
          |@class User
          |  id : Long PK
          |  nickname : String contains(emoji)
          |  age : Int
          |  lastLogin : Date
          |  accessKey : String
          |  status : UserStatus
          |
          |  +scope server
          |  roles : List[Role]
          |
          |  +index lastLogin desc
          |  +unique accessKey
          |
          |@class Role : Resource
          |  id : Int PK AI
          |  name : String
          |
          |@enum UserStatus
          |  Enabled = 1
          |  Banned = 2
          |
        """.stripMargin)

      defs.size === 3

      val u = defs(0).asInstanceOf[ClassDefinition]
      u.name === "User"
      u.options.size === 0
      u.body.size === 10
      u.body(0) === Field("id",SimpleData("Long"),List(NoArgsOption("PK")))
      u.body(1) === Field("nickname",SimpleData("String"),List(ArgsOption("contains",List(NoArgsOption("emoji"))) ))
      u.body(2) === Field("age",SimpleData("Int"),Nil)
      u.body(3) === Field("lastLogin",SimpleData("Date"),Nil)
      u.body(4) === Field("accessKey",SimpleData("String"),Nil)
      u.body(5) === Field("status",SimpleData("UserStatus"),Nil)


    }
  }


  object ClassDSLParserImpl extends ClassDSLParser with GenericDSLParser{
    def definitionsDef = {
      classDefinitionDef | enumDefinitionDef
    }
  }

}
