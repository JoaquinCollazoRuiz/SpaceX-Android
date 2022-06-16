package shared.fakes

import com.joaquincollazoruiz.spacex.data.remote.model.LaunchWithRocketDto

val FakeSuccessfulLaunchWithRocketDto2015 = LaunchWithRocketDto(
    id = "id",
    name = "launchname",
    success = true,
    dateUtc = "2015-12-22T01:29:00.000Z",
    dateLocal = "2015-12-22T21:29:00-04:00",
    dateUnix = 1450747740,
    linksDto = FakeLaunchLinksDto,
    rocketDto = FakeRocketDto,
)

val FakeFailedLaunchWithRocketDto2015 = LaunchWithRocketDto(
    id = "id",
    name = "launchname",
    success = false,
    dateUtc = "2015-12-22T01:29:00.000Z",
    dateLocal = "2015-12-22T21:29:00-04:00",
    dateUnix = 1450747740,
    linksDto = FakeLaunchLinksDto,
    rocketDto = FakeRocketDto,
)

val FakeSuccessfulLaunchWithRocketDto2016 = LaunchWithRocketDto(
    id = "id",
    name = "launchname",
    success = true,
    dateUtc = "2016-12-22T01:29:00.000Z",
    dateLocal = "2016-12-22T21:29:00-04:00",
    dateUnix = 1482304692,
    linksDto = FakeLaunchLinksDto,
    rocketDto = FakeRocketDto,
)

val FakeFailedLaunchWithRocketDto2016 = LaunchWithRocketDto(
    id = "id",
    name = "launchname",
    success = false,
    dateUtc = "2016-12-22T01:29:00.000Z",
    dateLocal = "2016-12-22T21:29:00-04:00",
    dateUnix = 1482304692,
    linksDto = FakeLaunchLinksDto,
    rocketDto = FakeRocketDto,
)