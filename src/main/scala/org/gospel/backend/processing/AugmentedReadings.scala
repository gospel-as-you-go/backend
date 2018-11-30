package org.gospel.backend.processing

import org.gospel.backend.processing.dto.ReadingsDto
import scalaz.std.`try`.toDisjunction
import org.gospel.backend.tools.functional._
import scalaz.{\/, \/-}

import scala.util.Try

object AugmentedReadings {
    type ReadingsUnion = Throwable \/ ReadingsDto
    type ToObject = String => ReadingsDto
    type RefToText = (String, String) => Throwable \/ String

    def augmentReadings(toObject: ToObject, refToText: RefToText)
                       (message: String): ReadingsUnion = {
        for {
            readings <- Try{toObject(message)} |> toDisjunction
            readings <- augment(refToText, readings)
        } yield readings
    }

    private def augment(refToText: RefToText, readings: ReadingsDto): ReadingsUnion = {
        val refsToTexts: Seq[String] => Seq[String] = mapRefsToTexts(refToText)
        val result = readings.copy(firsts = refsToTexts(readings.firsts),
                      seconds = refsToTexts(readings.seconds),
                      psalms = refsToTexts(readings.psalms),
                      gospels = refsToTexts(readings.gospels))
        result |> \/-.apply
    }

    private def mapRefsToTexts(refToText: RefToText)(readings: Seq[String]) =
        readings
            .map(x => refToText(x, ""))
            .filter(_.isRight)
            .map(_.getOrElse("").trim)
            .filter(_.length > 0)
}
