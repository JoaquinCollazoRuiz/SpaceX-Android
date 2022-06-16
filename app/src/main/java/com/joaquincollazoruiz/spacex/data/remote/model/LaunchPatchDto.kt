package com.joaquincollazoruiz.spacex.data.remote.model

import com.joaquincollazoruiz.spacex.data.local.model.LaunchPatchEntity
import com.joaquincollazoruiz.spacex.domain.model.PatchImage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchPatchDto(
    @Json(name = "small") val small: String?,
    @Json(name = "large") val large: String?
)

fun LaunchPatchDto.toDomainModel() = PatchImage(
    smallURL = small,
    largeURL = large,
)

fun LaunchPatchDto.toEntity(): LaunchPatchEntity = LaunchPatchEntity(
    small = small,
    large = large,
)