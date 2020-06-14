package footballdata

import cats.effect.{ConcurrentEffect, ContextShift, Resource, Timer}
import cats.implicits._
import footballdata.client.HttpFootballClient
import footballdata.routes._
import fs2.Stream
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object Server {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {

    val ec = scala.concurrent.ExecutionContext.global

    val httpFootballClient: Resource[F, HttpFootballClient[F]] = HttpFootballClient(ec)

    val mcp = MasterControlProgram(
      ???
    )


    for {
      client <- BlazeClientBuilder[F](global).stream
      helloWorldAlg = HelloWorldProgram.impl[F]
      jokeAlg = JokesProgram.impl[F](client)

      // Combine Service Routes into an HttpApp
      httpApp = (
        HelloWorldRoute.helloWorldRoutes[F](helloWorldAlg) <+>
        JokeRoute.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}