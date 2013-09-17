package com.geishatokyo.codegen.exporter

import com.geishatokyo.codegen.generator.GeneratedCode
import java.io.File
import com.geishatokyo.codegen.util.RichFile._
import com.geishatokyo.codegen.replacer.Merger

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 21:03
 */
case class MergeExporter(groupName : String,extension : String,exportDir : File) extends NormalFileExporter {

  def beforeExport() {}

  def export(code: GeneratedCode, tempFilePath: File) {
    val baseFilePath = new File(exportDir,filename(code))
    if (baseFilePath.exists()){
      val baseFile = baseFilePath.readAsString()
      val b = merger.merge(baseFile,code.code)
      if (b != baseFile){
        println("Merger file " + baseFilePath.getAbsolutePath)
        baseFilePath.write(b)
      }else{
        println("File didn't change:" + baseFilePath.getAbsolutePath)
      }
    }else{
      println("Create new file " + baseFilePath.getAbsolutePath)
      tempFilePath.copyTo(baseFilePath)
    }
  }

  def afterExport() {}
}
