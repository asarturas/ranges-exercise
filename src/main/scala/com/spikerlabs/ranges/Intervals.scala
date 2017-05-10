package com.spikerlabs.ranges

import scala.collection.immutable.SortedMap

trait Ranges {
  def countAt(num: Double): Int
}

// sample

case class SampleRanges(r: List[(Double, Double)]) extends Ranges {
  def countAt(num: Double): Int = {
    r.count {
      case (lower, higher) => lower <= num && num < higher
    }
  }
}

// optimised

trait RangePoint {
  def count: Int
  def diff: Int
}
case class Start(count: Int = 1, diff: Int = 1) extends RangePoint
case class End(count: Int = 0, diff: Int = -1) extends RangePoint

case class OptimisedRanges private(r: SortedMap[Double, RangePoint]) extends Ranges {
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
    def add(m: SortedMap[Double, RangePoint], location: Double, point: RangePoint) = {
      if (m.contains(location)) {
        m(location) match {
          case existingPoint if existingPoint.diff + point.diff == 0 => m - location
          case existingPoint: Start => m + (location -> existingPoint.copy(diff = existingPoint.diff + point.diff))
          case existingPoint: End => m + (location -> existingPoint.copy(diff = existingPoint.diff + point.diff))
        }
      } else {
        m + (location -> point)
      }
    }
    def addAll(m: SortedMap[Double, RangePoint], points: List[(Double, RangePoint)]): SortedMap[Double, RangePoint] =
      points match {
        case Nil => m
        case (key, point) :: tail => addAll(add(m, key, point), tail)
      }
    def index(m: SortedMap[Double, RangePoint], keys: List[Double], currentCount: Int = 0): SortedMap[Double, RangePoint] =
      keys match {
        case Nil => m
        case key :: restOfKeys if m.contains(key) =>
          m(key) match {
            case point : Start => index(
              m + (key -> point.copy(count = currentCount + point.diff)),
              restOfKeys,
              currentCount + point.diff
            )
            case point : End => index(
              m + (key -> point.copy(count = currentCount + point.diff)),
              restOfKeys,
              currentCount + point.diff
            )
        }
      }
    val points = r.flatMap {
      case (s, e) => List((s, Start()), (e, End()))
    }
    val draft = addAll(SortedMap.empty[Double, RangePoint], points)
    OptimisedRanges(index(draft, draft.keys.toList))
  }
}
