pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://repo.dairy.foundation/releases")
    }
}

includeBuild("../Simulator") {
    dependencySubstitution {
        substitute(module("org.codeblooded:Simulator")).using(project(":"))
    }
}
