pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://repo.dairy.foundation/releases")
    }
}

includeBuild("../MotorModeling") {
    dependencySubstitution {
        substitute(module("org.codeblooded:MotorModeling")).using(project(":"))
    }
}
includeBuild("../DriverStationWindow") {
    dependencySubstitution {
        substitute(module("org.codeblooded:DriverStationWindow")).using(project(":"))
    }
}
