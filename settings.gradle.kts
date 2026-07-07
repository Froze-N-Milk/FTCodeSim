pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://repo.dairy.foundation/releases")
    }
}

includeBuild("Examples")
includeBuild("Simulator")
includeBuild("DriverStationWindow")
includeBuild("MotorModeling")
