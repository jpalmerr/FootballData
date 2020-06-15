package footballdata

import cats.effect.Sync
import footballdata.client.FootballClient
import footballdata.models.StatusResponse

trait MasterControlProgram[F[_]] {

  def getStatus: F[StatusResponse]
}

object MasterControlProgram {

  def apply[F[_]: Sync] (
                        footballClient: FootballClient[F]
                        )
  = new MasterControlProgram[F] {

    override def getStatus: F[StatusResponse] = {
      footballClient.getApiStatus
    }
  }
}
