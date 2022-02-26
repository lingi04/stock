project(":stock-api") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation(project(":stock-domain"))
    }
}
