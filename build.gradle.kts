plugins {
    id("java")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.guava:guava:32.1.1-jre")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("ArticleCLI")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "ArticleCLI")
    }
}

tasks.test {
    useJUnitPlatform()
}
