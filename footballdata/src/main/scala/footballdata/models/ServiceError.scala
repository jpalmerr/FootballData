package footballdata.models

import scala.util.control.NoStackTrace

sealed abstract class ServiceError extends Throwable with NoStackTrace {
  def error: String
  def errorDescription: String

  override def toString: String = error
}

object ServiceError {

  final case object FootballClientFailure extends ServiceError {
    override def error: String = "failure"

    override def errorDescription: String = "football client failing"
  }

  final case object FootballClientTimeout extends ServiceError {
    override def error: String = "timeout"

    override def errorDescription: String = "football client timeout"
  }

}
