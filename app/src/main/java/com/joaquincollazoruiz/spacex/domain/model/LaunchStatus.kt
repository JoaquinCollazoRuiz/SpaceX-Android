package com.joaquincollazoruiz.spacex.domain.model

sealed interface LaunchStatus {
    /** Launch was successful **/
    object Successful : LaunchStatus

    /** Launch failed **/
    object Failed : LaunchStatus

    /** For cases where launch neither failed not succeeded (e.g. will launch in the future) **/
    object FutureLaunch : LaunchStatus
}