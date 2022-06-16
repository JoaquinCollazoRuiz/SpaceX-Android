package com.joaquincollazoruiz.spacex.data.mappers

import com.joaquincollazoruiz.spacex.data.local.model.RocketEntity
import com.joaquincollazoruiz.spacex.data.remote.model.RocketDto
import com.joaquincollazoruiz.spacex.data.remote.model.toDomainModel
import com.joaquincollazoruiz.spacex.data.remote.model.toEntity
import com.joaquincollazoruiz.spacex.domain.model.Rocket
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeRocketDto
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class RocketDtoMappingTest {

    private lateinit var dto: RocketDto

    @Before
    fun setUp() {
        dto = FakeRocketDto
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `dto should correctly map to domain model`() {
        val subject = dto.toDomainModel()

        expectThat(subject).isA<Rocket>()
        expectThat(subject.name).isEqualTo(dto.name)
        expectThat(subject.type).isEqualTo(dto.type)
    }

    @Test
    fun `dto should correctly map to entity model`() {
        val subject = dto.toEntity()

        expectThat(subject).isA<RocketEntity>()
        expectThat(subject.name).isEqualTo(dto.name)
        expectThat(subject.type).isEqualTo(dto.type)
    }

}