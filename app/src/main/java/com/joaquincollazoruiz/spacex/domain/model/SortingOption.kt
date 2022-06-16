package com.joaquincollazoruiz.spacex.domain.model

sealed interface SortingOption {
    object Ascending : SortingOption
    object Descending : SortingOption
}