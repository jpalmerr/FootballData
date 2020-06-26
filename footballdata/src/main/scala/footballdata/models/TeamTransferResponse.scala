package footballdata.models

import cats.effect.Sync
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, HCursor}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

final case class TeamTransferResponse(api: TransferApi)

object TeamTransferResponse {
  implicit val decoder: Decoder[TeamTransferResponse] = (c: HCursor) =>
    for {
      api <- c.downField("api").as[TransferApi]
    } yield TeamTransferResponse(api)
  implicit val encoder: Encoder[TeamTransferResponse] = deriveEncoder

  implicit def statusResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, TeamTransferResponse] = jsonOf[F, TeamTransferResponse]
}

final case class TransferApi(results: Int, transfers: Seq[TransferData])

object TransferApi{

    implicit val decoder: Decoder[TransferApi] = (c: HCursor) =>
    for {
      results <- c.downField("results").as[Int]
      transfers <- c.downField("transfers").as[Seq[TransferData]]
    } yield TransferApi(results, transfers)

  implicit val encoder: Encoder[TransferApi] = deriveEncoder
}


final case class TransferData(
                               playerId: Int,
                               playerName: String,
                               transferDate: String,
                               transferType: Option[String],
                               teamIn: Team,
                               teamOut: Team,
                             )

object TransferData {

  implicit val decoder: Decoder[TransferData] = (c: HCursor) =>
    for {
      playerId     <- c.downField("player_id").as[Int]
      playerName   <- c.downField("player_name").as[String]
      transferDate <- c.downField("transfer_date").as[String]
      transferType <- c.downField("type").as[Option[String]]
      teamIn       <- c.downField("team_in").as[Team]
      teamOut      <- c.downField("team_out").as[Team]
      _   <- c.downField("lastUpdate").as[Int]
    } yield TransferData(
      playerId,
      playerName,
      transferDate,
      transferType,
      teamIn,
      teamOut
    )

  implicit val encoder: Encoder[TransferData] = deriveEncoder
}

final case class Team(teamId: Option[Int], teamName: Option[String])

object Team {
  implicit val decoder: Decoder[Team] = (c: HCursor) =>
    for {
      teamId   <- c.downField("team_id").as[Option[Int]]
      teamName <- c.downField("team_name").as[Option[String]]
  } yield Team(teamId, teamName)

  implicit val encoder: Encoder[Team] = deriveEncoder
}
