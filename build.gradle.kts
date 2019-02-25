//

plugins {
    kotlin("jvm") version "1.3.21"
    application
}

application {
    mainClassName = "MainKt"
//    gradle run --args='arg1 arg2'
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.8")

    testImplementation("junit:junit:4.12")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//gradle run --args='arg1 arg2'
//gradle distTar
//gradle distZip
//gradle installDist
