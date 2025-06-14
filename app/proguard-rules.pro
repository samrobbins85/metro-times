#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.samrobbins.android.wearable.metrotimes.** { *; }
-keepattributes Annotation

# Add these rules to your existing proguard-rules.pro file

# CRITICAL: Keep generic signatures for reflection - this is likely your main issue
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Kotlin serialization specific rules
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Keep Kotlin serialization classes and their serializers
-keep,includedescriptorclasses class com.samrobbins.android.wearable.metrotimes.**$$serializer { *; }
-keepclassmembers class com.samrobbins.android.wearable.metrotimes.** {
    *** Companion;
}
-keepclasseswithmembers class com.samrobbins.android.wearable.metrotimes.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serializable data classes
-keep @kotlinx.serialization.Serializable class com.samrobbins.android.wearable.metrotimes.** { *; }

# Retrofit specific rules for R8 full mode
-keepattributes RuntimeVisibleParameterAnnotations

-keep,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Keep generic signatures for suspend functions
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Kotlinx Serialization - keep serializer instances
-if @kotlinx.serialization.Serializable class **
-keepnames class <1>$$serializer {
    static <1>$$serializer INSTANCE;
}

# OkHttp warnings
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Retrofit warnings
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.*
-dontwarn kotlin.Unit
-dontwarn javax.annotation.**
