package footballdata

import cats.effect.Sync
import cats.implicits._
import footballdata.models._
import org.http4s.Method._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._

trait JokesProgram[F[_]]{
  def get: F[Joke]
}

object JokesProgram {
  def apply[F[_]](implicit ev: JokesProgram[F]): JokesProgram[F] = ev

  def impl[F[_]: Sync](C: Client[F]): JokesProgram[F] = new JokesProgram[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._
    def get: F[Joke] = {
      C.expect[Joke](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => JokeError(t)} // Prevent Client Json Decoding Failure Leaking
    }
  }
}
