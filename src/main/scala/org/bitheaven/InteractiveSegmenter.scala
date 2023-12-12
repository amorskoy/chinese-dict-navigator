package org.bitheaven

import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel
import org.bitheaven.TranslateMode.{Regular, Systran, TranslateMode}

import java.io._
import java.net.ServerSocket
import java.util.Properties
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ParIterable
import scala.jdk.CollectionConverters._

/** This is a very simple demo of calling the Chinese Word Segmenter
 *  programmatically.  It assumes an input file in UTF8.
 * <p/>
 * <code>
 * Usage: java -mx1g -cp seg.jar SegDemo fileName
 * </code>
 * This will run correctly in the distribution home directory.  To
 * run in general, the properties for where to find dictionaries or
 * normalizations have to be set.
 *
 * @author Christopher Manning
 */
object InteractiveSegmenter {
  val NET_PORT = 8888

  private val basedir = System.getProperty("SegDemo", "data")
  var lastSegments = ""

  def segment(sample: String,
                      classifiers: ParIterable[CRFClassifier[CoreLabel]]): ParIterable[String] = {
    classifiers.map(segmenter => {
      segmenter.segmentString(sample).asScala.mkString(" ")
    })
  }

  def makeSegmenters() = {
    val props = new Properties
    props.setProperty("sighanCorporaDict", basedir)
    props.setProperty("NormalizationTable", "data/norm.simp.utf8");
    props.setProperty("normTableEncoding", "UTF-8");
    // below is needed because CTBSegDocumentIteratorFactory accesses it
    props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz")
    props.setProperty("inputEncoding", "UTF-8")
    props.setProperty("sighanPostProcessing", "true")

    val dictsLocations = Array(basedir + "/ctb.gz", basedir + "/pku.gz").par

    val segmenters = dictsLocations.map(dictLocation => {
      val segmenter = new CRFClassifier[CoreLabel](props)
      segmenter.loadClassifierNoExceptions(dictLocation, props)

      segmenter
    })

    segmenters
  }

  var segmenters: ParIterable[CRFClassifier[CoreLabel]] = null
  def setSegmenters(segmenters: ParIterable[CRFClassifier[CoreLabel]]) = {
    InteractiveSegmenter.segmenters = segmenters
  }

  def getNavigationStrategy(mode: TranslateMode): NavigationStrategy = mode match {
    case Regular => new RegularNavigation
    case Systran => new SystranNavigation
  }

  def printLine(line: String): Unit = println(line + System.lineSeparator())

  def handleMessage(message: String) = message match {
    case "s" =>
      printLine("Opening Systran for the previous input")
      getNavigationStrategy(Systran).navigateSentence(lastSegments)
    case "" =>
    case sample =>
      // val segmentedCases = segment(sample, segmenters).map(_.replace(" , ", ","))
      // val ctbCase = segmentedCases.head

      getNavigationStrategy(Regular).navigateSentence(sample)
    //segmentedCases.foreach(println)

    // We have only Systran mode, which enabled once per input and then resets
    // lastSegments = ctbCase
  }

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val mode = args(0)
    printLine(mode)

    System.setOut(new PrintStream(System.out, true, "utf-8"))
    setSegmenters(makeSegmenters())

    printLine(
      """Input:
        |     'x' for exit.
        |     's' to open Systran for the previous input""".stripMargin)

    mode match {
      case "interactive" =>
        Iterator.continually(scala.io.StdIn.readLine)
          .takeWhile(_.trim() != "x")
          .foreach(handleMessage)

      case "network" =>
        val ss = new ServerSocket(NET_PORT)
        val socket = ss.accept()
        val in = new BufferedReader(new InputStreamReader(socket.getInputStream))

        Iterator.continually(in.readLine())
          .takeWhile(_.trim() != "x")
          .foreach(handleMessage)

        socket.close()
        ss.close()

      case _ => printLine(s"Unknown mode: $mode")
    }

  }
}

object TranslateMode extends Enumeration {
  type TranslateMode = Value

  /**
   * Regular - segment and navigate Google, Yabla, BKRS
   * Systran - Navigate Systran on previous input
   */
  val Regular, Systran = Value
}
