package footballdata

import cats.effect.{ConcurrentEffect, Resource, Sync}
import footballdata.client.HttpFootballClient
import footballdata.db.Store

import scala.concurrent.ExecutionContext

final case class Resources[F[_], G[_]: Sync](
                                              footballClient: HttpFootballClient[F],
                                              store: Store[F, G]
                                            )

object Resources {
  def apply[F[_]: ConcurrentEffect,  G[_]: Sync](
                                     ec: ExecutionContext,
                                     store: Store[F, G]
                                   ): Resource[F, Resources[F, G]] = {

    for {
      football <- HttpFootballClient[F](ec)
    } yield Resources(football, store)
  }
}