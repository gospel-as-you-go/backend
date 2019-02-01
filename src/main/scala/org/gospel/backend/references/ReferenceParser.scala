package org.gospel.backend.references

import java.text.Normalizer.Form

import org.gospel.backend.tools.functional._
import scalaz.{-\/, \/, \/-}

import scala.collection.SortedMap
import java.text.Normalizer.{normalize => jnormalize, _}

import scala.util.matching.Regex.Match


case class Reference(book: String, chapters: SortedMap[Int, List[Int]])

object ReferenceParser {
    def apply(): ReferenceParser = new ReferenceParser()
}

class ReferenceParser {
    private val chapterIntervalExpander = new ChapterIntervalExpander()
    private val chaptersIntervalDelimiter = "--"
    private val versesIntervalDelimiter = "-"
    private val commaDelimiter = ","
    private val colonDelimiter = ":"
    private val semicolonDelimiter = ";"
    private val versesIntervalsRegex = "(\\d+)\\s*-\\s*(\\d+)".r
    private lazy val booksRegex = s"^(?i)(${Books.structure.keys.mkString("|")})".r

    def parse(ref: String): Throwable \/ Reference = {
        for {
            normalized <- ref |> normalize |> toUnion
            bookTuple <- normalized |> splitIntoBook |> toUnion
            chapters <- bookTuple._2 |> splitIntoChapters(bookTuple._1)
            result <- Reference(bookTuple._1, chapters) |> toUnion
        } yield result
    }

    private def toUnion[T](x: T) = \/-.apply(x)
    private def toInt(x: String) = x.trim.toInt
    private def toSequence(x: Match) =
        x.group(1) |> toInt to x.group(2) |> toInt

    private def mapAfterBook(afterBook: String) = {
        val chaptersSplit = afterBook.trim
            .split(colonDelimiter)
            .map(expandVersesInterval)
        val verses = chaptersSplit(1).split(commaDelimiter).map(_ |> toInt)
        (chaptersSplit(0) |> toInt, verses.toList)
    }

    private def expandVersesInterval(afterChapter: String) =
        if(afterChapter.indexOf(versesIntervalDelimiter) > -1)
            versesIntervalsRegex
                .replaceAllIn(afterChapter,
                    x => (x |> toSequence).mkString(","))
        else
            afterChapter

    private def splitIntoChapters(book: String)(afterBook: String) = {
        val chapters = chapterIntervalExpander.expand(book,afterBook)
            .split(semicolonDelimiter)
            .map(mapAfterBook)
            .foldLeft(SortedMap.empty[Int, List[Int]])((acc, x) =>
                acc + (x._1 -> (acc.getOrElse(x._1, List()) ++ x._2))
            )
        chapters |> toUnion
    }

    private def splitIntoBook(ref: String): (String, String) = {
        val book = booksRegex.findFirstIn(ref)
            .getOrElse("")
            .split(" ")
            .map(_.toLowerCase.capitalize)
            .mkString(" ")
        val afterBook = booksRegex.replaceAllIn(ref, "")
        (book, if (afterBook.length > 1) afterBook else "1")
    }

    private def normalize(ref: String): String = {
        val cleaned = ref.trim
            .replaceAll("\\s+", " ")
        val result:String = jnormalize(cleaned, Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}\\p{IsM}\\p{IsLm}\\p{IsSk}]+", "")
        result
    }
}
