package org.gospel.backend.processing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.gospel.backend.processing.Markup.getMarkup
import org.gospel.backend.processing.References.extractReferences
import org.gospel.backend.processing.dto.ReadingsDto
import org.gospel.backend.tools
import org.gospel.backend.tools.{Calendar, Calendars}
import org.gospel.backend.tools.Calendars.calendars
import scalaz.{\/, \/-}
import scalaz.std.`try`.toDisjunction

import scala.util.Try

object Readings {

    private val jsonMapper = new ObjectMapper().registerModule(DefaultScalaModule)

    def getReadings(message: String): Throwable \/ Seq[ReadingsDto] = {
        for {
            calendar <- getCalendar(message)
            markup <- getMarkup(calendar)
            reading <- extractReferences(markup)
            readings <- completeReadings(calendar, reading)
        } yield readings
    }

    def completeReadings(calendar: Calendar, readings: ReadingsDto) : Throwable \/ Seq[ReadingsDto] = {
        val completed = calendar.versions
            .map(x => readings.copy(version = x, bucketKey = completeBucketKey(calendar.bucketKey, x)))
        \/-(completed)
    }

    private def completeBucketKey(bucketKey: String, x: String) = {
        bucketKey.replace("/readings.json", s"/$x/readings.json")
    }

    private def getCalendar(message: String): Throwable \/ Calendar = toDisjunction(Try {
        jsonMapper.readValue(message, classOf[Calendar])
    })
}