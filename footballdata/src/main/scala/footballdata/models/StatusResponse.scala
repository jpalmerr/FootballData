package footballdata.models

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder

case class StatusResponse(results: Int, status: Status)

object StatusResponse {
  implicit val decoder: Decoder[StatusResponse] = deriveDecoder
  implicit val encoder: Encoder[StatusResponse] = deriveEncoder

  implicit def statusResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, StatusResponse] = jsonOf[F, StatusResponse]
}

case class Status(
                   user: String,
                   email: String,
                   plan: String,
                   token: String,
                   active: String,
                   subscriptionEnd: String,
                   requests: Int,
                   requestLimitDaily: Int
                 )

object Status {
  implicit val decoder: Decoder[Status] = deriveDecoder
  implicit val encoder: Encoder[Status] = deriveEncoder
}
