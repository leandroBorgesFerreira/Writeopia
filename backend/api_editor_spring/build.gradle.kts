plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.spring.dependencyManagement)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":backend:api_editor"))
    implementation(project(":plugins:writeopia_serialization"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.coroutines.reactor)

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation(libs.kotlin.test)
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
