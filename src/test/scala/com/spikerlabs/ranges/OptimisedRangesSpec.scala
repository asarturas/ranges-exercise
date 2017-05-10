package com.spikerlabs.ranges

import org.scalatest.{FlatSpec, Matchers}

class OptimisedRangesSpec extends FlatSpec with Matchers {
  it should "count at point matching exactly with start" in {
    val ranges = OptimisedRanges(List((1.0, 2.0)))
    ranges.countAt(1.0) should be(1)
  }
  it should "count when contains single range" in {
    val ranges = OptimisedRanges(List((1.0, 3.0)))
    ranges.countAt(0.5) should be(0)
    ranges.countAt(1.0) should be(1)
    ranges.countAt(2.0) should be(1)
    ranges.countAt(3.0) should be(0)
    ranges.countAt(3.5) should be(0)
  }
  it should "count when range has a subrange" in {
    val ranges = OptimisedRanges(List((1.0, 10.0), (3.0, 8.0)))
    ranges.countAt(2.0) should be(1)
    ranges.countAt(5.0) should be(2)
    ranges.countAt(9.0) should be(1)
  }
  it should "count when ranges intersect" in {
    val ranges = OptimisedRanges(List((1.0, 5.0), (3.0, 7.0)))
    ranges.countAt(2.0) should be(1)
    ranges.countAt(4.0) should be(2)
    ranges.countAt(6.0) should be(1)
  }
  it should "count dummy range" in {
    val ranges = OptimisedRanges(List((1.0, 2.0), (1.0, 4.0), (7.0, 9.0), (1.0, 10.0), (3.0, 5.0)))
    ranges.countAt(1.0) should be(3)
    ranges.countAt(2.0) should be(2)
    ranges.countAt(3.0) should be(3)
    ranges.countAt(4.0) should be(2)
    ranges.countAt(5.0) should be(1)
    ranges.countAt(6.0) should be(1)
    ranges.countAt(7.0) should be(2)
    ranges.countAt(8.0) should be(2)
    ranges.countAt(9.0) should be(1)
    ranges.countAt(10.0) should be(0)
  }
  it should "count sample range" in {
    val ranges = OptimisedRanges(List((0.0, 12.0), (106.0, 122.0), (123.0, 124.0), (11.0, 33.0), (33.0, 34.0)))
    ranges.countAt(2.0) should be(1)
    ranges.countAt(3.0) should be(1)
    ranges.countAt(5.0) should be(1)
    ranges.countAt(28.0) should be(1)
    ranges.countAt(64.0) should be(0)
    ranges.countAt(35.0) should be(0)
    ranges.countAt(63.0) should be(0)
    ranges.countAt(27.0) should be(1)
    ranges.countAt(44.0) should be(0)
    ranges.countAt(11.5) should be(2)
    ranges.countAt(12.0) should be(1)
    ranges.countAt(33.0) should be(1)
  }
}
