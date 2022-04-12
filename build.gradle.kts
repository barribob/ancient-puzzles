plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
val minecraftVersion: String by project
version = "$modVersion-$minecraftVersion"
val mavenGroup: String by project
group = mavenGroup
repositories {
    mavenCentral()
    maven {
        name = "Ladysnake Mods"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
    }
    maven { url = uri("https://www.cursemaven.com") }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {	url = uri("https://nexus.resourcefulbees.com/repository/maven-public/")}
    maven { url = uri("https://maven.terraformersmc.com/") }
}
tasks.test {
    useJUnitPlatform()
}
dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    val cardinalComponentsVersion: String by project
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:$cardinalComponentsVersion")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-chunk:$cardinalComponentsVersion")
    val maelstromVersion: String by project
    modImplementation("com.github.miyo6032:maelstrom-lib:${maelstromVersion}")
    include("com.github.miyo6032:maelstrom-lib:${maelstromVersion}")
    val junitVersion: String by project
    val junitCoreVersion: String by project
    testImplementation("org.assertj:assertj-core:${junitCoreVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${junitVersion}")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    modRuntimeOnly("curse.maven:disablecustomworldsadvice-401978:3677546")
    modRuntimeOnly("com.telepathicgrunt:CommandStructures-Fabric:2.0.0+1.18.2")
    val modmenuVersion: String by project
    modRuntimeOnly("com.terraformersmc:modmenu:${modmenuVersion}")
}
tasks {
    val javaVersion = JavaVersion.VERSION_17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}