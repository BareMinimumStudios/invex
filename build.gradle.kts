import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("net.fabricmc.fabric-loom-remap")
	`maven-publish`
	kotlin("jvm") version libs.versions.kotlin
}

group = "xyz.naomieow.invex"

repositories {
	maven("https://api.modrinth.com/maven")
	maven("https://maven.nucleoid.xyz")
}

loom {
	splitEnvironmentSourceSets()

	accessWidenerPath = file("src/main/resources/invex.classtweaker")

	mods {
		register("invex") {
			sourceSet(sourceSets.main.get())
		}
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(loom.officialMojangMappings())

	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)
	modImplementation(libs.fabric.kotlin)

	modImplementation(libs.opc)?.let(::include)
	modImplementation(libs.sgui)?.let(::include)
	modImplementation(libs.permissionsapi)?.let(::include)
}

tasks.processResources {
	inputs.property("version", libs.versions.invex)

	filesMatching("fabric.mod.json") {
		expand("version" to libs.versions.invex)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 17
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_17
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
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
