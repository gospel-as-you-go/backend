package org.gospel.backend.tools

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import org.gospel.backend.tools.functional._
import org.gospel.backend.tools.metric.time
import scalaz.\/
import scalaz.std.`try`.toDisjunction

import scala.util.Try

object Sns {

    def send[T](snsClient: AmazonSNS)
               (topicArn: String)
               (obj: T) : Throwable \/ String = {

        val json = Json.toJson(obj)
        val request = new PublishRequest(topicArn, json)
        val publish = { snsClient.publish(request) }
        Try(publish).map(x => x.getMessageId) |> toDisjunction
    }

    def sendAll[T](snsClient: AmazonSNS)
                  (topicArn: String)
                  (seq: Seq[T]) : Seq[Throwable \/ String] =
        time {seq.map(send(snsClient)(topicArn))} {"PUBLISH_ALL"}
}
