package com.spikerlabs.ranges

import scala.collection.immutable.SortedMap

trait Ranges {
  def countAt(num: Double): Int
}

case class SampleRanges(r: List[(Double, Double)]) extends Ranges {
  def countAt(num: Double): Int = {
    r.count {
      case (lower, higher) => lower <= num && num < higher
    }
  }
}
