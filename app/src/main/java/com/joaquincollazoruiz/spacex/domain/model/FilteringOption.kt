package com.joaquincollazoruiz.spacex.domain.model

sealed interface FilteringOption {
    data class ByLaunchStatus(val status: LaunchStatus) : FilteringOption
}