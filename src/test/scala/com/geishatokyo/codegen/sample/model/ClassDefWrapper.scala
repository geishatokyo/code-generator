package com.geishatokyo.codegen.sample.model

import com.geishatokyo.codegen.dsl.parser.{Field, Scope, BodyElement, ClassDefinition}

/**
 * Created with IntelliJ IDEA.
 * User: takezoux2
 * Date: 2013/11/21
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
class ClassDefWrapper(classDef : ClassDefinition) {

  def name = classDef.name

  def getFieldsForScope(scope : String) = {

    val targetScope = scope

    def skipToScope( elements : List[BodyElement]) : List[BodyElement] = {
      elements match{
        case h :: tails => {
          h match{
            case Scope(scopes) => {
              if(scopes.contains(targetScope)){
                tails
              }else{
                skipToScope(tails)
              }
            }
            case _ => skipToScope(tails)
          }
        }
        case Nil => Nil
      }
    }
    def getFields(elements : List[BodyElement]) : List[Field] = {
      elements match{
        case h :: tails => {
          h match{
            case f : Field => f :: getFields(tails)
            case Scope(scopes) if scopes.contains(targetScope) => getFields(tails)
            case Scope(_) => getFields(skipToScope(tails))
            case _ => getFields(tails)
          }
        }
        case _ => Nil
      }
    }

    getFields(classDef.body)
  }
}
