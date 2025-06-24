plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    apply (extra["appConfig"] as com.android.build.gradle.internal.dsl.BaseAppModuleExtension.()->Unit)

    namespace = "cn.hello.demo"
    defaultConfig {
        applicationId = "cn.hello.demo"
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled= false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 使用公共的签名配置
            signingConfig = signingConfigs.getByName("signing")
        }
        debug {
            isMinifyEnabled= false
            // 使用公共的签名配置
            signingConfig = signingConfigs.getByName("signing")
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
    flavorDimensions += listOf("environment")
    productFlavors {
        create("dev"){
            signingConfig = signingConfigs.getByName("signing")
            buildConfigField("String", "API_URL", "\"https://dev.api.example.com\"")
            buildConfigField("boolean", "FEATURE_ENABLED", "true")
            manifestPlaceholders["app_name"] = "Hello_dev"
            dimension = "environment"
        }
        create("pro"){
            signingConfig = signingConfigs.getByName("signing")
            buildConfigField("String", "API_URL", "\"https://pro.api.example.com\"")
            buildConfigField("boolean", "FEATURE_ENABLED", "true")
            manifestPlaceholders["app_name"] = "Hello_pro"
            dimension = "environment"
        }
    }
    //定义分包
//    splits {
//        abi {
//            this.isEnable = true
//            this.isUniversalApk = false
//            reset()
//            this.include("armeabi-v7a", "arm64-v8a" ) // "x86", "x86_64"
//        }
//    }
}

dependencies {

//    implementation(fileTree("libs").include("*.jar","*.aar"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar","*.aar"))))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}