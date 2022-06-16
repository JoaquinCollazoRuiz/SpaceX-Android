package com.joaquincollazoruiz.spacex.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joaquincollazoruiz.spacex.domain.model.DateTime
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus

/*
 * DATABASE
 */
@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey(autoGenerate = false) val uid: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "success") val success: Boolean?,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "dateUtc") val dateUtc: String?,
    @ColumnInfo(name = "dateLocal") val dateLocal: String?,
    @ColumnInfo(name = "dateUnix") val dateUnix: Long?,
    @Embedded val links: LaunchLinksEntity?,
    @Embedded val rocket: RocketEntity?,
)

fun LaunchEntity.toDomainModel(): Launch {
    val status = when (success) {
        true -> LaunchStatus.Successful
        false -> LaunchStatus.Failed
        else -> LaunchStatus.FutureLaunch
    }
    return Launch(
        id = uid,
        name = name,
        launchDateTime = if (dateUnix != null) DateTime(dateUnix) else null,
        launchStatus = status,
        rocket = rocket?.toDomainModel(),
        links = links?.toDomainModel(),
    )
}