plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("kotlin-kapt")
  id("com.google.dagger.hilt.android")
  id("de.mannodermaus.android-junit5")
  id("dev.shreyaspatil.compose-compiler-report-generator") version "1.3.1"
}

android {
  namespace = "com.techjd.mytasks"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.techjd.mytasks"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    kotlinCompilerExtensionVersion = "1.5.13"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  kapt {
    correctErrorTypes = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
  implementation("androidx.activity:activity-compose:1.9.0")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")
  testImplementation("junit:junit:4.13.2")
  testImplementation("io.mockk:mockk:1.13.11")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  // compose navigation
  implementation("androidx.navigation:navigation-compose:2.8.0-beta01")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

  // dagger hilt
  implementation("com.google.dagger:hilt-android:2.50")
  kapt("com.google.dagger:hilt-android-compiler:2.50")

  // retrofit
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")

  // for hiltviewmodel()
  implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

  // define a BOM and its version
  implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

  // define any required OkHttp artifacts without version
  implementation("com.squareup.okhttp3:okhttp")
  implementation("com.squareup.okhttp3:logging-interceptor")

  // for repeatOnLifecycle
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")

  // junit5
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

  // mock web server
  testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

  // for testing coroutines
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}