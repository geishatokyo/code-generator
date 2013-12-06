package com.geishatokyo.codegen.sample.api

import com.geishatokyo.codegen.generator.{GeneratedCode, Context, CodeGenerator}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/28
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
class PlayControllerGenerator extends CodeGenerator[APIWrapper] {
  def generate(models: List[APIWrapper])(implicit context: Context): List[GeneratedCode] = {

    models.groupBy(_.group).map(p => {
      gen(p._1,p._2)
    }).toList

  }

  private def gen( group : String , models : List[APIWrapper] ) = {

    val methods = models.map(m => {
      s"""
        |  def ${m.methodName}($m.methodParams) = Action({
        |    // implement heare
        |    Ok("Ok")
        |  })
      """.stripMargin


    })

    val code =
      s"""
        |// Just sample
        |object ${group.capitalize} extends Controller{
        |  ${methods.mkString}
        |
        |}
      """.stripMargin


    GeneratedCode("APIController",group,code)


  }

}
