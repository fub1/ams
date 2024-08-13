buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //kotlin2.0+ with compose.compiler
    alias(libs.plugins.compose.compiler) apply false
    //AS自带
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //ksp引入
    alias(libs.plugins.ksp) apply false
    // hilt引入 Group5: Dagger-Hilt by Google Gemini
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
}