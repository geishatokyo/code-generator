package com.geishatokyo.codegen.exporter

import java.io.File
import com.geishatokyo.codegen.replacer.Merger
import com.geishatokyo.codegen.generator.GeneratedCode
import com.geishatokyo.codegen.util.RichFile._

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 22:18
 */
trait NormalFileExporter extends FileExporter {

  def extension : String
  def exportDir : File

  lazy val dir = new File("target/code_gen/" + groupName)
  val merger = new Merger

  lazy val ext = if (extension.startsWith(".")) extension else ("." + extension)

  def filename(code : GeneratedCode) = code.name + ext

  def beforeExportToTemp() {
    if (dir.exists()){
      dir.deleteDir()
    }
    dir.mkdirs()
  }


  def exportToTemp(code: GeneratedCode) = {
    val f = dir / filename(code)
    f.write(code.code)
  }

  def afterExportToTemp() {}

  def checkExport = {
    exportDir.exists()
  }

}
