import org.javamodularity.moduleplugin.extensions.TestModuleOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/**
 * Gradle version 6.6.1
 */
plugins {
    kotlin("jvm") version "1.5.31"
    `java-library`
    id("org.openjfx.javafxplugin") version "0.0.10"
    id("org.jetbrains.dokka") version "1.4.20"
}
//see gradle.properties
val tornadoVersion: String by project
val kotlinVersion: String by project
val jsonVersion: String by project
val dokkaVersion: String by project
val httpclientVersion: String by project
val felixFrameworkVersion: String by project
val junit4Version: String by project
val junit5Version: String by project
val testfxVersion: String by project
val hamcrestVersion: String by project
val fontawesomefxVersion: String by project

group = "no.tornado"
version = "2.0.0-SNAPSHOT"
description = "JavaFX Framework for Kotlin"

extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

    api("org.glassfish:javax.json:${jsonVersion}")
    api("org.apache.httpcomponents:httpclient:${httpclientVersion}")
    api("de.jensd:fontawesomefx-fontawesome:${fontawesomefxVersion}")
    implementation("org.apache.felix:org.apache.felix.framework:${felixFrameworkVersion}")
}

javafx {
    version = "16"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.swing", "javafx.web", "javafx.media")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks.withType<JavaCompile> {
    options.release.set(16)
}

java.withSourcesJar()

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

/**
 * Testing
 */
sourceSets {
    getByName("test").java.srcDirs("src/test/kotlin")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    //no modules yet
    extensions.configure(TestModuleOptions::class) {
        runOnClasspath = true
    }
}

dependencies {
    //common
    testImplementation("org.hamcrest:hamcrest:${hamcrestVersion}")
    testImplementation("org.hamcrest:hamcrest-library:${hamcrestVersion}")
    testImplementation("org.testfx:testfx-junit5:${testfxVersion}")
    //Junit 5
    testImplementation(platform("org.junit:junit-bom:${junit5Version}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:${kotlinVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    //Junit 4
    testCompileOnly("junit:junit:${junit4Version}")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    //headless
    testRuntimeOnly("org.testfx:openjfx-monocle:jdk-12.0.1+2") // jdk-9+181 for Java 9, jdk-11+26 for Java 11
}

