plugins {
    // Apply the application plugin to add support for
    // building a CLI application in Java.
    application
    // https://plugins.gradle.org/plugin/org.openjfx.javafxplugin
    id("org.openjfx.javafxplugin") version "0.0.13"
}
repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
dependencies {
}
javafx {
    version = "21.0.2"
    modules = mutableListOf("javafx.controls")
}
application {
    // Define the main class for the application.
    mainClass.set("at.fhooe.sail.ois.test.Start")
}