package com.spikerlabs.ranges

import scala.collection.immutable.SortedMap

case class Point(count: Int, diff: Int)
object Point {
  def Start(count: Int = 1, diff: Int = 1): Point = Point(count, diff)
  def End(count: Int = 0, diff: Int = -1): Point = Point(count, diff)
}

case class OptimisedRanges private(r: SortedMap[Double, Point]) extends Ranges {
  def countAt(num: Double): Int = {
    r.takeWhile {
      case (key, _) => key <= num
    }.lastOption.map {
      case (_, point) => point.count
    }.getOrElse(0)
  }
}

object OptimisedRanges {
  def apply(r: List[(Double, Double)]): OptimisedRanges = {
    val points = r.flatMap {
      case (s, e) => List((s, Point.Start()), (e, Point.End()))
    }
    val draft = addAll(SortedMap.empty[Double, Point], points)
    OptimisedRanges(index(draft, draft.keys.toList))
  }

  private def add(m: SortedMap[Double, Point], location: Double, point: Point) = {
    if (m.contains(location)) {
      m(location) match {
        case existingPoint if existingPoint.diff + point.diff == 0 => m - location
        case existingPoint => m + (location -> existingPoint.copy(diff = existingPoint.diff + point.diff))
      }
    } else {
      m + (location -> point)
    }
  }

  private def addAll(m: SortedMap[Double, Point], points: List[(Double, Point)]): SortedMap[Double, Point] =
    points match {
      case Nil => m
      case (key, point) :: tail => addAll(add(m, key, point), tail)
    }

  private def index(m: SortedMap[Double, Point], keys: List[Double], currentCount: Int = 0): SortedMap[Double, Point] =
    keys match {
      case Nil => m
      case key :: restOfKeys if m.contains(key) =>
        val point = m(key)
        val newCount = currentCount + point.diff
        index(m + (key -> point.copy(count = newCount)), restOfKeys, newCount)
    }
}
