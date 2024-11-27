plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.example.alarmmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.alarmmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding=true
    }
}

dependencies {
    // Views/Fragments Integration
    implementation ("androidx.navigation:navigation-fragment:2.8.3")
    implementation ("androidx.navigation:navigation-ui:2.8.3")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    implementation ("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.8.20")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}