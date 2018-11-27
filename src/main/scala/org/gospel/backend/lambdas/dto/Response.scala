package org.gospel.backend.lambdas.dto

import scala.beans.BeanProperty

case class Response[+T](@BeanProperty result: T, @BeanProperty request: Any)
