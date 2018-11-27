package org.gospel.backend.processing

import org.gospel.backend.processing.Markup.getMarkup
import org.gospel.backend.processing.References.extractReferences
import org.gospel.backend.processing.dto.ReadingsDto
import org.gospel.backend.tools.{Calendar, Calendars}
import org.gospel.backend.tools.Calendars.calendars
import scalaz.\/

object Readings {

    def getReadings: Seq[Throwable \/ ReadingsDto] = {
        val result = calendars.toList.map(mapToReadings)
        result
    }

    private def mapToReadings(calendar: Calendar) = {
        for {
            markup <- getMarkup(calendar)
            reference <- extractReferences(markup)
        } yield reference
    }
}
