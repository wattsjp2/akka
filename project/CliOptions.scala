/**
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka

case class CliOption[T](private val value: T) {
  def get: T = value
}

object CliOption {
  def apply[T](path: String, default: T)(implicit ev: CliOptionParser[T]): CliOption[T] = ev.parse(path, default)

  implicit class BooleanCliOption(cliOption: CliOption[Boolean]) {
    def ifTrue[A](a: => A): Option[A] = if (cliOption.get) Some(a) else None
  }

  trait CliOptionParser[T] {
    def parse(path: String, default: T): CliOption[T]
  }

  object CliOptionParser {
    implicit object BooleanCliOptionParser extends CliOptionParser[Boolean] {
      def parse(path: String, default: Boolean) =
        CliOption(System.getProperty(path, default.toString).toBoolean)
    }
  }
}
