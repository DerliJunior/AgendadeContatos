plugins {
}

android {
    namespace = "deveandroid.odair.agendadecontatos"
    compileSdk = 34

    defaultConfig {
        applicationId = "deveandroid.odair.agendadecontatos"
        minSdk = 21
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
}

dependencies {

}