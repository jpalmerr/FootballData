package footballdata

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import org.http4s.server.Server

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  private val serverResource: Resource[IO, Server[IO]] =
    for {
      resources <- Resources[IO](global)
      server    <- FootballServer.stream(resources)
    } yield server


  def run(args: List[String]): IO[ExitCode] = {
    serverResource.use(_ => IO.never).as(ExitCode.Success)
  }
}