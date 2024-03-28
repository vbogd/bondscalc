plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(11)
}

val ktorVersion = "2.1.2"

dependencies {
    implementation(project(":domain"))
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
//    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
//    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
