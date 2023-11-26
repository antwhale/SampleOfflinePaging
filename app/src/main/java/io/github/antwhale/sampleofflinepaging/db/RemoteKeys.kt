package io.github.antwhale.sampleofflinepaging.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    @ColumnInfo
    val repoId: Long,
    @ColumnInfo
    val prevKey: Int?,
    @ColumnInfo
    val nextKey: Int?
)