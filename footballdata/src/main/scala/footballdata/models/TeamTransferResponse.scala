package footballdata.models

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

final case class TeamTransferResponse(api: TransferApi)

object TeamTransferResponse {
  implicit val decoder: Decoder[TeamTransferResponse] = ???
  implicit val encoder: Encoder[TeamTransferResponse] = ???

  implicit def statusResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, TeamTransferResponse] = jsonOf[F, TeamTransferResponse]
}

final case class TransferApi(results: Int, transfers: List[TransferData])

object TransferApi{
  implicit val decoder: Decoder[TeamTransferResponse] = ???
  implicit val encoder: Encoder[TeamTransferResponse] = ???
}

final case class TransferData(
                               playerId: Int,
                               playerName: String,
                               transferData: String,
                               transferType: String,
                               teamIn: Team,
                               teamOut: Team,
                               lastUpdate: Int
                             )

object TransferData {
  implicit val decoder: Decoder[TeamTransferResponse] = ???
  implicit val encoder: Encoder[TeamTransferResponse] = ???
}

final case class Team(teamId: Int, teamName: String)

object Team {
  implicit val decoder: Decoder[TeamTransferResponse] = ???
  implicit val encoder: Encoder[TeamTransferResponse] = ???
}
