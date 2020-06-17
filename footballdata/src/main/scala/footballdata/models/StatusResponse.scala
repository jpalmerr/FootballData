package footballdata.models

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, HCursor}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

final case class StatusResponse(api: Api)

object StatusResponse {
  implicit val decoder: Decoder[StatusResponse] = deriveDecoder
  implicit val encoder: Encoder[StatusResponse] = deriveEncoder

  implicit def statusResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, StatusResponse] = jsonOf[F, StatusResponse]
}

final case class Api(results: Int, status: Status)

object Api {
  implicit val decoder: Decoder[Api] = deriveDecoder
  implicit val encoder: Encoder[Api] = deriveEncoder
}


final case class Status(
                   user: String,
                   email: String,
                   plan: String,
                   token: String,
                   active: String,
                   subscriptionEnd: String,
                   requests: Int,
                   requestsLimitDay: Int,
                 )

object Status {
  implicit val decoder: Decoder[Status] = (c: HCursor) =>
  for {
      user    <- c.downField("user").as[String]
      email   <- c.downField("email").as[String]
      plan    <- c.downField("plan").as[String]
      token   <- c.downField("token").as[String]
      active  <- c.downField("active").as[String]
      sub     <- c.downField("subscription_end").as[String]
      reqs    <- c.downField("requests").as[Int]
      reqslim <- c.downField("requests_limit_day").as[Int]
      _       <- c.downField("payments").as[Unit]
  }  yield Status(
    user,
    email,
    plan,
    token,
    active,
    sub,
    reqs,
    reqslim,
  )

  implicit val encoder: Encoder[Status] = deriveEncoder
}
