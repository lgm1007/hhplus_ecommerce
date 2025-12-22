plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.24"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.redisson:redisson-spring-boot-starter:3.34.1") {
        exclude(group = "org.redisson", module = "redisson-spring-data-33")
    }
    implementation("org.redisson:redisson-spring-data-27:3.34.1")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}