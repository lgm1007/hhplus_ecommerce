plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

group = "com.example"
version = "1.0.0"

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":infrastructure"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Transactional을 위한 spring-data 의존성 추가
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.redisson:redisson-spring-boot-starter:3.34.1") {
		exclude(group = "org.redisson", module = "redisson-spring-data-33")
	}
	implementation("org.springframework.kafka:spring-kafka")
	implementation("io.github.microutils:kotlin-logging:3.0.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation(kotlin("test"))
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.assertj:assertj-core:3.22.0")
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
