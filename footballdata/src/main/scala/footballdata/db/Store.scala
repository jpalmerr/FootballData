package footballdata.db

import cats.arrow.FunctionK
import cats.effect._
import cats.implicits._
import doobie.free.connection.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.transactor.Transactor
import footballdata.ExecutionContexts
import footballdata.models.TransferData

trait Store[F[_], G[_]] {
  def upsertTransferData(transferData: TransferData): G[Unit]
  def commit[A](f: G[A]): F[A]
  def lift: FunctionK[F, G]
}

class PostgresStore[F[_]](transactor: Transactor[F], val lift: FunctionK[F, ConnectionIO])(implicit b: Bracket[F, Throwable])
  extends Store[F, ConnectionIO] {

  val cioUnit: ConnectionIO[Unit] = ().pure[ConnectionIO]

  override def upsertTransferData(transferData: TransferData): ConnectionIO[Unit] =
    Statements.upsert(transferData).run.void

  override def commit[A](f: ConnectionIO[A]): F[A] = f.transact(transactor)

}

object PostgresStore {
  // TODO: load from config
  def resource[F[_]: Async: ContextShift](
                                           executionContexts: ExecutionContexts,
                                           lift: FunctionK[F, ConnectionIO]
                                         ): Resource[F, Store[F, ConnectionIO]] =
    HikariTransactor
      .newHikariTransactor[F](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/football",
        "football-app",
        "password",
        executionContexts.dbConnectionExecutionContext,
        executionContexts.dbTransactionBlocker
      )
      .map(new PostgresStore(_, lift))
}