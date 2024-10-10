plugins {
	id("fabric-loom") version "1.7-SNAPSHOT"
	id("maven-publish")
}

val minecraft_version: String by project
val mod_version = System.getenv("MOD_VERSION") ?: System.getenv("GITHUB_SHA")?.take(7) ?: "0.0.0"
val maven_group: String by project
version = "$mod_version+$minecraft_version"
group = maven_group

base {
	val archives_base_name: String by project
	archivesName = archives_base_name
}

repositories {
	maven {
		name = "Terraformersmc Maven"
		url = uri("https://maven.terraformersmc.com/releases/")
	}
	maven {
		name = "Shedaniel Maven"
		url = uri("https://maven.shedaniel.me/")
	}
}

loom {
	splitEnvironmentSourceSets()

	mods {
		create("takeitpairs") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}

}

val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project
dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${minecraft_version}")
	mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
	modImplementation("net.fabricmc:fabric-loader:${loader_version}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

	// Include dependencies
	include("com.google.code.gson:gson:2.11.0")

	// Config dependencies
	modApi("com.terraformersmc:modmenu:11.0.1")
	modApi("me.shedaniel.cloth:cloth-config-fabric:15.0.127")
}

tasks.processResources {
	inputs.property("version", project.version)
	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${project.base.archivesName.get()}"}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = project.base.archivesName.get()
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}