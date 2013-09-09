package com.geishatokyo.codegen.exporter

import com.geishatokyo.codegen.generator.GeneratedCode
import java.io.File

/**
 *
 * User: takeshita
 * DateTime: 13/09/09 17:47
 */
trait FileExporter {

  def groupName : String

  def beforeExportToTemp()
  def exportToTemp(code : GeneratedCode) : File
  def afterExportToTemp()


  def checkExport : Boolean
  def beforeExport() : Unit
  def export( code : GeneratedCode,tempFilePath : File) : Unit
  def afterExport() : Unit

}
