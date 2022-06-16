package com.joaquincollazoruiz.spacex.data.mappers

import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity
import com.joaquincollazoruiz.spacex.data.remote.model.LaunchWithRocketDto
import com.joaquincollazoruiz.spacex.data.remote.model.toDomainModel
import com.joaquincollazoruiz.spacex.data.remote.model.toEntity
import com.joaquincollazoruiz.spacex.domain.model.DateTime
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeFailedLaunchWithRocketDto2015
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class LaunchWithRocketDtoMappingTest {

    private lateinit var dto: LaunchWithRocketDto

    @Before
    fun setUp() {
        dto = FakeFailedLaunchWithRocketDto2015
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `dto should correctly map to domain model`() {
        val subject = dto.toDomainModel()

        expectThat(subject).isA<Launch>()
        expectThat(subject.id).isEqualTo(dto.id)
        expectThat(subject.name).isEqualTo(dto.name)
        with(expectThat(subject.launchDateTime).isA<DateTime>()) {
            get(DateTime::unixTimestamp).isEqualTo(dto.dateUnix)
        }
        expectThat(subject.launchStatus).isEqualTo(LaunchStatus.Failed)
        expectThat(subject.rocket).isNotNull()
        expectThat(subject.links).isNotNull()
    }

    @Test
    fun `dto should correctly map to entity model`() {
        val subject = dto.toEntity()

        expectThat(subject).isA<LaunchEntity>()
        expectThat(subject.year).isEqualTo(2015)
        expectThat(subject.success).isEqualTo(false)

        expectThat(subject.uid).isEqualTo(dto.id)
        expectThat(subject.name).isEqualTo(dto.name)
        expectThat(subject.dateUtc).isEqualTo(dto.dateUtc)
        expectThat(subject.dateUnix).isEqualTo(dto.dateUnix)
        expectThat(subject.dateLocal).isEqualTo(dto.dateLocal)
        expectThat(subject.rocket).isNotNull()
        expectThat(subject.links).isNotNull()
    }

}