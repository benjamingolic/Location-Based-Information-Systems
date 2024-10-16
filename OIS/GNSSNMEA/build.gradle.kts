plugins {
    application
    // https://plugins.gradle.org/plugin/org.openjfx.javafxplugin
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    //  This dependency is used by the application plugin.
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.projectlombok:lombok:0.11.0")
    annotationProcessor ("org.projectlombok:lombok:1.18.20")
}

javafx {
    version = "20"
    modules = mutableListOf("javafx.controls")
}

application {
    // Define the main class for the application.
    mainClass.set("at.fhooe.sail.ois.gnss.Start")
}
