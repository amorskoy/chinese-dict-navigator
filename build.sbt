name := "ChineseDictNavigator"

version := "0.1"

scalaVersion := "2.13.1"

assemblyOutputPath in assembly := file("dist/chinese-dict-navigator.jar")

libraryDependencies += "stanford" % "segmenter" % "3.9.2" %
  Provided from ("file:///projects/ChineseDictNavigator/deps/stanford-segmenter-3.9.2.jar")