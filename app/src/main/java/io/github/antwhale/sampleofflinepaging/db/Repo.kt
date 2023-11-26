package io.github.antwhale.sampleofflinepaging.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey @ColumnInfo @field:SerializedName("id") val id: Long,
    @ColumnInfo @field:SerializedName("name") val name: String,
    @ColumnInfo @field:SerializedName("full_name") val fullName: String,
    @ColumnInfo @field:SerializedName("description") val description: String?,
    @ColumnInfo @field:SerializedName("html_url") val url: String,
    @ColumnInfo @field:SerializedName("stargazers_count") val stars: Int,
    @ColumnInfo @field:SerializedName("forks_count") val forks: Int,
    @ColumnInfo @field:SerializedName("language") val language: String?
)