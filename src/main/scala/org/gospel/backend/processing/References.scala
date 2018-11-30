package org.gospel.backend.processing

import org.gospel.backend.processing.dto.ReadingsDto
import scalaz.{\/, \/-}

object References {
    private val psalmPrefix = "Psalm"
    private val gospelRegex = "^(?:Luke|Marc|John|Matthew) .*$"

    def extractReferences(refs: Seq[String]): Throwable \/ ReadingsDto = {
        val firstSplit = refs.span(x => !x.startsWith(psalmPrefix))
        val firsts = firstSplit._1
        val psalmSplit = firstSplit._2.span(x => x.startsWith(psalmPrefix))
        val psalms = psalmSplit._1.map(extractPsalms)
        val secondSplit = psalmSplit._2.span(x => !x.matches(gospelRegex))
        \/-(ReadingsDto(firsts, psalms, secondSplit._1, secondSplit._2))
    }

    private def extractPsalms(ref: String) =
        ref.replaceAll("(?:P|p)salm\\s+\\d+\\((\\d+)\\)", "Psalm $1")

}
