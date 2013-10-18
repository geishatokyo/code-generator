package com.geishatokyo.codegen.exporter

import java.io.File
import com.geishatokyo.codegen.generator.GeneratedCode
import com.geishatokyo.codegen.util.RichFile._
import com.geishatokyo.codegen.util.Logger

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 22:17
 */
case class OverwriteExporter(groupName : String,extension : String,exportDir : File,overwrite : Boolean = false) extends NormalFileExporter {

  def beforeExport() {}

  def export(code: GeneratedCode, tempFilePath: File) {
    val f = new File(exportDir,filename(code))

    if (f.exists()){
      if (overwrite){
        Logger.log("Overwrite " + f.getAbsolutePath)
        f.write(code.code)
      }else{
        Logger.log("File:" + f.getAbsolutePath + " is already exists")
      }

    }else{
      Logger.log("Create " + f.getAbsolutePath)
      f.write(code.code)
    }

  }

  def afterExport() {}
}
