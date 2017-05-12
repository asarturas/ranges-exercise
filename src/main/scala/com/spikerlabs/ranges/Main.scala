package com.spikerlabs.ranges

import scala.io.{StdIn, Source}

object Main extends App {
  if (args.isEmpty) {
    println("provide source file name as attribute")
    System.exit(-1)
  }
  // read data
  val sourceFile = args.head
  val sourceNumbers: List[Double] = Source.fromFile(sourceFile).getLines().flatMap(_.split(' ')).map(_.toDouble).toList
  val sourceRanges: List[(Double, Double)] = sourceNumbers.grouped(2).toList.map {
    case List(a, b) => (a, b)
  }
  // compute
  val ranges = OptimisedRanges(sourceRanges)
  println("Enter number or Cmd+D to terminate")
  while (true) {
    val n = StdIn.readDouble
    println(ranges.countAt(n))
  }
}
