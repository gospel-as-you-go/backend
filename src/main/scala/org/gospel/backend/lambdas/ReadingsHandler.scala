package org.gospel.backend.lambdas

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import org.gospel.backend.lambdas.dto.Response

class ReadingsHandler extends RequestHandler[SNSEvent, Response[String]] {
    override def handleRequest(input: SNSEvent, context: Context): Response[String] = {
        println(input.getRecords)
        Response(request = input, result = "Done")
    }
}
