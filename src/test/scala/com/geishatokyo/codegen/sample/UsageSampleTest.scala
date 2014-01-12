package com.geishatokyo.codegen.sample

import org.specs2.mutable.Specification
import com.geishatokyo.codegen.dsl.parser.model.ClassDSLParser
import com.geishatokyo.codegen.dsl.parser.GenericDSLParser
import com.geishatokyo.codegen.Generator
import com.geishatokyo.codegen.exporter.OverwriteExporter
import java.io.File
import com.geishatokyo.codegen.sample.model.{WrapperConverter, ScalaCaseClassGenerator}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
class UsageSampleTest extends Specification {

  "Models" should{
    "export as such" in {
      val generator = new Generator(MyClassParser)

      generator += new WrapperConverter()
      generator += new ScalaCaseClassGenerator()
      generator += new OverwriteExporter("CaseClass",".scala",new File("target/gen"),true)


      generator.generate(
        getClass.getClassLoader.getResourceAsStream("sns.model"),false)

    }
  }


}

