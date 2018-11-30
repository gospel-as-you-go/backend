import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.gospel.backend.processing.RawReadings._
import org.gospel.backend.processing.{AugmentedReadings, FunctionConfig, References, Texts}
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
        val message = " { \"firsts\": [ \"Romans 10:9-18\" ], \"psalms\": [ \"Psalm 18(19):2-5\" ], \"seconds\": [], \"gospels\": [ \"Matthew 4:18-22\" ], \"bucketKey\": \"gospel.scraper.storage/general/20181130/NRSVCE/readings.json\", \"version\": \"NRSVCE\" }"
        val res = FunctionConfig.augmentedReadings(message)
        println(res)
    }
}
