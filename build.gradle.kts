import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	`maven-publish`
	kotlin("jvm") version libs.versions.kotlin
	alias(libs.plugins.cloche)
}

group = "xyz.naomieow.invex"
version = "1.0.0"

repositories {
	cloche.librariesMinecraft()

	mavenCentral()

	cloche {
		main()
		mavenFabric()
	}

	maven("https://api.modrinth.com/maven")
	maven("https://maven.nucleoid.xyz")
	maven("https://maven.terraformersmc.com/")
	maven("https://maven.ladysnake.org/releases")
	maven("https://maven.wispforest.io/releases")
	maven("https://maven.shedaniel.me/")
}

cloche {
	metadata {
		modId = "invex"
		name = "InvEx"
		description = ""
		license = "BML-1.0"

		author {
			name = "naomieow"
			contact = "https://github.com/naomieow"
		}

		url = "https://github.com/BareMinimumStudios/invex"
		sources = "https://github.com/BareMinimumStudios/invex"
		issues = "https://github.com/BareMinimumStudios/invex/issues"

		icon = "assets/invex/icon.png"
	}

	common {
		mappings {
			official()
		}

		dependencies {
			modImplementation(libs.fabric.kotlin)
		}
	}

	fabric("1.20.1") {
		minecraftVersion = "1.20.1"
		loaderVersion = libs.versions.fabric.loader

		runs {
			server()
		}

		dependencies {
			fabricApi(libs.versions.fabric.api.get1201())

			modImplementation(libs.opc.get1201())
			include(libs.opc.get1201())
			modImplementation(libs.sgui.get1201())
			include(libs.sgui.get1201())
			modImplementation(libs.permissionsapi.get1201())
			include(libs.permissionsapi.get1201())

			modCompileOnly(libs.trinkets.get1201())
			modCompileOnly(libs.accessories.get1201())
		}

		metadata {
			dependencies {
				dependency {
					modId = "fabric-api"
					version(libs.versions.fabric.api.get1201().get())
				}
				dependency {
					modId = "fabric-language-kotlin"
					version(libs.versions.fabric.kotlin.get())
				}
			}

			entrypoint("main") {
				adapter = "kotlin"
				value = "xyz.naomieow.invex.InvEx"
			}
		}
	}

	fabric("1.21.1") {
		minecraftVersion = "1.21.1"
		loaderVersion = libs.versions.fabric.loader

		runs {
			server()
		}

		dependencies {
			fabricApi(libs.versions.fabric.api.get1211())

			modImplementation(libs.opc.get1211())
			include(libs.opc.get1211())
			modImplementation(libs.sgui.get1211())
			include(libs.sgui.get1211())
			modImplementation(libs.permissionsapi.get1211())
			include(libs.permissionsapi.get1211())

			modCompileOnly(libs.trinkets.get1211())
			modCompileOnly(libs.accessories.get1211())
		}

		metadata {
			dependencies {
				dependency {
					modId = "fabric-api"
					version(libs.versions.fabric.api.get1211().get())
				}
				dependency {
					modId = "fabric-language-kotlin"
					version(libs.versions.fabric.kotlin.get())
				}
			}

			entrypoint("main") {
				adapter = "kotlin"
				value = "xyz.naomieow.invex.InvEx"
			}
		}
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs = listOf("-Xmulti-platform", "-Xno-check-actual", "-Xexpect-actual-classes")
	}
}
