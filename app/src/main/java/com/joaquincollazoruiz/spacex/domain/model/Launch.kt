package com.joaquincollazoruiz.spacex.domain.model

data class Launch(
    val id: String,
    val name: String?,
    val launchDateTime: DateTime?,
    val launchStatus: LaunchStatus,
    val rocket: Rocket?,
    val links: Links?,
)