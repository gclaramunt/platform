/*
 *  ____    ____    _____    ____    ___     ____ 
 * |  _ \  |  _ \  | ____|  / ___|  / _/    / ___|        Precog (R)
 * | |_) | | |_) | |  _|   | |     | |  /| | |  _         Advanced Analytics Engine for NoSQL Data
 * |  __/  |  _ <  | |___  | |___  |/ _| | | |_| |        Copyright (C) 2010 - 2013 SlamData, Inc.
 * |_|     |_| \_\ |_____|  \____|   /__/   \____|        All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU Affero General Public License as published by the Free Software Foundation, either version 
 * 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See 
 * the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this 
 * program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.precog.quirrel

trait Phases {
  type Expr
  type Error
  
  type ConditionTree
  
  type Phase = Expr => Set[Error]
  
  private val Phases: List[Phase] =
    bindNames _ :: checkProvenance _ :: solveCriticalConditions _ :: Nil
  
  def bindNames(expr: Expr): Set[Error]
  def checkProvenance(expr: Expr): Set[Error]
  def solveCriticalConditions(expr: Expr): Set[Error]
  
  def findCriticalConditions(expr: Expr): Map[String, Set[ConditionTree]]
  
  def runPhasesInSequence(tree: Expr): Set[Error] =
    Phases.foldLeft(Set[Error]()) { _ ++ _(tree) }
  
  def Error(node: Expr, tp: ErrorType): Error
  def showError(error: Error): String
}