package shared.fakes

import com.joaquincollazoruiz.spacex.data.remote.model.LaunchLinksDto

val FakeLaunchLinksDto = LaunchLinksDto(
    youtubeURL = "youtubeURL",
    articleURL = "articleURL",
    wikiURL = "wikiURL",
    patchDto = FakeLaunchPatchDto
)