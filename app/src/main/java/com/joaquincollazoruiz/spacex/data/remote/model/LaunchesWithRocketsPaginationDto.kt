package com.joaquincollazoruiz.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchesWithRocketsPaginationDto(
    @Json(name = "docs") val docs: List<LaunchWithRocketDto>,
    @Json(name = "totalDocs") val totalDocs: Int,
    @Json(name = "offset") val offset: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "pagingCounter") val pagingCounter: Int,
    @Json(name = "hasPrevPage") val hasPrevPage: Boolean,
    @Json(name = "hasNextPage") val hasNextPage: Boolean,
)