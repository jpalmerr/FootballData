package footballdata

import cats.arrow.FunctionK
import cats.effect.{ExitCode, IO, IOApp, LiftIO, Resource}
import cats.implicits._
import doobie.free.connection.ConnectionIO
import footballdata.db.{DatabaseMigration, PostgresStore}
import org.http4s.server.Server

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  val liftToConnIO: FunctionK[IO, ConnectionIO] = LiftIO.liftK[ConnectionIO]

  private val serverResource: Resource[IO, Server[IO]] =
    for {
      _         <- Resource.liftF(IO.fromEither(DatabaseMigration.flywayMigrateDatabase))
      ec        = new ExecutionContexts
      store     <- PostgresStore.resource[IO](ec, liftToConnIO)
      resources <- Resources[IO, ConnectionIO](global, store)
      server    <- FootballServer.stream(resources)
    } yield server


  def run(args: List[String]): IO[ExitCode] = {
    serverResource.use(_ => IO.never).as(ExitCode.Success)
  }
}