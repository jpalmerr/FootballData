package footballdata.client

import cats.effect.{ConcurrentEffect, Resource, Sync}
import cats.implicits._
import footballdata.models.{ServiceError, StatusResponse, TeamTransferResponse}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.syntax.all._
import org.http4s._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait FootballClient[F[_]] {
  def getApiStatus: F[StatusResponse]
  def getTeamTransfers(teamId: Int): F[TeamTransferResponse]
}

/**
  *
  * @param client
  *
  * client is https://www.api-football.com/
  * sub the api key into headers to make requests
  * never commit the api key
  */

final class HttpFootballClient[F[_]: Sync](client: Client[F]) extends FootballClient[F] {

  val uri: Uri = uri"https://v2.api-football.com"
  private val hostHeader = Header("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
  private val keyHeader = Header("x-rapidapi-key", "")

  override def getApiStatus: F[StatusResponse] = {
    val footballStatusUrl: Uri =  uri.withPath("/status")

    val req =
      Request[F](
        method = Method.GET,
        uri = footballStatusUrl
      )
        .withHeaders(
          Headers.of(hostHeader, keyHeader)
        )

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

  override def getTeamTransfers(teamId: Int): F[TeamTransferResponse] = {

    val req =
      Request[F](
        method = Method.GET,
        uri = uri / "transfers" /"team" / teamId.toString
      )
          .withHeaders(
            Headers.of(hostHeader, keyHeader)
          )

    client
        .fetch(req) { resp =>
          resp.status match {
            case Status.Ok =>
              resp.as[TeamTransferResponse]
            case Status.GatewayTimeout | Status.RequestTimeout =>
              ServiceError.FootballClientTimeout.raiseError[F, TeamTransferResponse]

            case _ =>
              ServiceError.FootballClientFailure.raiseError[F, TeamTransferResponse]
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

