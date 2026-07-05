plugins {
    id("fabric-loom") version "1.17-SNAPSHOT"
    id("maven-publish")
}

val isObfuscated = stonecutter.current.project.startsWith("1.")

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project
val yarn_mappings = project.findProperty("yarn_mappings") as? String
val loader_version: String by project
val fabric_version: String by project
// val client_arguments_version: String by project

version = mod_version
group = maven_group

base {
    archivesName.set("${archives_base_name}_${stonecutter.current.project}")
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("gamingchair") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }

    runConfigs.configureEach {
        ideConfigGenerated(true)
        runDir("../../run")
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    // maven {
    //     url = uri("https://maven.xpple.dev/maven2")
    // }
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")
    if (stonecutter.current.project.startsWith("1.")) {
        add("mappings", "net.fabricmc:yarn:${yarn_mappings}:v2")
        add("modImplementation", "net.fabricmc:fabric-loader:${loader_version}")
        add("modImplementation", "net.fabricmc.fabric-api:fabric-api:${fabric_version}")
    } else {
        add("implementation", "net.fabricmc:fabric-loader:${loader_version}")
        add("implementation", "net.fabricmc.fabric-api:fabric-api:${fabric_version}")
    }

    // modImplementation("dev.xpple:clientarguments:${client_arguments_version}")
}

tasks.processResources {
    val mcVersion = stonecutter.current.project

    inputs.property("version", project.version)
    inputs.property("minecraft_version", mcVersion)
    inputs.property("loader_version", loader_version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "version" to project.version,
            "minecraft_version" to mcVersion,
            "loader_version" to loader_version
        ))
    }
}

val targetJavaVersion = if (isObfuscated) 21 else 25
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = archives_base_name
            from(components["java"])
        }
    }
    repositories {
    }
}

if (isObfuscated) {
    tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
        destinationDirectory.set(file("${rootProject.projectDir}/builds"))
    }
} else {
    tasks.named<org.gradle.jvm.tasks.Jar>("jar") {
        destinationDirectory.set(file("${rootProject.projectDir}/builds"))
    }
}