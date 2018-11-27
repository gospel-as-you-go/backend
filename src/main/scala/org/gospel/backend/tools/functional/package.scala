package org.gospel.backend.tools

import scala.util.{Failure, Success, Try}

package object functional {

    implicit class Composition[A, +B](g: A => B) {
        def >> [C] (f: B => C): A => C = (x: A) => f(g(x))
    }

    implicit class RailwaysComposition[A, +B](g: A => Try[B]) {
        def >>> [C] (f: B => Try[C]): A => Try[C] = (x: A) => {
            g(x) match {
                case Success(y) => f(y)
                case Failure(e) => Failure(e)
            }
        }
    }

    implicit class Pipe[A](a: A) {
        def |> [B](f: A => B): B = f(a)
    }
}
