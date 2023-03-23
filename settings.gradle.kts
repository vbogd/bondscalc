pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "BondsCalc"
include(
    ":app",
    ":data",
    ":domain",
    ":moex",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val coroutinesVersion = version("coroutinesVersion", "1.6.4")
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").version(coroutinesVersion)
        }
    }
}
