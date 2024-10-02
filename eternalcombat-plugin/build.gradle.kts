plugins {
    `eternalcombat-java`
    `eternalcombat-repositories`

    id("net.minecrell.plugin-yml.bukkit")
    id("io.github.goooler.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    implementation(project(":eternalcombat-api"))

    // kyori
    implementation("net.kyori:adventure-platform-bukkit:${Versions.ADVENTURE_PLATFORM_BUKKIT}")
    implementation("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_TEXT_MINIMESSAGE}")

    // litecommands
    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")

    // Okaeri configs
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:${Versions.OKAERI_CONFIGS_YAML_BUKKIT}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS_SERDES_COMMONS}")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:${Versions.OKAERI_CONFIGS_SERDES_BUKKIT}")

    // Panda utilities
    implementation("org.panda-lang:panda-utilities:${Versions.PANDA_UTILITIES}")

    // GitCheck
    implementation("com.eternalcode:gitcheck:${Versions.GIT_CHECK}")

    // commons
    implementation("commons-io:commons-io:${Versions.APACHE_COMMONS}")

    // bstats
    implementation("org.bstats:bstats-bukkit:${Versions.B_STATS_BUKKIT}")

    // caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:${Versions.CAFFEINE}")

    implementation("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")

    // worldguard
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:${Versions.WORLD_GUARD_BUKKIT}")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:${Versions.PLACEHOLDER_API}")
}

bukkit {
    main = "com.eternalcode.combat.CombatPlugin"
    author = "EternalCodeTeam"
    apiVersion = "1.13"
    prefix = "EternalCombat"
    name = "EternalCombat"
    softDepend = listOf("WorldGuard", "PlaceholderAPI")
    version = "${project.version}"
}

tasks {
    runServer {
        minecraftVersion("1.21.1")
    }
}

tasks.shadowJar {
    archiveFileName.set("EternalCombat v${project.version}.jar")

    dependsOn("checkstyleMain")
    dependsOn("checkstyleTest")
    dependsOn("test")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
        "kotlin/**",
        "javax/**"
    )

    val prefix = "com.eternalcode.combat.libs"
    listOf(
        "panda.std",
        "panda.utilities",
        "org.panda-lang",
        "eu.okaeri",
        "net.kyori",
        "org.bstats",
        "dev.rollczi.litecommands",
        "com.eternalcode.gitcheck",
        "org.json.simple",
        "org.apache.commons",
        "javassist",
        "com.github.benmanes.caffeine",
        "com.eternalcode.commons"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}
