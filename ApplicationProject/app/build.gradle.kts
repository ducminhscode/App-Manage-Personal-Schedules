
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.applicationproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.applicationproject"
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

    buildFeatures {
        dataBinding = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.i18n)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    //Sign in with Google
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.sun.mail:android-mail:1.6.2")
    implementation("com.sun.mail:android-activation:1.6.2")

    //OTP
    implementation("com.google.android.material:material:1.9.0")

    //Add recycle view
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //Add card view
    implementation("androidx.cardview:cardview:1.0.0")

    //Add chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Api gmail
//    implementation ("com.google.api-client:google-api-client:2.0.0")
//    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
//    implementation ("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")

    //Image
    implementation("io.github.ParkSangGwon:tedimagepicker:1.6.1")
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.4.2")

}
