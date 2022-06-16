package com.joaquincollazoruiz.spacex.data.mappers

import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity
import com.joaquincollazoruiz.spacex.data.local.model.toDomainModel
import com.joaquincollazoruiz.spacex.domain.model.DateTime
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import shared.fakes.FakeFailedLaunchEntity2015
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class LaunchEntityMappingTest {

    private lateinit var entity: LaunchEntity

    @Before
    fun setUp() {
        entity = FakeFailedLaunchEntity2015
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `entity should correctly map to domain model`() {
        val subject = entity.toDomainModel()

        expectThat(subject).isA<Launch>()
        expectThat(subject.id).isEqualTo(entity.uid)
        expectThat(subject.name).isEqualTo(entity.name)
        with(expectThat(subject.launchDateTime).isA<DateTime>()) {
            get(DateTime::unixTimestamp).isEqualTo(entity.dateUnix)
        }
        expectThat(subject.launchStatus).isEqualTo(LaunchStatus.Failed)
        expectThat(subject.rocket).isNotNull()
        expectThat(subject.links).isNotNull()
    }

}