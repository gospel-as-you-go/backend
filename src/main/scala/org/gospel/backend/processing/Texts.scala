package org.gospel.backend.processing

import java.net.URLEncoder

import scalaz.{-\/, \/, \/-}
import com.themillhousegroup.scoup.Scoup
import com.themillhousegroup.scoup.ScoupImplicits._
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor}
import scalaz.std.`try`._

object Texts {
    private val encode = (x: String) => URLEncoder.encode(x, "UTF-8")
    private implicit val scoup: Scoup.type = Scoup
    private implicit val context: ExecutionContextExecutor = ExecutionContext.global
    private val bibleGatewayLink = "https://www.biblegateway.com/passage/?search="

    def findTextByRef(ref: String, version: String)
                     (implicit scoup: Scoup = scoup): Throwable \/ String = {
        val endpoint = s"$bibleGatewayLink${encode(ref)}&version=${encode(version)}"
        val future = scoup.parse(endpoint).map(extractTexts)
        val awaited = Await.ready(future, Duration.Inf).value.get
        toDisjunction(awaited)
    }

    private def extractTexts(document: Document) = {
        val markups: Elements = document.select("p>span.text")
        markups.filter(_.ownText().trim().length() > 0)
            .map(x => s"<span class='${x.className()}'>${x.html()}</span>")
            .fold("")(_+"\n"+_).replaceAll("\n", "")
    }
}
