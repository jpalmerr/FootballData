package footballdata.client

import cats.effect.{ConcurrentEffect, Resource, Sync}
import cats.implicits._
import footballdata.models.{ServiceError, StatusResponse}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.syntax.all._
import org.http4s._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, _}

trait FootballClient[F[_]] {
  def getApiStatus: F[StatusResponse]
}

final class HttpFootballClient[F[_]: Sync](client: Client[F]) extends FootballClient[F] {

  override def getApiStatus: F[StatusResponse] = {
    val footballStatusUrl: Uri =  uri"https://v2.api-football.com/status"

    val req =
      Request[F](
        method = Method.GET,
        uri = footballStatusUrl
      )
        .withHeaders(Headers.of(Header("X-RapidAPI-Key", "TODOKEY"))) // TODO: add api keys

    client
      .fetch(req) { resp =>
        resp.status match {
          case Status.Ok =>
            resp.as[StatusResponse]

          case Status.GatewayTimeout | Status.RequestTimeout =>
            ServiceError.FootballClientTimeout.raiseError[F, StatusResponse]

          case _ =>
            ServiceError.FootballClientFailure.raiseError[F, StatusResponse]
        }
      }
  }
}

object HttpFootballClient {
  def apply[F[_]: ConcurrentEffect](ec: ExecutionContext): Resource[F, HttpFootballClient[F]] = {
    val timeout: Duration = 30.seconds
    BlazeClientBuilder[F](ec)
        .withRequestTimeout(timeout)
        .resource
        .map { client =>
          new HttpFootballClient(client)
        }
  }
}

