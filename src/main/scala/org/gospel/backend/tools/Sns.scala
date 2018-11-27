package org.gospel.backend.tools

import java.lang.System.getenv

import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSClientBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import org.gospel.backend.tools.functional._
import scalaz.\/
import scalaz.std.`try`.toDisjunction

import scala.util.Try

object Sns {

    implicit private def snsClient: AmazonSNS = AmazonSNSClientBuilder.standard().build()

    def send[T](obj: T)(implicit topicArn: String = getenv("PROPAGATION_TOPIC"),
                                 snsClient: AmazonSNS = snsClient) : Throwable \/ String = {
        val json = new ObjectMapper().writeValueAsString(obj)
        val request = new PublishRequest(topicArn, json)
        Try(snsClient.publish(request)).map(x => x.getMessageId) |> toDisjunction
    }

    def sendAll[T](seq: Seq[T])(implicit topicArn: String = getenv("PROPAGATION_TOPIC"),
                                         snsClient: AmazonSNS = snsClient) : Seq[Throwable \/ String] = seq.map(send)
}
