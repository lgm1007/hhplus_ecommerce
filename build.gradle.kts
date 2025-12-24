plugins {
    // 루트에서는 플러그인 버전만 관리하고 적용은 하지 않음
    id("org.jetbrains.kotlin.jvm") version "1.9.25" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "2.7.18" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.24" apply false
}

allprojects {
    group = "com.example"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
