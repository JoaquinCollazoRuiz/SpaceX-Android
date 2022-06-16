package com.joaquincollazoruiz.spacex.data.remote.model

import com.joaquincollazoruiz.spacex.data.local.model.LaunchLinksEntity
import com.joaquincollazoruiz.spacex.domain.model.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchLinksDto(
    @Json(name = "webcast") val youtubeURL: String?,
    @Json(name = "article") val articleURL: String?,
    @Json(name = "wikipedia") val wikiURL: String?,
    @Json(name = "patch") val patchDto: LaunchPatchDto?,
)

fun LaunchLinksDto.toDomainModel() = Links(
    webcastURL = youtubeURL,
    articleURL = articleURL,
    wikiURL = wikiURL,
    patchImage = patchDto?.toDomainModel()
)

fun LaunchLinksDto.toEntity() = LaunchLinksEntity(
    youtubeURL = youtubeURL,
    articleURL = articleURL,
    wikiURL = wikiURL,
    patchEntity = patchDto?.toEntity(),
)