package org.bitheaven

import java.awt.Desktop
import java.net.{URI, URLEncoder}

import scala.util.Try

case class NavigatorParams(sentence: String)

object NavigateDicts {
  val googleBase = "https://translate.google.com.ua/?hl=en&tab=rT1&authuser=0#view=home&op=translate&sl=zh-CN&tl=ru&text="
  val yablaBase = "https://chinese.yabla.com/chinese-english-pinyin-dictionary.php?define="
  val bkrsBase = "https://bkrs.info/slovo.php?ch="

  private def parseParams(args: Array[String]): NavigatorParams = {
    Try {NavigatorParams(args(0))}
      .getOrElse(throw new IllegalArgumentException("""Arguments: "<chinese sentence>""""))
  }

  def navigate(uri: URI): Unit = {
    Desktop.getDesktop.browse(uri)
    Thread.sleep(200)
  }

  def main(args: Array[String]): Unit = {
    val params = parseParams(args)
    val sentence = URLEncoder.encode(params.sentence.replace(" ", ""), "utf-8")

    val parts = params.sentence
      .replace(" ", "")
      .split("[,:]")
      .map(part => URLEncoder.encode(part, "utf-8") )

    val googleUri = new URI(googleBase + sentence)
    val yablaUri = new URI(yablaBase + sentence)
    val bkrsUri = parts.map(part => new URI(bkrsBase + part)).toList

    val toNavigate = List(googleUri, yablaUri) ++ bkrsUri

    toNavigate.foreach(navigate)
  }
}
