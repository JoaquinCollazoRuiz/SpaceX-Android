package com.joaquincollazoruiz.spacex.data.mappers

import com.joaquincollazoruiz.spacex.data.local.model.LaunchLinksEntity
import com.joaquincollazoruiz.spacex.data.local.model.toDomainModel
import com.joaquincollazoruiz.spacex.domain.model.Links
import com.joaquincollazoruiz.spacex.domain.model.PatchImage
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.FakeLaunchLinksEntity
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class LaunchLinksEntityMappingTest {

    private lateinit var entity: LaunchLinksEntity

    @Before
    fun setUp() {
        entity = FakeLaunchLinksEntity
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `entity should correctly map to domain model`() {
        val subject = entity.toDomainModel()

        expectThat(subject).isA<Links>()
        expectThat(subject.articleURL).isEqualTo(entity.articleURL)
        expectThat(subject.wikiURL).isEqualTo(entity.wikiURL)
        expectThat(subject.webcastURL).isEqualTo(entity.youtubeURL)
        with(expectThat(subject.patchImage).isA<PatchImage>()) {
            get(PatchImage::smallURL).isEqualTo(entity.patchEntity!!.small)
            get(PatchImage::largeURL).isEqualTo(entity.patchEntity!!.large)
        }
    }

}