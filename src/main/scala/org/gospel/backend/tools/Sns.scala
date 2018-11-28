package org.gospel.backend.tools

import java.lang.System.getenv

import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSClientBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.gospel.backend.tools.functional._
import org.gospel.backend.tools.metric.time
import scalaz.\/
import scalaz.std.`try`.toDisjunction

import scala.util.Try

object Sns {

    implicit private def snsClient: AmazonSNS = AmazonSNSClientBuilder.standard().build()
    private val jsonMapper = new ObjectMapper().registerModule(DefaultScalaModule)

    def send[T](obj: T)(implicit topicArn: String = getenv("PROPAGATION_TOPIC"),
                        snsClient: AmazonSNS = snsClient) : Throwable \/ String = {

        val json = jsonMapper.writeValueAsString(obj)
        val request = new PublishRequest(topicArn, json)
        val publish = { snsClient.publish(request) }
        Try(publish).map(x => x.getMessageId) |> toDisjunction
    }

    def sendAll[T](seq: Seq[T])(implicit topicArn: String = getenv("PROPAGATION_TOPIC"),
                                         snsClient: AmazonSNS = snsClient) : Seq[Throwable \/ String] = time {
        seq.map(send)
    } {"PUBLISH_ALL"}
}
