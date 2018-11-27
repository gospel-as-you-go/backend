import org.gospel.backend.processing.Readings._
import org.gospel.backend.tools.Calendars
import org.gospel.backend.tools.functional._

import scala.util.Try

object App {

    val add: Int => Int => Try[Int] = x => y => Try(x + y)
    val divide: Int => Int => Try[Int] = x => y => Try(x / y)

    val addOne: Int => Try[Int] = add(1)
    val divideByZero: Int => Try[Int] = (x: Int) => divide(x)(0)

    def main(args: Array[String]): Unit = {
        val f1 = addOne >>> addOne >>> addOne >>> divideByZero >>> addOne
        val f2 = addOne >>> addOne >>> addOne >>> addOne

        val res = getReadings
//        val rd = getReadings(mk.get)
        println(Calendars.calendars)
        println(f2(1))
//        val f1 = addOne(1) >>= addOne >>= addOne
//        println(f1)
    }

}
