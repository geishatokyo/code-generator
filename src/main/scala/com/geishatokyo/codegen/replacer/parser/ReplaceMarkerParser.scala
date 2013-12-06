package com.geishatokyo.codegen.replacer.parser

import util.parsing.combinator.RegexParsers
import annotation.tailrec

/**
 * 
 * User: takeshita
 * DateTime: 13/09/06 15:17
 */
class ReplaceMarkerParser {

  val holdMarker = "##hold"
  val replaceMarker = "##replace"

  val endMarker = "##end"

  val insteadOfMarker = "##insteadOf"
  val insertMarker = "##insert"

  def parse( str : String) : List[Block] = parse(str.lines)

  def parse( lines : Iterator[String]) : List[Block] = {
    parseOutside(new CurrentHoldIterator(lines))

  }

  class CurrentHoldIterator(innerIte : Iterator[String]) extends Iterator[String] {

    private var currentLine : String = null

    def hasNext = innerIte.hasNext

    def next() = {
      currentLine = innerIte.next()
      currentLine
    }

    def current = currentLine

    private var index = 0

    def nextIndex {
      index += 1
      index
    }
  }


  def parseOutside(lines : CurrentHoldIterator) : List[Block] = {

    if (!lines.hasNext) return Nil

    var blockLines : List[String] = Nil
    var blocks : List[Block] = Nil

    while(lines.hasNext){
      var line = lines.next()
      if (line.contains(holdMarker)){
        blocks = blocks ::: List(StringBlock(blockLines),parseInsideHold(lines))
        blockLines = Nil
      }else if (line.contains(replaceMarker)){
        blocks = blocks ::: List(StringBlock(blockLines),parseInsideReplace(lines))
        blockLines = Nil
      }else if(line.contains(insteadOfMarker)){
        blocks = blocks ::: List(StringBlock(blockLines),parseInsteadOf(lines))
        blockLines = Nil
      }else if(line.contains(insertMarker)){
        val name = line.substring(line.indexOf(insertMarker) + insertMarker.length).trim
        blocks = blocks ::: List(StringBlock(blockLines),InsertBlock(name,List(StringBlock(List(line)))))
        blockLines = Nil
      }else{
        blockLines = blockLines :+ line
      }
    }

    if (blockLines.size > 0){
      blocks = blocks :+ StringBlock(blockLines)
    }

    blocks
  }


  def parseInsteadOf(lines : CurrentHoldIterator) : InsteadOfBlock = {
    var targetLines = List[String]()
    var allLines = List[String](lines.current)
    var finishParseInsteadOfArea = false
    var end = false

    while(lines.hasNext && !finishParseInsteadOfArea){
      val line = lines.next()
      allLines = line :: allLines
      if(line.contains(insertMarker)){
        finishParseInsteadOfArea = true;
      }else{
        targetLines = line :: targetLines
      }

    }

    while(lines.hasNext && !end){
      val line = lines.next()
      allLines = line :: allLines

      if(line.contains(endMarker)){
        end = true
      }

    }

    InsteadOfBlock(targetLines.reverse, allLines.reverse)

  }

  def parseInsideHold(lines : CurrentHoldIterator) : HoldBlock = {

    val current = lines.current
    val name = {
      val s = current.substring(current.indexOf(holdMarker) + holdMarker.length).split(" ")
      s.find(_.length > 0).getOrElse {
        val i = lines.nextIndex
        "hold" + i
      }
    }


    var blockLines : List[String] = List(current)
    var blocks : List[Block] = Nil
    var end = false

    while(lines.hasNext && !end){
      var line = lines.next()
      if (line.contains(replaceMarker)){
        blocks = blocks ::: List(StringBlock(blockLines),parseInsideReplace(lines))
        blockLines = Nil
      }else if (line.contains(endMarker)){
        blockLines = blockLines :+ line
        end = true
      }else{
        blockLines = blockLines :+ line
      }
    }

    blocks = blocks :+ StringBlock(blockLines)


    HoldBlock(name,blocks)
  }



  def parseInsideReplace(lines : CurrentHoldIterator) : ReplaceBlock = {

    val current = lines.current
    val name = {
      val s = current.substring(current.indexOf(replaceMarker) + replaceMarker.length).split(" ")
      s.find(_.length > 0).getOrElse{
        var i = lines.nextIndex
        "replace" + i
      }
    }


    var blockLines : List[String] = List(current)
    var blocks : List[Block] = Nil
    var end = false

    while(lines.hasNext && !end){
      var line = lines.next()
      if (line.contains(holdMarker)){
        blocks = blocks ::: List(StringBlock(blockLines),parseInsideHold(lines))
        blockLines = Nil
      }else if (line.contains(endMarker)){
        blockLines = blockLines :+ line
        end = true
      }else{
        blockLines = blockLines :+ line
      }
    }

    blocks = blocks :+ StringBlock(blockLines)


    ReplaceBlock(name,blocks)
  }

}
