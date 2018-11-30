package org.gospel.backend.processing

import java.lang.System.getenv

import com.amazonaws.services.s3.model.PutObjectResult
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSClientBuilder}
import org.gospel.backend.processing.dto.ReadingsDto
import org.gospel.backend.tools.{Json, S3, Sns}
import scalaz.\/

object FunctionConfig {
    type SendMessages = Seq[Any] => Seq[Throwable \/ String]

    private def snsClient: AmazonSNS = AmazonSNSClientBuilder.standard().build()
    private def s3Client: AmazonS3 = AmazonS3ClientBuilder.defaultClient

    val referencesTopicName: String = getenv("REFERENCES_TOPIC")
    val calendarTopicName: String = getenv("CALENDAR_TOPIC")
    val storageBucketName: String = getenv("STORAGE_BUCKET")

    val sendSnsMessages: String => SendMessages =
        Sns.sendAll(snsClient)
    val sendSnsMessage: String => Any => Throwable \/ String =
        Sns.send(snsClient)
    val sendRefsMessages: SendMessages =
        sendSnsMessages(referencesTopicName)
    val sendCalendarMessages: SendMessages =
        sendSnsMessages(calendarTopicName)

    val doesS3ObjectExist: String => Throwable \/ Boolean =
        S3.doesS3ObjectExist(storageBucketName, s3Client)
    val writeToS3: (String, Any) => Throwable \/ PutObjectResult =
        S3.writeToS3(storageBucketName, s3Client, Json.toJson)
    val readS3Object: String => Throwable \/ String =
        S3.readS3Object(storageBucketName, s3Client)

    val augmentedReadings: String => Throwable \/ ReadingsDto =
        AugmentedReadings.augmentReadings(
            Json.toObject(classOf[ReadingsDto]),
            Texts.findTextByRef
        )
}
