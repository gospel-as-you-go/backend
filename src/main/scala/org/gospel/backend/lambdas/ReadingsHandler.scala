package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.processing.Readings.getReadings

import scala.collection.JavaConverters._

class ReadingsHandler extends RequestHandler[SNSEvent, Response[String]] {
    private val jsonMapper = new ObjectMapper().registerModule(DefaultScalaModule)

    override def handleRequest(input: SNSEvent, context: Context): Response[String] = {
        val rawReadings = input.getRecords.asScala
            .map(x => getReadings(x.getSNS.getMessage))
        println(input.getRecords.get(0).getSNS.getMessage)
        rawReadings.filter(_.isLeft).foreach(System.err.print)
        val readings = rawReadings.filter(_.isRight).flatMap(_.getOrElse(Seq.empty))
        println(readings)
        Response(request = input, result = readings.toString())
    }
}
