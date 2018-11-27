package org.gospel.backend.tools


import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.LocalDate

import scala.beans.BeanProperty

case class Calendar(@BeanProperty name: String, @BeanProperty link: String) {
    def toJson: String = new ObjectMapper().writeValueAsString(this)
}

object Calendars {
    val days: Seq[String] = getDays
    val calendars: Seq[Calendar] = Seq(
        Calendar(link = "https://universalis.com/", name = "General"),
        Calendar(link = "https://universalis.com/americas/", name = "Latin America"),
        Calendar(link = "https://universalis.com/asia/", name = "Asia"),
        Calendar(link = "https://universalis.com/asia.india/", name = "Asia - India"),
        Calendar(link = "https://universalis.com/asia.singapore/", name = "Asia - Singapore"),
        Calendar(link = "https://universalis.com/australia/", name = "Australia"),
        Calendar(link = "https://universalis.com/australia.ordinariate/", name = "Australia - Ordinariate"),
        Calendar(link = "https://universalis.com/canada/", name = "Canada"),
        Calendar(link = "https://universalis.com/east/", name = "Eastern General"),
        Calendar(link = "https://universalis.com/europe/", name = "Europe"),
        Calendar(link = "https://universalis.com/europe.england/", name = "Europe - England"),
        Calendar(link = "https://universalis.com/europe.france/", name = "Europe - France"),
        Calendar(link = "https://universalis.com/europe.ireland/", name = "Europe - Ireland"),
        Calendar(link = "https://universalis.com/europe.italy/", name = "Europe - Italy"),
        Calendar(link = "https://universalis.com/europe.malta/", name = "Europe - Malta"),
        Calendar(link = "https://universalis.com/europe.netherlands/", name = "Europe - Netherlands"),
        Calendar(link = "https://universalis.com/europe.poland/", name = "Europe - Poland"),
        Calendar(link = "https://universalis.com/europe.scotland/", name = "Europe - Scotland"),
        Calendar(link = "https://universalis.com/europe.slovakia/", name = "Europe - Slovakia"),
        Calendar(link = "https://universalis.com/europe.sweden/", name = "Europe - Sweden"),
        Calendar(link = "https://universalis.com/europe.wales/", name = "Europe - Wales"),
        Calendar(link = "https://universalis.com/meast/", name = "Middle East"),
        Calendar(link = "https://universalis.com/meast.sarabia/", name = "Middle East - Southern Arabia"),
        Calendar(link = "https://universalis.com/nigeria/", name = "Nigeria"),
        Calendar(link = "https://universalis.com/nz/", name = "New Zealand"),
        Calendar(link = "https://universalis.com/philippines/", name = "Philippines"),
        Calendar(link = "https://universalis.com/safrica/", name = "Southern Africa"),
        Calendar(link = "https://universalis.com/usa/", name = "United States")
    ).flatMap(x => days.map(y => x.copy(link = s"${x.link}$y/mass.htm") ))

    def getDays: Seq[String] = {
        val now = LocalDate.now()
        for (day <- 0 to 9) yield now.plusDays(day).toString("YYYYMMdd")
    }
}
