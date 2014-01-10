package com.geishatokyo.codegen

import java.util.Date

/**
 * Created by takeshita on 14/01/10.
 */
object ExecutionHistory {

  private var executions : List[Execution] = Nil
  def add(execution : Execution) = {
    executions = executions :+ execution
  }

  case class Execution(definitions : List[Any] , executed : Date, dryRun : Boolean)


}
