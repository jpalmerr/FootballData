package footballdata

import cats.implicits._
import cats.effect.Sync
import footballdata.models.ServiceError
import footballdata.models.ServiceError._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import org.http4s.circe.jsonEncoderOf
import org.http4s.headers.`Content-Type`
import org.http4s.{Charset, EntityEncoder, MediaType, Response, Status}

trait ResponseCodecs {

  implicit def throwableErrorJsonEncoder: Encoder[Throwable]

  implicit def throwableEntityEncoder[F[_]: Sync]: EntityEncoder[F, Throwable] =
    jsonEncoderOf[F, Throwable]
      .withContentType(`Content-Type`(MediaType.application.json, Charset.`UTF-8`))

  def errResponse[F[_]: Sync](err: Throwable): F[Response[F]] = err match {
    case FootballClientFailure | FootballClientTimeout  => Response[F](Status.NotFound).withEntity(err).pure[F]
    case _                                              => Response[F](Status.InternalServerError).withEntity(err).pure[F]
  }

  def toResponse[F[_]: Sync, A: EntityEncoder[F, *]](status: Status)(response: F[A]): F[Response[F]] =
    response.attempt.flatMap {
      case Left(e)   => errResponse(e)
      case Right(er) => Response[F](status).withEntity(er).pure[F]
    }
}

trait ServiceCodecs extends ResponseCodecs {
  override implicit val throwableErrorJsonEncoder: Encoder[Throwable] = Encoder.instance {
    case se: ServiceError => Json.obj("responseCode" -> se.error.asJson, "responseMessage" -> se.errorDescription.asJson)
    case t: Throwable     => Json.obj("responseCode" -> t.getMessage.asJson)
  }
}
