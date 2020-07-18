package org.wlosp.varo.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object VaroStorageTable : IntIdTable("storage") {
    val stage: Column<Stage> = enumeration("stage", Stage::class)
}

class VaroStorage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VaroStorage>(VaroStorageTable) {
        val storage: VaroStorage
            get() = findById(1)!!
    }

    var stage: Stage by VaroStorageTable.stage
}

enum class Stage {
    SETUP,
    READY,
    STARTING,
    RUNNING
}