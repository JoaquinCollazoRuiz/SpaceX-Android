package com.joaquincollazoruiz.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SortByDateUnixOption(@Json(name = "date_unix") val direction: Int)
