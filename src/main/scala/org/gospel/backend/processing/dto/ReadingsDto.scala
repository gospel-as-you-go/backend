package org.gospel.backend.processing.dto

import scala.beans.BeanProperty


case class ReadingsDto(@BeanProperty var firsts: Seq[String] = Seq.empty,
                       @BeanProperty var psalms: Seq[String]  = Seq.empty,
                       @BeanProperty var seconds: Seq[String] = Seq.empty,
                       @BeanProperty var gospels: Seq[String] = Seq.empty,
                       @BeanProperty var bucketKey: String = "",
                       @BeanProperty var version: String = "") {
    def this() = this(firsts = Seq.empty)
}


