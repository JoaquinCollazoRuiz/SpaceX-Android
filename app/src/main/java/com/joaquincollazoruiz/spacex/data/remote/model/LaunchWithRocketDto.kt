package com.joaquincollazoruiz.spacex.data.remote.model

import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity
import com.joaquincollazoruiz.spacex.domain.model.DateTime
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.ZoneOffset

@JsonClass(generateAdapter = true)
data class LaunchWithRocketDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "success") val success: Boolean?,
    @Json(name = "date_utc") val dateUtc: String?,
    @Json(name = "date_local") val dateLocal: String?,
    @Json(name = "date_unix") val dateUnix: Long?,
    @Json(name = "links") val linksDto: LaunchLinksDto?,
    @Json(name = "rocket") val rocketDto: RocketDto?
)

fun LaunchWithRocketDto.toDomainModel(): Launch {
    val status = when (success) {
        true -> LaunchStatus.Successful
        false -> LaunchStatus.Failed
        else -> LaunchStatus.FutureLaunch
    }
    return Launch(
        id = id,
        name = name,
        rocket = rocketDto?.toDomainModel(),
        links = linksDto?.toDomainModel(),
        launchStatus = status,
        launchDateTime = if (dateUnix != null) DateTime(dateUnix) else null
    )
}

fun LaunchWithRocketDto.toEntity(): LaunchEntity {
    val dt = if (dateUnix == null) null else LocalDateTime.ofEpochSecond(
        dateUnix,
        0,
        ZoneOffset.UTC
    )

    return LaunchEntity(
        uid = id,
        name = name,
        success = success,
        dateUtc = dateUtc,
        year = dt?.year,
        dateLocal = dateLocal,
        dateUnix = dateUnix,
        links = linksDto?.toEntity(),
        rocket = rocketDto?.toEntity()
    )
}