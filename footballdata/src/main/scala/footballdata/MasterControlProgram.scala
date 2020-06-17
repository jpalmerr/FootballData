package footballdata

import cats.effect.Sync
import footballdata.client.FootballClient
import footballdata.models.{StatusResponse, TeamTransferResponse}

trait MasterControlProgram[F[_]] {

  def getStatus: F[StatusResponse]
  def getTransfersByTeam: F[TeamTransferResponse]
}

object MasterControlProgram {

  def apply[F[_]: Sync] (
                        footballClient: FootballClient[F]
                        )
  = new MasterControlProgram[F] {

    override def getStatus: F[StatusResponse] = {
      footballClient.getApiStatus
    }

    override def getTransfersByTeam: F[TeamTransferResponse] = ???
  }
}
