project(":stock-batch") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-batch")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("mysql:mysql-connector-java")
        implementation("com.slack.api:slack-api-client:1.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.9.1")
        implementation("org.jsoup:jsoup:1.13.1")
        runtimeOnly("mysql:mysql-connector-java")
        compileOnly("com.yahoofinance-api:YahooFinanceAPI:3.12.3")
        implementation(project(":stock-domain"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.batch:spring-batch-test")
        testImplementation(project(":stock-domain"))
    }
}
