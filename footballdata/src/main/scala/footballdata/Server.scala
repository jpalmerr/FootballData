package footballdata

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import footballdata.routes._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object FootballServer {

  def stream[F[_]: ConcurrentEffect](resources: Resources[F])(implicit T: Timer[F], C: ContextShift[F]): Resource[F, Server[F]] = {
    import resources._

    // pass mcp into routes that choose to use them
    // can then be added to httpApp
    val mcp = MasterControlProgram(
      footballClient
    )

    for {
      client <- BlazeClientBuilder[F](global).resource
      helloWorldAlg = HelloWorldProgram.impl[F]
      jokeAlg = JokesProgram.impl[F](client)

      // Combine Service Routes into an HttpApp
      httpApp = (
        HelloWorldRoute.helloWorldRoutes[F](helloWorldAlg) <+>
        JokeRoute.jokeRoutes[F](jokeAlg) <+>
        StatusRoute.route[F](mcp)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .resource
    } yield exitCode
  }

}