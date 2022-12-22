package org.bitheaven

import java.awt.Desktop
import java.net.{URI, URLEncoder}
import sys.process._

abstract class NavigationStrategy {
  def getUriList(sentence: String): List[URI]

  def normalize(inputSentence: String): String = URLEncoder.encode(inputSentence, "utf-8")

  def navigateUri(uri: URI): Unit = {
    Desktop.getDesktop.browse(uri)
    Thread.sleep(200)
  }

  def navigateSentence(sentence: String): Unit = {
    getUriList(sentence).foreach(navigateUri)
  }
}


class RegularNavigation extends NavigationStrategy {
  val googleBase = "https://translate.google.com.ua/?hl=en&tab=rT1&authuser=0#view=home&op=translate&sl=zh-CN&tl=ru&text="
  val yablaBase = "https://chinese.yabla.com/chinese-english-pinyin-dictionary.php?define="
  val bkrsBase = "https://bkrs.info/slovo.php?ch="

  override def getUriList(inputSentence: String): List[URI] = {
    val sentence = normalize(inputSentence)
    val resolvedSentence = normalize(resolveByDict(inputSentence))

    //For Yabla we want to allow it's own segmentation to compare within edge cases
    val sentenceNoSegments = URLEncoder.encode(inputSentence.replace(" ", ""), "utf-8")

    val googleUri = new URI(googleBase + sentence)
    val googleResolvedUri = new URI(googleBase + resolvedSentence)
    val yablaUri = new URI(yablaBase + sentenceNoSegments)

    val parts = resolvedSentence
      .split("[,:;]")
      .map(part => URLEncoder.encode(part, "utf-8") )

    val bkrsUri = parts.map(part => new URI(bkrsBase + part)).toList

    List(googleUri, googleResolvedUri, yablaUri) ++ bkrsUri
  }

  def resolveByDict(inputSentence: String): String = {
    val cmd = "python3 py/lookup_words.py \"" + inputSentence + "\""

    Process(cmd).!!
  }
}


class SystranNavigation extends NavigationStrategy {
  val systranBase = "https://translate.systran.net/translationTools/text?source=zh&target=en&input="

  override def getUriList(inputSentence: String): List[URI] = {
    val systranUri = new URI(systranBase + normalize(inputSentence))

    List(systranUri)
  }
}
