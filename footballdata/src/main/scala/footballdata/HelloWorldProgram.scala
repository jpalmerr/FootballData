package footballdata


import cats.Applicative
import cats.implicits._
import footballdata.models.{Greeting, Name}

trait HelloWorldProgram[F[_]]{
  def hello(n: Name): F[Greeting]
}

object HelloWorldProgram {
  implicit def apply[F[_]](implicit ev: HelloWorldProgram[F]): HelloWorldProgram[F] = ev

  def impl[F[_]: Applicative]: HelloWorldProgram[F] = new HelloWorldProgram[F]{
    def hello(n: Name): F[Greeting] =
      Greeting("Hello, " + n.name).pure[F]
  }
}
