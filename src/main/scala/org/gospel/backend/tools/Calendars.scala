package org.gospel.backend.tools


import java.lang.System.getenv

import com.fasterxml.jackson.databind.ObjectMapper
import org.gospel.backend.tools.Versions.versions
import org.joda.time.LocalDate

import scala.beans.BeanProperty

case class Calendar(@BeanProperty var name: String,
                    @BeanProperty var link: String,
                    @BeanProperty var bucketKey: String = getenv("STORAGE_BUCKET"),
                    @BeanProperty var versions: Seq[String] = versions) {
    def this() = this("", "")
}

object Calendars {
    private val site = "https://universalis.com"
    val days: Seq[String] = getDays
    val calendars: Seq[Calendar] = Seq(
        Calendar(link = "general", name = "General"),
        Calendar(link = "americas", name = "Latin America"),
        Calendar(link = "asia", name = "Asia"),
        Calendar(link = "asia.india", name = "Asia - India"),
        Calendar(link = "asia.singapore", name = "Asia - Singapore"),
        Calendar(link = "australia", name = "Australia"),
        Calendar(link = "australia.ordinariate", name = "Australia - Ordinariate"),
        Calendar(link = "canada", name = "Canada"),
        Calendar(link = "east", name = "Eastern General"),
        Calendar(link = "europe", name = "Europe"),
        Calendar(link = "europe.england", name = "Europe - England"),
        Calendar(link = "europe.france", name = "Europe - France"),
        Calendar(link = "europe.ireland", name = "Europe - Ireland"),
        Calendar(link = "europe.italy", name = "Europe - Italy"),
        Calendar(link = "europe.malta", name = "Europe - Malta"),
        Calendar(link = "europe.scotland", name = "Europe - Scotland"),
        Calendar(link = "europe.sweden", name = "Europe - Sweden"),
        Calendar(link = "europe.wales", name = "Europe - Wales"),
        Calendar(link = "meast", name = "Middle East"),
        Calendar(link = "meast.sarabia", name = "Middle East - Southern Arabia"),
        Calendar(link = "nigeria", name = "Nigeria"),
        Calendar(link = "nz", name = "New Zealand"),
        Calendar(link = "philippines", name = "Philippines"),
        Calendar(link = "safrica", name = "Southern Africa"),
        Calendar(link = "usa", name = "United States")
    ).flatMap(x => days.map(y => x
        .copy(bucketKey = s"${x.bucketKey}/${x.link}/$y/readings.json")
        .copy(link = s"$site/${x.link}/$y/mass.htm".replace("general/", ""))
    ))

    def getDays: Seq[String] = {
        val now = LocalDate.now()
        for (day <- 0 to 9) yield now.plusDays(day).toString("YYYYMMdd")
    }
}
