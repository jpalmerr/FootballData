package footballdata

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import footballdata.routes._
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

object FootballServer {

  def stream[F[_]: ConcurrentEffect](resources: Resources[F])(implicit T: Timer[F], C: ContextShift[F]): Resource[F, Server[F]] = {
    import resources._

    // pass mcp into routes that choose to use them
    // can then be added to httpApp
    val mcp = MasterControlProgram(
      footballClient
    )

    val helloWorldAlg = HelloWorldProgram.impl[F]

    // Combine Service Routes into an HttpApp
    val httpApp = (
        HelloWorldRoute.helloWorldRoutes[F](helloWorldAlg) <+>
        StatusRoute.route[F](mcp) <+>
        TeamTransferRoute.route[F](mcp)
      ).orNotFound

    // With Middlewares in place
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    for {
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .resource
    } yield exitCode
  }

}