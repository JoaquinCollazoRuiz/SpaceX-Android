package com.joaquincollazoruiz.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LaunchesWithRocketsRequestBody(
    @Json(name = "query") query: RequestQuery? = null,
    @Json(name = "options") options: RequestOptions = RequestOptions()
) : LaunchesRequestBody(query, options.populateWith(PopulateRequestFields.Rocket))