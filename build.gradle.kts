// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false

    // добавляем плагин Google services Gradle для Firebase
    id("com.google.gms.google-services") version "4.4.4" apply false
}