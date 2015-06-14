package com.alex.kucher

import java.nio.charset.Charset
import java.nio.file.{Files, Path}

object Utils {
  def loadData(lines: Array[String]): Array[Double] = lines.mkString(" ").replaceAll("[ ]{2,}", " ").split(" ").map(_.toDouble)

  def loadData(path: Path): Array[Double] = Files.readAllLines(path, Charset.forName("UTF-8")).toArray.mkString(" ").replaceAll("[ ]{2,}", " ").split(" ").map(_.toDouble)
}
