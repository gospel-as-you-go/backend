package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.gospel.backend.lambdas.dto.Response
import org.gospel.backend.tools.Calendars.calendars
import org.gospel.backend.tools.Sns

class CalendarLinksHandler extends RequestHandler[Any, Response[Seq[String]]] {
    type MessageIds = Seq[String]
    override def handleRequest(input: Any, context: Context): Response[MessageIds] = {
        val result = Sns.sendAll(calendars)
        result.filter(_.isLeft).foreach(System.err.print)
        Response(result = result.filter(_.isRight).map(_.getOrElse("")), request = input)
    }
}
