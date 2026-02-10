plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
    id("maven-publish")
    id("java")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String
base.archivesName.set(project.property("archives_base_name") as String)

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("spadblock") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

repositories {
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
    maven("https://maven.shedaniel.me/") { name = "shedaniel" }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    include("com.google.code.gson:gson:2.10.1")

    // Apache HttpClient и все его зависимости
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    include("org.apache.httpcomponents:httpclient:4.5.14")

    implementation("org.apache.httpcomponents:httpcore:4.4.16")
    include("org.apache.httpcomponents:httpcore:4.4.16")

    implementation("commons-codec:commons-codec:1.15")
    include("commons-codec:commons-codec:1.15")

    // ← ДОБАВЬТЕ ЭТУ СТРОКУ
    implementation("commons-logging:commons-logging:1.2")
    include("commons-logging:commons-logging:1.2")
}



tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version.toString(),
            "minecraft_version" to project.property("minecraft_version").toString(),
            "loader_version" to project.property("loader_version").toString(),
            "fabric_version" to project.property("fabric_version").toString()
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}
