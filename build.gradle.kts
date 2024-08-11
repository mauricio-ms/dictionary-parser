plugins {
    id("java")
}

group = "com.lang.trainer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.antlr:antlr4:4.13.1")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.itextpdf:itextpdf:5.5.13.3")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}