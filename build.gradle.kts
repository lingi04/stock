plugins {
    id("java")
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

subprojects {
    group = "com.jh"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        compileOnly("org.projectlombok:lombok")
        runtimeOnly("com.h2database:h2")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("io.github.javaunit:autoparams-lombok:0.0.3")
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

val querydslProjects = listOf(
    project(":stock-domain"),
    project(":stock-batch")

)

configure(querydslProjects) {
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation("com.querydsl:querydsl-jpa")
//        compileOnly("com.querydsl:querydsl-core")
        annotationProcessor("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jpa") // querydsl JPAAnnotationProcessor 사용 지정
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
        testImplementation("com.querydsl:querydsl-jpa")
    }
}
