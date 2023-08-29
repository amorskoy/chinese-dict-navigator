name := "ChineseDictNavigator"

version := "0.1"

scalaVersion := "2.13.1"

assemblyOutputPath in assembly := file("dist/chinese-dict-navigator.jar")

val currentDirectory = new java.io.File(".").getCanonicalPath

libraryDependencies += "stanford" % "segmenter" % "3.9.2" %
  Provided from ("file://" +currentDirectory+ "/deps/stanford-segmenter-3.9.2.jar")

libraryDependencies +=
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"