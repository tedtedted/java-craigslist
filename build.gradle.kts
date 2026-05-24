plugins {
    `java-library`
    jacoco
    alias(libs.plugins.spotless)
    alias(libs.plugins.maven.publish)
}

group = "io.github.tedredington"
version = "0.1.0-SNAPSHOT"
description = "A Java 21 client for searching Craigslist listings."

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

sourceSets {
    create("examples") {
        java.srcDir("src/examples/java")
        resources.srcDir("src/examples/resources")
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

configurations {
    named("examplesImplementation") { extendsFrom(configurations.implementation.get()) }
    named("examplesRuntimeOnly") { extendsFrom(configurations.runtimeOnly.get()) }
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jsoup)
    api(libs.slf4j.api)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
    testImplementation(libs.mockwebserver)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.slf4j.simple)

    "examplesRuntimeOnly"(libs.slf4j.simple)
}

tasks.test {
    useJUnitPlatform {
        excludeTags("live")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("liveTest") {
    description = "Runs @Tag(\"live\") tests against real Craigslist."
    group = "verification"
    useJUnitPlatform { includeTags("live") }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-serial", "-Xlint:-processing"))
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).apply {
        addBooleanOption("Xdoclint:none", true)
        addStringOption("Xmaxwarns", "1")
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

spotless {
    java {
        googleJavaFormat(libs.versions.googleJavaFormat.get())
        target("src/**/*.java")
        targetExclude("build/**")
    }
    kotlinGradle {
        target("*.gradle.kts")
    }
}

mavenPublishing {
    coordinates(group.toString(), "java-craigslist", version.toString())
    pom {
        name.set("java-craigslist")
        description.set(project.description)
        url.set("https://github.com/tedredington/java-craigslist")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("tedredington")
                name.set("Ted Redington")
            }
        }
        scm {
            url.set("https://github.com/tedredington/java-craigslist")
            connection.set("scm:git:git://github.com/tedredington/java-craigslist.git")
            developerConnection.set("scm:git:ssh://git@github.com/tedredington/java-craigslist.git")
        }
    }
}
