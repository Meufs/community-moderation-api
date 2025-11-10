package snb.projects.bootstrap.configuration

import org.flywaydb.core.Flyway

object FlywayRunner {
    fun migrate(dbUrl: String?, dbUsername: String?, dbPassword: String?) {
        val dataSource = org.postgresql.ds.PGSimpleDataSource().apply {
            setURL(dbUrl)
            user = dbUsername
            password = dbPassword
        }

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas("meufs")
            .load()
        flyway.migrate()
    }
}