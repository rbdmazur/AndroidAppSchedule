package com.example.schedule.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "subjects",
    indices = [Index("id")])
data class Subject(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "subject_name") val name: String
) {
    override fun toString(): String {
        return name
    }
}
