package com.joaquincollazoruiz.spacex.data.local.model

import androidx.room.ColumnInfo
import com.joaquincollazoruiz.spacex.domain.model.Rocket

/*
 * DATABASE
 */
data class RocketEntity(
    @ColumnInfo(name = "rocketId") val id: String,
    @ColumnInfo(name = "rocketName") val name: String?,
    @ColumnInfo(name = "rocketType") val type: String?,
)

fun RocketEntity.toDomainModel() = Rocket(name = name, type = type)