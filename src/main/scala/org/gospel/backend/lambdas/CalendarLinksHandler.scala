package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.tools.Calendars.calendars
import org.gospel.backend.tools.metric.time
import org.gospel.backend.tools.{Sns, metric}

class CalendarLinksHandler extends RequestHandler[Any, Response[Seq[String]]] {
    type MessageIds = Seq[String]
    override def handleRequest(input: Any, context: Context): Response[MessageIds] = {
        println(s"[CONTEXT] Remaining Time: ${context.getRemainingTimeInMillis} ms")
        val result = Sns.sendAll(Seq(calendars.head))
        time {result.filter(_.isLeft).foreach(System.err.print)} {"PRINT ERRORS"}
        time { Response(result = result.filter(_.isRight).map(_.getOrElse("")),
                        request = input) } {"GET RIGHT RESULTS"}
    }
}
