package footballdata.routes

import cats.effect.Sync
import footballdata.models.{StatusResponse, TeamTransferResponse}
import footballdata.{MasterControlProgram, ServiceCodecs}
import org.http4s.HttpRoutes
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

object TeamTransferRoute extends ServiceCodecs {

  def route[F[_]: Sync](mcp: MasterControlProgram[F]): HttpRoutes[F] = {

    implicit val entityEncoder: EntityEncoder[F, TeamTransferResponse] = jsonEncoderOf[F, TeamTransferResponse]

    HttpRoutes.of[F] {
      case GET -> Root / "teamTransfers" / IntVar(teamId) =>
        toResponse(Status.Ok) {
          mcp.getTransfersByTeam(teamId)
        }
    }
  }
}
