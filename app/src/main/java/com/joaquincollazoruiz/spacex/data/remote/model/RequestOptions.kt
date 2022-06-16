package com.joaquincollazoruiz.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RequestOptions(
    @Json(name = "populate") val fieldsToPopulate: List<String> = emptyList(),
    @Json(name = "pagination") val pagination: Boolean = false,
    @Json(name = "sort") val sort: SortByDateUnixOption = SortByDateUnixOption(direction = -1),
) {
    fun populateWith(field: String): RequestOptions {
        val populateUnion = fieldsToPopulate.union(listOf(field)).toList()
        return RequestOptions(
            fieldsToPopulate = populateUnion,
            pagination = pagination,
            sort = sort
        )
    }
}