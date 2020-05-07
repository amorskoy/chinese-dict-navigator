package org.bitheaven

import java.io._
import java.util
import java.util.Properties
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel
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
  private val basedir = System.getProperty("SegDemo", "data")

  private def segment(sample: String,
                      classifiers: Iterable[CRFClassifier[CoreLabel]]): Iterable[String] = {
    classifiers.map(segmenter => {
      segmenter.segmentString(sample).asScala.mkString(" ")
    })
  }

  private def makeSegmenters() = {
    val props = new Properties
    props.setProperty("sighanCorporaDict", basedir)
    props.setProperty("NormalizationTable", "data/norm.simp.utf8");
    props.setProperty("normTableEncoding", "UTF-8");
    // below is needed because CTBSegDocumentIteratorFactory accesses it
    props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz")
    props.setProperty("inputEncoding", "UTF-8")
    props.setProperty("sighanPostProcessing", "true")
    val segmenterCTB = new CRFClassifier[CoreLabel](props)
    val segmenterPKU = new CRFClassifier[CoreLabel](props)
    segmenterCTB.loadClassifierNoExceptions(basedir + "/ctb.gz", props)
    segmenterPKU.loadClassifierNoExceptions(basedir + "/pku.gz", props)

    segmenterCTB :: segmenterPKU :: Nil
  }

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    System.setOut(new PrintStream(System.out, true, "utf-8"))
    val segmenters = makeSegmenters()

    println("Input 'x' for exit. " + System.lineSeparator())
    Iterator.continually(scala.io.StdIn.readLine)
      .takeWhile(_ != "x")
      .foreach(sample => {
        val segmentedCases = segment(sample, segmenters)
        val ctbCase = segmentedCases.head

        NavigateDicts.main(Array(ctbCase))
        segmentedCases.foreach(println)
      })

  }
}
