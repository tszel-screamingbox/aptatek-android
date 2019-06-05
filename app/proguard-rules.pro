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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# UXCam
-keep class com.uxcam.** { *; }
-dontwarn com.uxcam.**

# ActivityStarter
-keep class **Starter { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Crashyltics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-keep public class * extends com.github.mikephil.charting.renderer.BubbleChartRenderer

# JodaTime
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.aptatek.pkulab.device.bluetooth.model.** { <fields>; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# OpenCSV
-keep class org.apache.commons.** { *; }

# App-specific
-keep class com.aptatek.pkulab.view.** { *; }
-keep class com.aptatek.pkulab.domain.model.** { *; }
-keep class com.aptatek.pkulab.data.PinCode { *; }
-keep class com.aptatek.pkulab.data.model.** { *; }
-keepclassmembers class * {
    @activitystarter.Arg *;
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keep class net.sqlcipher.** { *; }
-keep class com.commonsware.cwac.saferoom.** { *; }

