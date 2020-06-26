package footballdata.models

import cats.effect.Sync
import io.circe.generic.semiauto.{deriveDecoder,  deriveEncoder}
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
  implicit val decoder: Decoder[TransferApi] = deriveDecoder

  //  implicit val decoder: Decoder[TransferApi] = (c: HCursor) =>
//    for {
//      results <- c.downField("results").as[Int]
//      transfers <- c.downField("transfers").as[Seq[TransferData]]
//    } yield TransferApi(results, transfers)

  implicit val encoder: Encoder[TransferApi] = deriveEncoder
}


final case class TransferData(
                               player_id: Int,
                               player_name: String,
                               transfer_date: String,
                               `type`: Option[String],
                               team_in: Team,
                               team_out: Team,
                               lastUpdate: Int
                             )

/*
"player_id": 138827,
                "player_name": "B. Duncan",
                "transfer_date": "2019-09-02",
                "type": "€ 1.8M",
                "team_in": {
                    "team_id": 502,
                    "team_name": "Fiorentina"
                },
                "team_out": {
                    "team_id": 40,
                    "team_name": "Liverpool"
                },
                "lastUpdate": 1577932180
 */

object TransferData {
  implicit val decoder: Decoder[TransferData] = deriveDecoder

//  implicit val decoder: Decoder[TransferData] = (c: HCursor) =>
//    for {
//      playerId     <- c.downField("player_id").as[Int]
//      playerName   <- c.downField("player_name").as[String]
//      transferData <- c.downField("transfer_date").as[String]
//      transferType <- c.downField("type").as[String]
//      teamIn       <- c.downField("team_in").as[Team]
//      teamOut      <- c.downField("team_out").as[Team]
//      lastUpdate   <- c.downField("lastUpdate").as[Int]
//    } yield TransferData(
//      playerId,
//      playerName,
//      transferData,
//      transferType,
//      teamIn,
//      teamOut,
//      lastUpdate
//    )

  implicit val encoder: Encoder[TransferData] = deriveEncoder
}

final case class Team(team_id: Option[Int], team_name: Option[String])

object Team {
  implicit val decoder: Decoder[Team] = deriveDecoder
//  implicit val decoder: Decoder[Team] = (c: HCursor) =>
//    for {
//      teamId   <- c.downField("team_id").as[Int]
//      teamName <- c.downField("team_name").as[String]
//  } yield Team(teamId, teamName)

  implicit val encoder: Encoder[Team] = deriveEncoder
}
