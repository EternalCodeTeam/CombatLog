plugins {
    `java-library`
    checkstyle
}

group = "com.eternalcode"
version = "1.3.3"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

checkstyle {
    toolVersion = "10.21.1"

    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")

    maxErrors = 0
    maxWarnings = 0
}

// https://github.com/JabRef/jabref/pull/10812/files#diff-49a96e7eea8a94af862798a45174e6ac43eb4f8b4bd40759b5da63ba31ec3ef7R267
configurations.named("checkstyle") {
    resolutionStrategy {
        capabilitiesResolution {
            withCapability("com.google.collections:google-collections") {
                select("com.google.guava:guava:33.4.0-jre")
            }
        }
    }
}

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.release = 17
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
