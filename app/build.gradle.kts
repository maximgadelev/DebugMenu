plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.kpfu.itis.debugmenu"
    compileSdk = 34
    val releaseServer = "https://315996e0-2d7d-44a6-b352-fd878552dc42.mock.pstmn.io"
    val stageServer = "https://0b5aeaf2-4e94-450f-92d3-fa082dc84a57.mock.pstmn.io"
    val testServer = "https://748e8de9-1595-41ec-9a0b-985897a10f95.mock.pstmn.io"
    defaultConfig {
        applicationId = "ru.kpfu.itis.debugmenu"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        getByName("release") {
            buildConfigField(
                "String[]",
                "SERVER_URLS",
                "{\"$testServer\", \"$stageServer\", \"$releaseServer\"}"
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField(
                "String[]",
                "SERVER_URLS",
                "{\"$testServer\", \"$stageServer\", \"$releaseServer\"}"
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    val stageImplementation by configurations.creating
}

dependencies {
    val stageImplementation by configurations
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup:seismic:1.0.3")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("com.github.moxy-community:moxy:2.2.2")
    implementation("com.github.HBiSoft:HBRecorder:3.0.3")
    implementation("com.github.moxy-community:moxy-androidx:2.2.2")
    kapt("com.github.moxy-community:moxy-compiler:2.2.2")
    implementation("com.github.moxy-community:moxy-ktx:2.2.2")
    implementation("com.github.moxy-community:moxy-material:2.2.2")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")
    implementation("com.jakewharton:process-phoenix:2.1.1")
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:3.1.0")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:3.1.0")
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    implementation(project(":crashreporter"))
    "stageImplementation"("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.15")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}