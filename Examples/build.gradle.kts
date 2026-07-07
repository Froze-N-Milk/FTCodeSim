plugins {
    id("dev.frozenmilk.teamcode") version "11.1.0-1.1.1"
    id("dev.frozenmilk.sinister.sloth.load") version "0.2.4"
}

repositories {
    maven("https://www.jitpack.io")
}

ftc {
    sdk.TeamCode()

    dairy {
        implementation(Sloth)
        implementation(slothboard)
    }

    pedro {
        implementation(core)
        implementation(telemetry)
        implementation(ftc)
    }
}

dependencies {
    implementation("com.pedropathing:ivy:1.0.0")
    implementation("dev.frozenmilk.dairy:CachingHardware:1.0.0")
    testImplementation("junit:junit:4.13.2")
    // no version means it will get the local version
    implementation("org.codeblooded:Simulator")
}