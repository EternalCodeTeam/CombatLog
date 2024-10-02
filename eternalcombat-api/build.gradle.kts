plugins {
    `eternalcombat-java`
    `eternalcombat-repositories`
    `eternalcombat-publish`
    `eternalcombat-java-unit-test`
}

dependencies {
    // Spigot api
    compileOnlyApi("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}
