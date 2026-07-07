plugins {
    id("dev.frozenmilk.jvm-library") version "11.1.0-1.1.1"
    id("dev.frozenmilk.publish") version "0.0.5"
    id("dev.frozenmilk.doc") version "0.0.5"
    application
}

repositories {
    maven("https://www.jitpack.io")
}

dependencies {
    implementation("com.github.WilliamAHartman:Jamepad:1.3.2")
    implementation("com.github.kwhat:jnativehook:2.2.2")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "ord.codeblooded"
            artifactId = "DriverStationWindow"

            artifact(dairyDoc.dokkaJavadocJar)
            artifact(dairyDoc.dokkaHtmlJar)

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}

application {
    mainClass = "org.jjophoven.driverstation.DriverStationWindow"
}

java {
    manifest {
        attributes["Main-Class"] = "org.jjophoven.driverstation.DriverStationWindow"
    }
}
