package shared.fakes

import com.joaquincollazoruiz.spacex.data.local.model.LaunchLinksEntity

val FakeLaunchLinksEntity = LaunchLinksEntity(
    youtubeURL = "youtubeURL",
    articleURL = "articleURL",
    wikiURL = "wikiURL",
    patchEntity = FakeLaunchPatchEntity
)