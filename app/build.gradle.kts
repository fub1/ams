// build version code  management tools
import java.text.SimpleDateFormat
import java.util.Properties
import java.util.Date

// Set up the version properties file
val versionPropsFile = file("version.properties")
val versionProps = Properties()

if (versionPropsFile.canRead()) {
    versionProps.load(versionPropsFile.inputStream())
}

// Increment version code and version name
val code = versionProps["versionCode"].toString().toInt()
// Ensure that version name increments by 0.0001 and formatted correctly
val name = "%.4f".format(versionProps["versionName"].toString().toFloat() + 0.0001)

versionProps["versionCode"] = code.toString()
versionProps["versionName"] = name
versionProps.store(versionPropsFile.writer(), null)

// Generate the current date in "yyyyMMdd" format
val dateFormat = SimpleDateFormat("yyMMdd")
val buildDate: String = dateFormat.format(Date())


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    //kotlin2.0+ with compose.compiler
    alias(libs.plugins.compose.compiler)
    //for datastore-protobuf
    alias(libs.plugins.google.protobuf)

//    kotlin("plugin.serialization") version "1.8.0" // 请根据实际的 Kotlin 版本调整 240819

}




android {

    namespace = "com.crty.ams"
    compileSdk = 34




    defaultConfig {
        applicationId = "com.crty.ams"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = name


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            //isMinifyEnabled = false
            versionNameSuffix = "-$buildDate"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            versionNameSuffix = "-dev-$buildDate"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // kotlin2.0+ with compose.compiler下方方法废弃
        // kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

        // reference google demo sunflower version 15898e5e5c2212cb7d174e6d182f92baee2d6cae
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// protobuf settings
// Setup protobuf configuration, generating lite Java and Kotlin classes
// reference nowinandroid
// Proto DataStore 默认要求在 app/src/main/proto/ 目录下的 proto 文件中保存预定义的架构。（下面改写了这个路径）
// 此架构用于定义您在 Proto DataStore 中保存的对象的类型。
protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }

    // Generates the java Protobuf-lite code for the Protobuf in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}





dependencies {
    // Moshi
//    implementation(libs.converter.moshi)

//    implementation(libs.logging.interceptor) // 照相功能用
//    implementation(libs.kotlinx.serialization.json.v151) // dataPicker用
//    implementation(libs.ucrop) // 照相功能用
//    implementation(libs.timber) // 照相功能用
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)

    // Libs Group1: AndroidX.core
    // https://developer.android.com/jetpack/androidx/releases/core
    // Kotlin
    implementation(libs.androidx.core.ktx)
    // To use the Animator APIs
    implementation(libs.androidx.core.animation)

    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)


    // To test the Animator APIs
    androidTestImplementation(libs.androidx.core.animation.testing)
    // Optional - to support backwards compatibility of RemoteViews
    implementation(libs.androidx.core.remoteviews)
    // Optional - APIs for SplashScreen,Android 12+ App Startup Logo
    implementation(libs.androidx.core.splashscreen)

    // Libs Group2: AndroidX.Compose
    // Compose 由 androidx 中的 7 个 Maven 组 ID 构成2-1->2-6(compiler 除外)
    // BOM+running time+ui+foundation+compiler+material3+material+animation
    // https://developer.android.com/jetpack/androidx/releases/compose
    // BOM to library version mapping, compose库更新时会更新对应的BOM版本，需要查表更新BOM版本号

    // 2-0 BOM
    implementation(platform(libs.androidx.compose.bom))
    // 2-1 compose runtime
    // https://developer.android.com/jetpack/androidx/releases/compose-runtime
    implementation(libs.androidx.compose.runtime)
    // https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary
    implementation(libs.androidx.compose.runtime.saveable)



    // 2-2 ui
    // https://developer.android.com/jetpack/androidx/releases/compose-ui
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    // Tooling support (Previews, etc.)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Test
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // 2-3 androidx.compose.foundation
    // https://developer.android.com/jetpack/androidx/releases/compose-foundation
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)

    // 2-4 androidx.compose.material3
    // https://developer.android.com/jetpack/androidx/releases/compose-material3
    implementation(libs.androidx.compose.material3)
    // 2-5 androidx.compose.material
    // https://developer.android.com/jetpack/androidx/releases/compose-material
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    // 2-6 androidx.compose.animation
    // https://developer.android.com/jetpack/androidx/releases/compose-animation
    implementation(libs.androidx.compose.animation)



    // Libs Group3: AndroidX.activity.compose
    // https://developer.android.com/jetpack/androidx/releases/activity
    implementation(libs.androidx.activity.compose)

    // Libs Group4: AndroidX.lifecycle
    // https://developer.android.com/jetpack/androidx/releases/lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Lifecycle utilities for Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    implementation(libs.androidx.lifecycle.process)

    // Libs Group5: Dagger&Hilt
    // https://developer.android.com/jetpack/androidx/releases/hilt
    // BY Google Gemini
    // 5-1 Dagger
    implementation(libs.google.dagger.hilt.android)
    ksp(libs.google.dagger.hilt.android.compiler)
    // 5-2 Hilt组件https://developer.android.com/jetpack/androidx/releases/hilt
    // 暂时不引入
    // androidx.hilt.lifecycle  androidx.hilt.navigation androidx.hilt.navigation.compose
    // androidx.hilt.navigation.fragment androidx.hilt.work
    // implementation(libs.androidx.hilt.navigation)

    // Libs Group6: Navigation
    // https://developer.android.com/jetpack/androidx/releases/navigation
    // 6-1 Navigation Kotlin
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    // 6-2 Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)
    // 6-3 Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)


    // Libs Group7: Paging
    // https://developer.android.com/jetpack/androidx/releases/paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Libs Group8: Room
    implementation(libs.room.runtime)
    // For KSP
    ksp(libs.room.compiler) // For KSP
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)
    // optional - Paging 3 Integration
    implementation(libs.room.paging)
    // optional - Test helpers
    testImplementation(libs.room.testing)

    // Libs Group9: DataStore
    //https://developer.android.com/jetpack/androidx/releases/datastore
    // 8-1 DataStore-preferences
    implementation(libs.androidx.datastore.preferences)
    // 8-2 DataStore-Proto
    implementation(libs.androidx.datastore.proto)
    // 8-3 DataStore-Proto: google protobuf
    implementation(libs.google.protobuf.javalite)
    implementation(libs.google.protobuf.protoc)


    // Libs Group10: WorkManager
    // https://developer.android.com/jetpack/androidx/releases/work
    // 10-1 Kotlin + coroutines
    implementation(libs.work.runtime.ktx)
    // 10-2 Testing
    androidTestImplementation(libs.work.testing)

    // Libs Group11: network
    implementation(libs.gson)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2)
    // Libs Group12: coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)


    // Libs Group-x
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}



