
[![](https://img.shields.io/badge/API-22%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://img.shields.io/badge/Support-Linkedin-brightgreen)](https://in.linkedin.com/in/ashish-pujari-b2655166)
# Project Title 

Currency Convertor : It's simple currency convertor app, implemented using kotlin.

# Prerequisites

Dependency in root build.gradle file

```
	// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext.kotlin_version = '1.3.70'
    ext.retrofit_version='2.7.1'
    ext.coroutine_version='1.3.2'
    ext.ktx_core_version='1.1.0'
    ext.roomVersion = '2.2.5'
    ext.archLifecycleVersion = '2.2.0'
    ext.coreTestingVersion = '2.1.0'
    ext.materialVersion = '1.1.0'
    ext.coroutines = '1.3.4'
    ext.room_ktx='2.2.1'
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

 ```
# Dependency

Dependency and app details -  app-level build.gradle file

```
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'
android {
    compileSdkVersion 29
    buildToolsVersion "29.0.1"
    packagingOptions {
        exclude 'META-INF/atomicfu.kotlin_module'
    }
    defaultConfig {
        applicationId "com.ashish.currencyconverter"
        minSdkVersion 23
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }
    buildToolsVersion = '28.0.3'
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.2.0'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    //android x
    implementation 'androidx.annotation:annotation:1.1.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    //retrofit
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation 'com.squareup.okhttp3:logging-interceptor:4.3.0'

    //coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutine_version"
    implementation 'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2'

    //core
    implementation "androidx.core:core-ktx:$ktx_core_version"
    implementation "androidx.annotation:annotation:$ktx_core_version"

    // Room components
    implementation "androidx.room:room-runtime:$roomVersion"
    kapt "androidx.room:room-compiler:$roomVersion"
    androidTestImplementation "androidx.room:room-testing:$roomVersion"
    implementation "androidx.room:room-ktx:$room_ktx"
    kapt "androidx.room:room-compiler:$room_ktx"

    // Lifecycle components
    implementation "androidx.lifecycle:lifecycle-extensions:$archLifecycleVersion"
    kapt "androidx.lifecycle:lifecycle-compiler:$archLifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$archLifecycleVersion"

    // Kotlin components
    api "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines"
    api "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines"

    //facebook stetho
    implementation 'com.facebook.stetho:stetho:1.5.1'
}

  ```
  
 # Usage
  Used MVVM + Repository + Room + Retrofit
 
# Screenshots

Please check below screen shots.
![Alt text](https://github.com/ashishpujari204/CurrencyConverter/blob/staging/icon.png "App Logo")
![Alt text](https://github.com/ashishpujari204/CurrencyConverter/blob/staging/home.png "Home Screen")
![Alt text](https://github.com/ashishpujari204/CurrencyConverter/blob/staging/list.png "Currency Selection List")

# Contributing

Please fork this repository and contribute back using (https://github.com/ashishpujari204/CurrencyConverter) 

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated but will be thoroughly reviewed .

# Licenses

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright [2020] [Ashish Pujari]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
