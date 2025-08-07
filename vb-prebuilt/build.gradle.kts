plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "live.hms.vb_prebuilt"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.4.8"
    }

}

dependencies {

    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation(project(":prebuilt-themes"))
    implementation("androidx.compose.ui:ui-android:1.6.8")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.ui:ui-viewbinding:1.3.2")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
//    implementation(platform('androidx.compose:compose-bom:2023.10.01'))
//
//            // Material Design 3
//            implementation 'androidx.compose.material3:material3'
//    implementation 'androidx.compose.foundation:foundation'
//    implementation 'androidx.compose.ui:ui-tooling-preview'
//    debugImplementation 'androidx.compose.ui:ui-tooling'
//// UI Tests
//    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
//    debugImplementation 'androidx.compose.ui:ui-test-manifest'
//// Optional - Integration with ViewModels
//    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1'
//    // Optional - Integration with LiveData
//    implementation "androidx.compose.ui:ui-viewbinding:1.3.2"
//    implementation 'androidx.compose.runtime:runtime-livedata'
//    implementation "com.github.bumptech.glide:compose:1.0.0-beta01"

}

val copyFonts by tasks.registering(Copy::class) {
    from("../../../assets/fonts")
    into("src/main/res/font")
    include("*.ttf", "*.otf")
    rename { filename ->
        // Extract the extension first
        val lastDotIndex = filename.lastIndexOf('.')
        val nameWithoutExt = if (lastDotIndex > 0) filename.substring(0, lastDotIndex) else filename
        val extension = if (lastDotIndex > 0) filename.substring(lastDotIndex) else ".ttf"

        // Handle specific font name conversions
        val converted = nameWithoutExt
            .replace(Regex("-"), "_")                    // Replace hyphens with underscores
            .replace(Regex("SemiBold"), "Semibold")      // Fix SemiBold -> Semibold
            .replace(Regex("ExtraBold"), "Extrabold")    // Fix ExtraBold -> Extrabold
            .replace(Regex("([a-z])([A-Z])"), "$1_$2")  // Add underscore before capitals
            .lowercase()                                 // Convert to lowercase
            .replace(Regex("[^a-z0-9_]"), "_")          // Replace invalid chars with underscores

        // Ensure starts with a letter
        val finalName = if (!converted.first().isLetter()) {
            "f$converted"
        } else {
            converted
        }

        "$finalName$extension"
    }
}

tasks.named("preBuild") {
    dependsOn(copyFonts)
}

// Publishing section removed for local development
