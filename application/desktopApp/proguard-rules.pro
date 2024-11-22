-keep class org.sqlite.** { *; }
-keep class org.slf4j.**{ *; }
-keep class com.sun.jna.** { *; }
-keep class org.bouncycastle.jcajce.provider.** { *; }
-keep class org.bouncycastle.jce.provider.** { *; }
-keep class * implements com.sun.jna.* { *; }
-keep class io.ktor.** { *; }


-keep class kotlinx.datetime.** { *; }
-dontwarn org.slf4j.**
-dontwarn android.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keep class androidx.compose.runtime.** { *; }
-keep class kotlinx.coroutines.** { *; }

