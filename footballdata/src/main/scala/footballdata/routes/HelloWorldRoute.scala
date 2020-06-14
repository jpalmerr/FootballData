package footballdata.routes

import cats.effect.Sync
import cats.implicits._
import footballdata.HelloWorldProgram
import footballdata.models.Name
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HelloWorldRoute {

  def helloWorldRoutes[F[_]: Sync](H: HelloWorldProgram[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }
}
