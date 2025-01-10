import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.navigationSafeArgs)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.theawesomemovieapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.theawesomemovieapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").reader())
        // Acessa a chave TMDB_API_KEY do local.properties
        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")
    }

    buildFeatures {
        // Habilita a geração de campos personalizados para BuildConfig
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Carrousel
    implementation(libs.me.relex.circleindicator)
    implementation(libs.imaginativeworld.whynotimagecarousel)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    ksp(libs.moshi.kotlin.codegen)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.roomKsp)

    // Gson
    implementation(libs.gson)

    // DI
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    implementation(libs.hilt.navigation.fragment)
    kapt(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.core.testing)
    testImplementation(libs.mock.web.server)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.test.core)
}