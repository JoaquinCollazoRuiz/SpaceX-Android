package com.joaquincollazoruiz.spacex.repository.caching

/**
 * Provides unix timestamp from epoch.
 **/
fun interface ICurrentUnixTimeProvider {
    fun currentTimeMillis(): Long
}