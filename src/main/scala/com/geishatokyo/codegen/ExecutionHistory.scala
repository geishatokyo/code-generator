package com.geishatokyo.codegen

import java.util.Date

/**
 * Created by takeshita on 14/01/10.
 */
object ExecutionHistory {

  private var _executions : List[Execution] = Nil
  def add(execution : Execution) = {
    _executions = _executions :+ execution
  }
  def executions = _executions

  case class Execution(definitions : List[Any] , executed : Date, dryRun : Boolean)


}
