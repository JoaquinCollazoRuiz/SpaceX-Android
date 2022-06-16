package shared.fakes

import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus

val FakeLaunch = Launch(
    id = "launchid",
    name = "launchname",
    launchDateTime = FakeDateTime,
    launchStatus = LaunchStatus.Failed,
    rocket = FakeRocket,
    links = FakeLinks,
)