package footballdata.routes

import cats.effect.Sync
import cats.implicits._
import footballdata.models.StatusResponse
import footballdata.{MasterControlProgram, ServiceCodecs}
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object StatusRoute extends ServiceCodecs {

  def route[F[_] : Sync](mcp: MasterControlProgram[F]): HttpRoutes[F] = {

    implicit val entityEncoder: EntityEncoder[F, StatusResponse] = jsonEncoderOf[F, StatusResponse]

    HttpRoutes.of[F] {
      case GET -> Root / "status" =>
        toResponse(Status.Ok) {
          for {
            response <- mcp.getStatus
          } yield response
        }
    }

  }
}
