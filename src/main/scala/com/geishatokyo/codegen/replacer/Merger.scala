package com.geishatokyo.codegen.replacer

import parser._
import parser.HoldBlock
import parser.ReplaceBlock
import scala.Some
import java.io.File
import com.geishatokyo.codegen.util.RichFile._
import com.geishatokyo.codegen.util.Logger

/**
 * 
 * User: takeshita
 * DateTime: 13/09/09 16:04
 */
class Merger {
  val parser = new ReplaceMarkerParser

  val lineSep = System.getProperty("line.separator")

  def merge( baseFilePath : File,replaceFile : File) : File = {

    if(!replaceFile.exists()) return baseFilePath
    else merge(baseFilePath, replaceFile.readAsString())
  }
  def merge( baseFilePath : File,replace : String) : File = {


    if (baseFilePath.exists()){
      val baseFile = baseFilePath.readAsString()
      val b = merge(baseFile,replace)
      if (b != baseFile){
        Logger.log("Merger file " + baseFilePath.getAbsolutePath)
        baseFilePath.write(b)
      }else{
        Logger.log("File didn't change:" + baseFilePath.getAbsolutePath)
      }
      baseFilePath
    }else{
      Logger.log("Create new file " + baseFilePath.getAbsolutePath)
      baseFilePath.write(replace)
      baseFilePath
    }
  }


  def merge( base : String, replace : String) : String = {

    val baseBlocks = parser.parse(base)
    val replaceBlocks = parser.parse(replace)

    val topLevel = getTopLevel(baseBlocks)
    if(topLevel != getTopLevel(replaceBlocks)){
      throw new Exception("Top level block types are different.")
    }

    val s = topLevel match{
      case TopLevel.Hold => {
        holdMerge(baseBlocks,replaceBlocks)
      }
      case TopLevel.Replace => {
        replaceMerge(baseBlocks,replaceBlocks)
      }
      case _ => {
        if(baseBlocks.exists(_.isInstanceOf[InsteadOfBlock])){
          holdMerge(baseBlocks,replaceBlocks)
        }else{
          toString(replaceBlocks)
        }
      }
    }

    if (s.length > 0){
      s.substring(0,s.length - lineSep.length)
    }else{
      s
    }

  }



  protected def holdMerge( base : List[Block], replace : List[Block]) : String = {
    val builder = new StringBuilder()
    val nameMap = toNameMap(base)
    var insteadOfBlocks = base.collect({
      case insteadOf : InsteadOfBlock => insteadOf
    })

    replace.foreach({
      case StringBlock(lines) => {
        val _lines = if(insteadOfBlocks.size > 0){
          val (replaced,_lines) = applyInsteadOf(insteadOfBlocks.head,lines)
          if(replaced){
            insteadOfBlocks = insteadOfBlocks.tail
          }
          _lines
        }else{
          lines
        }
        _lines.foreach(l => builder.append(l + lineSep))
      }
      case HoldBlock(name,blocks) => {
        nameMap.get("hold." + name) match{
          case Some(_blocks) => {
            builder.append(replaceMerge(_blocks,blocks))
          }
          case _ => {
            Logger.log("HoldBlock:" + name + " not found.")
            builder.append(toString(blocks))
          }
        }
      }
      case ReplaceBlock(name,blocks) => {
        builder.append(toString(blocks))
      }
      case InsteadOfBlock(targets,lines) => {
        lines.foreach(l => builder.append(l + lineSep))
      }
      case InsertBlock(name,blocks) => {
        builder.append(toString(blocks))
      }
    })
    builder.toString()
  }

  protected def replaceMerge(base : List[Block],replace : List[Block]) : String = {
    val builder = new StringBuilder()
    val nameMap = toNameMap(replace)

    base.foreach({
      case StringBlock(lines) => {
        lines.foreach(l => builder.append(l + lineSep))
      }
      case HoldBlock(name,blocks) => {
        builder.append(toString(blocks))

      }
      case ReplaceBlock(name,blocks) => {
        nameMap.get("replace." + name) match{
          case Some(_blocks) => {
            builder.append(holdMerge(blocks,_blocks))
          }
          case _ => {
            Logger.log("ReplaceBlock:" + name + " not found.")
            builder.append(toString(blocks))
          }
        }
      }
      case InsertBlock(name,blocks) => {
        nameMap.get("replace." + name) match{
          case Some(_blocks) => {
            val v = toString(_blocks.map(b => b match{
              case StringBlock(lines) => {
                StringBlock(lines.drop(1).dropRight(1))
              }
              case _ => b
            }))

            builder.append(v)
            builder.append(toString(blocks))
          }
          case None => {
            builder.append(toString(blocks))
          }
        }
      }
      case AppendBlock(name,blocks) => {
        (nameMap.get("append." + name) orElse nameMap.get("replace." + name)) match{
          case Some(List(StringBlock(lines))) => {
            builder.append(toString(List(StringBlock(lines.drop(1).dropRight(1)))))
            builder.append(toString(blocks))
          }
          case None => {
            builder.append(toString(blocks))
          }
        }
      }
    })
    builder.toString()
  }


  protected def toString( blocks : List[Block]) : String = {
    val builder = new StringBuilder()
    blocks.foreach({
      case StringBlock(lines) => {
        lines.foreach(l => builder.append(l + lineSep))
      }
      case HoldBlock(_,blocks) => {
        builder.append(toString(blocks))
      }
      case ReplaceBlock(_,blocks) => {
        builder.append(toString(blocks))
      }
    })
    builder.toString()
  }


  protected def toNameMap(blocks : List[Block]) = {
    blocks.collect({
      case HoldBlock(name,blocks) => ("hold." + name) -> blocks
      case ReplaceBlock(name,blocks) => ("replace." + name) -> blocks
      case InsertBlock(name,blocks) => ("replace." + name) -> blocks
      case AppendBlock(name,blocks) => ("append." + name) -> blocks
    }).toMap

  }

  protected def getTopLevel(blocks : List[Block]) = {

    val topLevel = blocks.collectFirst({
      case HoldBlock(_,_) => TopLevel.Hold
      case ReplaceBlock(_,_) => TopLevel.Replace
      case i : InsertBlock => TopLevel.Replace
      case i : InsteadOfBlock => TopLevel.Hold
      case AppendBlock(_,_) => TopLevel.Replace
    })

    topLevel match{
      case Some(TopLevel.Hold) => {
        if (blocks.exists( b => {
          b.isInstanceOf[ReplaceBlock] |
          b.isInstanceOf[InsertBlock]
        })){
          throw new Exception("Can't use both hold and replace marker on top level.")
        }
      }
      case Some(TopLevel.Replace) => {
        if (blocks.exists(_.isInstanceOf[HoldBlock])){
          throw new Exception("Can't use both hold and replace marker on top level.")
        }
      }
      case _ =>
    }

    topLevel getOrElse TopLevel.Hold

  }


  def applyInsteadOf(insteadOf : InsteadOfBlock,lines : List[String]) = {
    var i = lines.indexOfSlice(insteadOf.replaceTarget)

    if(i >= 0){
      true -> (lines.take(i) ::: insteadOf.allLines ::: lines.drop(i + insteadOf.replaceTarget.size))
    }else{
      false -> lines
    }

  }

}

object TopLevel extends Enumeration{
  val Nothing,Hold,Replace = Value
}
