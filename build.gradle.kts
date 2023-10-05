plugins {
    id("java")
    application
}

group = "org.dakralex.plcarticlemgmt"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.guava:guava:32.1.1-jre")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("articlecli.ArticleCLI")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "articlecli.ArticleCLI")
    }
}

tasks.test {
    useJUnitPlatform()
}
