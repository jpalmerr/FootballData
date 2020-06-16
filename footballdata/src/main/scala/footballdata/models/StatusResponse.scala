package footballdata.models

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class StatusResponse(api: Api)

object StatusResponse {
  implicit val decoder: Decoder[StatusResponse] = deriveDecoder
  implicit val encoder: Encoder[StatusResponse] = deriveEncoder

  implicit def statusResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, StatusResponse] = jsonOf[F, StatusResponse]
}

case class Api(results: Int, status: Status)

object Api {
  implicit val decoder: Decoder[Api] = deriveDecoder
  implicit val encoder: Encoder[Api] = deriveEncoder
}


case class Status(
                   user: String,
                   email: String,
                   plan: String,
                   token: String,
                   active: String,
                   subscription_end: String,
                   requests: Int,
                   requests_limit_day: Int,
                   payments: Seq[String]
                 )

object Status {
  implicit val decoder: Decoder[Status] = deriveDecoder
  implicit val encoder: Encoder[Status] = deriveEncoder
}
