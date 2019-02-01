package org.gospel.backend.references

class ChapterIntervalExpander {
    private val chapterRegex = "(\\d+)\\s*:".r
    private val chaptersIntervalDelimiter = "--"
    private val rightVerseRegex = ":\\s*(\\d+)"
    private val leftVerseRegex = "(\\d+)$"
    private val semicolonDelimiter = ";"

    def expand(book: String, afterBook: String): String = {
        if(afterBook.indexOf(chaptersIntervalDelimiter) == -1) {
            return afterBook
        }
        val findVerse: Int => Option[Int] = Books.findLastVerseFor(book)
        afterBook.split(semicolonDelimiter)
            .map(_.trim)
            .map(expandIntervals(findVerse))
            .mkString(semicolonDelimiter)
    }

    private def expandIntervals(findVerse: Int => Option[Int])(subRef: String): String = {
        if(subRef.indexOf(chaptersIntervalDelimiter) == -1) {
            return subRef
        }
        subRef.split(chaptersIntervalDelimiter)
            .map(_.trim)
            .reduceLeft(breakChapterIntervals(findVerse))
    }

    private def breakChapterIntervals(findVerse: Int => Option[Int])(left: String, right: String): String = {
        val leftChapter = chapterRegex.findAllMatchIn(left).map(_.group(1)).toList.reverse.head.trim.toInt
        val rightChapter = chapterRegex.findFirstMatchIn(right).map(_.group(1)).getOrElse("1").trim.toInt
        val leftVerse = leftVerseRegex.r.findFirstMatchIn(left).map(_.group(1)).getOrElse("1").trim.toInt
        val lastLeftVerse = findVerse(leftChapter).getOrElse(leftVerse)
        val headPart = left.replaceAll(leftVerseRegex, s"$$1-$lastLeftVerse")
        val tailPart = right.replaceAll(rightVerseRegex, ":1-$1")

        if(rightChapter - leftChapter <= 1) {
            return s"$headPart;$tailPart"
        }

        val middlePart = (leftChapter + 1 until rightChapter)
            .map(x => s"$x:1-${findVerse(x).getOrElse(1)}")
            .mkString(";")

        s"$headPart;$middlePart;$tailPart"
    }
}
