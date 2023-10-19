plugins {
    `maven-publish`
}

dependencies {
    // Spigot api
    compileOnlyApi(libs.spigotApi)

    // kp.
    api(libs.adventurePlatformBukkit)
    api(libs.adventureTextMinimessage)

    // litecommands
    api(libs.liteCommands)

    // Okaeri configs
    api(libs.okaeriConfigsYamlBukkit)
    api(libs.okaeriConfigsSerdesCommons)
    api(libs.okaeriConfigsSerdesBukkit)

    // Panda utilities
    api(libs.pandaUtilities)

    // GitCheck
    api(libs.gitCheck)

    // commons
    api(libs.apacheCommons)

    // bstats
    api(libs.bStatsBukkit)

    // worldguard
    compileOnly(libs.worldGuardBukkit)

    // tests
    testImplementation(libs.spigotApi)
    testImplementation(libs.jUnitJupiterApi)
    testImplementation(libs.jUnitJupiterParams)
    testRuntimeOnly(libs.jUnitJupiterEngine)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "eternalcombat-api"
            from(project.components["java"])
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "eternalcodeReleases"
            url = uri("https://repo.eternalcode.pl/releases")
            credentials {
                username = System.getenv("ETERNAL_CODE_MAVEN_USERNAME")
                password = System.getenv("ETERNAL_CODE_MAVEN_PASSWORD")
            }
        }
    }
}
