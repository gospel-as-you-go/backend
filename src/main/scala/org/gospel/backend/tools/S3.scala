package org.gospel.backend.tools

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectResult
import org.gospel.backend.tools.functional._
import scalaz.\/
import scalaz.std.`try`.toDisjunction

import scala.io.Source
import scala.util.Try

object S3 {
    def doesS3ObjectExist(bucketName: String, amazonS3: AmazonS3)
                         (key: String): Throwable \/ Boolean = Try {
        amazonS3.doesObjectExist(bucketName, key)
    } |> toDisjunction

    def writeToS3[A](bucketName: String, amazonS3: AmazonS3, toJson: A => String)
                    (key: String, obj: A): Throwable \/ PutObjectResult = Try {
        amazonS3.putObject(bucketName, key, toJson(obj))
    } |> toDisjunction

    def readS3Object(bucketName: String, amazonS3: AmazonS3)
                    (key: String): Throwable \/ String = Try {
        amazonS3.getObject(bucketName, key)
    }
        .map(_.getObjectContent)
        .map(Source.fromInputStream(_).mkString("\n")) |> toDisjunction
}
