package footballdata

import cats.effect.Sync
import footballdata.client.FootballClient
import footballdata.db.Store
import footballdata.models.{StatusResponse, TeamTransferResponse}

trait MasterControlProgram[F[_]] {

  def getStatus: F[StatusResponse]
  def getTransfersByTeam(teamId: Int): F[TeamTransferResponse]
}

object MasterControlProgram {

  def apply[F[_]: Sync, G[_]: Sync] (
                        footballClient: FootballClient[F],
                        store: Store[F, G]
                        )
  = new MasterControlProgram[F] {

    override def getStatus: F[StatusResponse] = {
      footballClient.getApiStatus
    }

    override def getTransfersByTeam(teamId: Int): F[TeamTransferResponse] = {
      footballClient.getTeamTransfers(teamId)
    }
  }
}
