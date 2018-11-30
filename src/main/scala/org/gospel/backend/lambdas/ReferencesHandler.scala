package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.processing.FunctionConfig
import org.gospel.backend.processing.RawReadings.getReadings
import scalaz.\/

import scala.collection.JavaConverters._

class ReferencesHandler extends RequestHandler[SNSEvent, Response[String]] {
    type SendMessages = Seq[Any] => Seq[Throwable \/ String]
    type Stdout = Any => Unit

    private val sendMessages = FunctionConfig.sendRefsMessages
    private val stdout: Stdout = println

    override def handleRequest(input: SNSEvent, context: Context): Response[String] =
        handle(sendMessages, stdout)(input, context)

    def handle(sendMessages: SendMessages, stdout: Stdout)
              (input: SNSEvent, context: Context): Response[String] = {
        val references = input.getRecords.asScala
            .map(x => getReadings(x.getSNS.getMessage))
        stdout(input.getRecords.get(0).getSNS.getMessage)
        references.filter(_.isLeft).foreach(System.err.print)
        val readings = references
            .filter(_.isRight)
            .flatMap(_.getOrElse(Seq.empty))
        stdout(references)
        sendMessages(references)
        Response(request = input, result = readings.toString())
    }
}
