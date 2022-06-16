package com.joaquincollazoruiz.spacex.data.local.model

import androidx.room.ColumnInfo
import com.joaquincollazoruiz.spacex.domain.model.PatchImage

/*
 * DATABASE
 */
data class LaunchPatchEntity(
    @ColumnInfo(name = "smallPatch") val small: String?,
    @ColumnInfo(name = "largePatch") val large: String?
)

fun LaunchPatchEntity.toDomainModel() = PatchImage(
    smallURL = small,
    largeURL = large,
)
