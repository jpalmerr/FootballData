package footballdata.db

import doobie.implicits._
import doobie.util.update.Update0
import footballdata.models.TransferData

object Statements {

  def upsert(td: TransferData): Update0 =
    sql"INSERT INTO transactions (teamoutid, teamoutname, teaminid, teaminname, playername, transferdate, transfertype) VALUES (${td.teamOut.teamId}, ${td.teamOut.teamName}, ${td.teamIn.teamId}, ${td.teamIn.teamName}, ${td.playerName}, ${td.transferDate}, ${td.transferType})".update
}
