package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.processing.FunctionConfig
import org.gospel.backend.tools.Calendars.calendars
import org.gospel.backend.tools.metric.time
import scalaz.\/

class CalendarLinksHandler extends RequestHandler[Any, Response[Seq[String]]] {
    type MessageIds = Seq[String]
    type SendMessages = Seq[Any] => Seq[Throwable \/ String]

    private val sendMessages = FunctionConfig.sendCalendarMessages

    override def handleRequest(input: Any, context: Context): Response[MessageIds] =
        handle(sendMessages)(input, context)

    def handle(sendMessages: SendMessages)
              (input: Any, context: Context): Response[MessageIds] = {
        println(s"[CONTEXT] Remaining Time: ${context.getRemainingTimeInMillis} ms")
        val result = sendMessages(Seq(calendars.head))
        time {result.filter(_.isLeft).foreach(System.err.print)} {"PRINT ERRORS"}
        time { Response(result = result.filter(_.isRight).map(_.getOrElse("")),
            request = input) } {"GET RIGHT RESULTS"}
    }
}
