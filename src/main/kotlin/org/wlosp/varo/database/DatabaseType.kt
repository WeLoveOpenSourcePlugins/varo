package org.wlosp.varo.database

import org.wlosp.varo.dependencies.Dependency

enum class DatabaseType(val useHikari: Boolean, val driver: Dependency) {

    SQLITE(false, Dependency.SQLITE_DRIVER),
    MYSQL(true, Dependency.MYSQL_DRIVER),
    POSTGRESQL(true, Dependency.POSTGRESQL_DRIVER)
}