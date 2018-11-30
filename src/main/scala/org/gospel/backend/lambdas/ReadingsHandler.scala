package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.processing.RawReadings.getReadings

import scala.collection.JavaConverters._

class ReadingsHandler extends RequestHandler[SNSEvent, Response[String]] {
    type Stdout = Any => Unit

    private val stdout: Stdout = println

    override def handleRequest(input: SNSEvent, context: Context): Response[String] =
        handle(stdout)(input, context)

    def handle(stdout: Stdout)
              (input: SNSEvent, context: Context): Response[String] = {
        val rawCalendars = input.getRecords.asScala
//            .map(x => getReadings(x.getSNS.getMessage))
        stdout(input.getRecords.get(0).getSNS.getMessage)

        Response(request = input, result = rawCalendars.toString())
    }
}
