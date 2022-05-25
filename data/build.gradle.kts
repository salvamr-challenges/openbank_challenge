import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    val localProperties = gradleLocalProperties(rootDir)

    compileSdk = Project.targetSdk

    defaultConfig {
        minSdk = Project.minSdk
        targetSdk = Project.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))

        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("api.key")}\"")
        buildConfigField("String", "API_KEY_PRIVATE", "\"${localProperties.getProperty("api.key.private")}\"")
        buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("api.url")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":domain"))
    customImplementation(Dependencies.data)
}