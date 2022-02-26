import org.springframework.boot.gradle.tasks.bundling.BootJar;

project(":stock-domain") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("mysql:mysql-connector-java")
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}
