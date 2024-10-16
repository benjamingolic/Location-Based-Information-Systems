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
    implementation("net.postgis:postgis-jdbc:2021.1.0")
    implementation("net.postgis:postgis-geometry:2021.1.0")
    implementation(files("${rootProject.projectDir.absolutePath}/libs/DummyGIS.jar"))
}
javafx {
    version = "21.0.2"
    modules = mutableListOf("javafx.controls", "javafx.swing")
}
application {
    // Define the main class for the application.
    mainClass.set("at.fhooe.sail.ois.map.Start")
}