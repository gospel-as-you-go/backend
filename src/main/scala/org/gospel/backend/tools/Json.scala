package org.gospel.backend.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Json {
    private val jsonMapper = new ObjectMapper().registerModule(DefaultScalaModule)
    def toJson[A](obj: A): String = jsonMapper.writeValueAsString(obj)
    def toObject[A](kind: Class[A])(json: String): A = jsonMapper.readValue(json, kind)
}
