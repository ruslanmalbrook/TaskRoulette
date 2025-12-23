plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "tech.taskroulette.benchmark"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Reason: Macrobenchmark is a self-instrumenting module targeting the app module.
    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    buildTypes {
        create("benchmark") {
            // Reason: allow installation on local devices for benchmarking.
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
    }
}

androidComponents {
    beforeVariants(selector().all()) { variantBuilder ->
        // Reason: only build benchmark variant to keep Gradle configuration light.
        variantBuilder.enable = variantBuilder.buildType == "benchmark"
    }
}

dependencies {
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
}


