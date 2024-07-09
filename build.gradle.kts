import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "me.chaoticwagon"
version = getFullVersion()

repositories {
    mavenCentral()
    maven("https://jitpack.io") // Minestom
}

dependencies {
    // Minestom
    implementation(libs.minestom)

    // Configuration
    implementation(libs.bundles.hoplite)

    // Debugging
    implementation(libs.debugRenderer)

    // Misc
    implementation(libs.guava)
    implementation(libs.minimessage)
    implementation(kotlin("stdlib-jdk8"))

}

tasks {
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")

        manifest {
            attributes(
                "Main-Class" to "me.chaoticwagon.vivarium.VivariumKt",
            )
        }

        from("LICENSE")

        minimize {
            exclude(dependency("com.github.Minestom:Minestom:.*"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

fun getFullVersion(): String {
    val version = project.property("vivarium.version")!! as String
    return if (version.contains("-SNAPSHOT")) {
        "$version+rev.${getGitRevision()}"
    } else {
        version
    }
}

fun getGitRevision(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
