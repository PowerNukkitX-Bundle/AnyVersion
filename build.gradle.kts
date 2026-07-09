plugins {
    java
    alias(libs.plugins.shadow)
}

group = "org.powernukkitx.anyversion"
version = "2.8.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

repositories {
    mavenLocal()
    maven("https://repo.powernukkitx.org/releases")
    mavenCentral()
    maven("https://www.jitpack.io")
    maven("https://repo.opencollab.dev/maven-releases")
    maven("https://repo.opencollab.dev/maven-snapshots")
}

dependencies {
    compileOnly(libs.server)
    compileOnly(libs.bedrock.connection)
    compileOnly(libs.okaeri.configs.core)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.shadowJar {
    archiveBaseName = "AnyVersion"
    archiveClassifier = ""
    archiveVersion = ""
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
