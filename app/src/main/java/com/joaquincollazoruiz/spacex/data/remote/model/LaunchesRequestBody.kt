package com.joaquincollazoruiz.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class LaunchesRequestBody(
    @Json(name = "query") val query: RequestQuery? = null,
    @Json(name = "options") val options: RequestOptions? = null
)