package footballdata.db

import scala.util.control.NonFatal
import org.flywaydb.core.Flyway
import cats.syntax.either._

object DatabaseMigration {

  // TODO: load from config
  def flywayMigrateDatabase: Either[Throwable, Int] =
    Either
      .catchNonFatal {
        val flywayConfig = Flyway
          .configure()
          .dataSource("jdbc:postgresql://localhost:5432/football", "football-app", "password")
          .table("schema_version")
          .baselineOnMigrate(true)
        val migrationsApplied: Int = flywayConfig.load().migrate()

        migrationsApplied
      }
      .leftMap {
        case NonFatal(t) =>
          t
      }

}
