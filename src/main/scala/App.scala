import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.gospel.backend.processing.Readings._
import org.gospel.backend.tools.Calendars
import org.gospel.backend.tools.functional._

import scala.io.Source
import scala.util.Try

object App {

    val add: Int => Int => Try[Int] = x => y => Try(x + y)
    val divide: Int => Int => Try[Int] = x => y => Try(x / y)

    val addOne: Int => Try[Int] = add(1)
    val divideByZero: Int => Try[Int] = (x: Int) => divide(x)(0)

    def main(args: Array[String]): Unit = {
//        val f1 = addOne >>> addOne >>> addOne >>> divideByZero >>> addOne
//        val f2 = addOne >>> addOne >>> addOne >>> addOne

        val message = "{\"name\":\"General\",\"link\":\"https://universalis.com/20181128/mass.htm\",\"bucketKey\":\"gospel.scraper.storage/general/20181128/readings.json\",\"versions\":[\"NRSVCE\",\"NRSV\",\"SG21\",\"DHH\",\"ERV-AR\",\"CEI\",\"OL\",\"RUSV\"]}"
        val res = getReadings(message)
        println(res)
    }
}
