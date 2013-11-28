package com.geishatokyo.codegen.exporter

import com.geishatokyo.codegen.generator.GeneratedCode
import java.io.File
import com.geishatokyo.codegen.util.RichFile._
import com.geishatokyo.codegen.replacer.Merger
import com.geishatokyo.codegen.util.Logger

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 21:03
 */
case class MergeExporter(groupName : String,extension : String,exportDir : File) extends NormalFileExporter {

  def beforeExport() {}

  def export(code: GeneratedCode, tempFilePath: File) {
    val baseFilePath = new File(exportDir,filename(code))
    merger.merge(baseFilePath,code.code)
  }

  def afterExport() {}
}
