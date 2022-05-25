// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
}

buildscript {
    dependencies {
        classpath(ClassPath.hilt)
        classpath(ClassPath.safeArgs)
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}