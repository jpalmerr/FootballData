package footballdata.routes

import cats.effect.Sync
import cats.implicits._
import footballdata.JokesProgram
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object JokeRoute {

  def jokeRoutes[F[_]: Sync](J: JokesProgram[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }
}
