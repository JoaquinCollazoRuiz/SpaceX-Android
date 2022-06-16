package com.joaquincollazoruiz.spacex.data.mappers

import com.joaquincollazoruiz.spacex.data.local.model.RocketEntity
import com.joaquincollazoruiz.spacex.data.local.model.toDomainModel
import com.joaquincollazoruiz.spacex.domain.model.Rocket
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeRocketEntity
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class RocketEntityMappingTest {

    private lateinit var entity: RocketEntity

    @Before
    fun setUp() {
        entity = FakeRocketEntity
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `entity should correctly map to domain model`() {
        val subject = entity.toDomainModel()

        expectThat(subject).isA<Rocket>()
        expectThat(subject.name).isEqualTo(entity.name)
        expectThat(subject.type).isEqualTo(entity.type)
    }

}