plugins {
    `java-library`
    kotlin("jvm") version "1.7.21"
    kotlin("kapt") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ru.melonhell"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.elytrium.net/repo/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("com.velocitypowered:velocity-proxy:3.2.0-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("net.elytrium.limboapi:api:1.1.16")
}

tasks {
    shadowJar {
        relocate("net.elytrium.commons.kyori", "net.elytrium.limboapi.thirdparty.commons.kyori")
        relocate("net.elytrium.commons.config", "net.elytrium.limboapi.thirdparty.commons.config")
        archiveVersion.set("")
        archiveClassifier.set("")
        minimize()
    }
    assemble.get().dependsOn(shadowJar)
    jar.get().enabled = false
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}