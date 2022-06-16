package com.joaquincollazoruiz.spacex.data.remote.model

import com.joaquincollazoruiz.spacex.data.local.model.RocketEntity
import com.joaquincollazoruiz.spacex.domain.model.Rocket
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RocketDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "type") val type: String?,
)

fun RocketDto.toDomainModel() = Rocket(name = name, type = type)

fun RocketDto.toEntity() = RocketEntity(id = id, name = name, type = type)