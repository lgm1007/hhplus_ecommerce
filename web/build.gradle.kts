plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
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
	implementation(project(":application"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.github.microutils:kotlin-logging:3.0.5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	testImplementation(kotlin("test"))
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
