plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.cyclonedx.bom") version "3.4.1"
}

android {
    namespace = "com.example.runner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.runner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

tasks.register("exportRuntimeDeps") {
    dependsOn("assembleDebug")
    doLast {
        val targetDir = layout.buildDirectory.dir("deps").get().asFile
        targetDir.mkdirs()
        project.configurations.getByName("debugRuntimeClasspath").resolve().forEach { file ->
            if (file.name.endsWith(".jar")) {
                val dest = File(targetDir, file.name)
                if (!dest.exists()) {
                    file.copyTo(dest)
                }
            }
        }
    }
}