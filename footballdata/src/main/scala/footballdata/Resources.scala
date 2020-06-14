package footballdata

import cats.effect.{ConcurrentEffect, Resource, Sync}
import footballdata.client.HttpFootballClient

import scala.concurrent.ExecutionContext

final case class Resources[F[_]: ConcurrentEffect](footballClient: HttpFootballClient[F])

object Resources {
  def apply[F[_]: ConcurrentEffect](ec: ExecutionContext): Resource[F, Resources[F]] = {

    for {
      football <- HttpFootballClient[F](ec)
    } yield Resources(football)
  }
}