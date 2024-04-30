pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url =
                uri("https://mvnrepository.com/artifact/com.balsikandar.android/crashreporter/1.1.0")
        }
    }
}

rootProject.name = "My Application"
include(":app")
include(":crashreporter")
