package org.gospel.backend.processing

import java.net.URLEncoder

import scalaz.{-\/, \/, \/-}
import com.themillhousegroup.scoup.Scoup
import com.themillhousegroup.scoup.ScoupImplicits._
import org.gospel.backend.tools.Calendar
import org.jsoup.nodes.Document

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor}
import scalaz.std.`try`._

object Markup {
    private implicit val context: ExecutionContextExecutor = ExecutionContext.global

    def getMarkup(calendar: Calendar): Throwable \/ Seq[String] = {
        val future = Scoup.parse(calendar.link).map(extractReferences)
        val awaited = Await.ready(future, Duration.Inf).value.get
        toDisjunction(awaited)
    }

    private def extractReferences(document: Document) = {
        val markups = document.select("table th[align=right]")
        markups.filter(_.ownText.contains(" ")).map(_.ownText).toSeq
    }
}