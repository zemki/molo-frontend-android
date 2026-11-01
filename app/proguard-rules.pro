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

-dontwarn javax.validation.valueextraction.UnwrapByDefault
-dontwarn javax.validation.valueextraction.ValueExtractor
-keepnames class news.molo.core.navigation.Screen* { *; }
-keepnames class news.molo.core.navigation.TopLevelScreen { *; }
-keepnames class news.molo.core.navigation.TopLevelScreen$* { *; }

-keepnames class news.molo.domain.model.TabSelectionType { *; }
-keepnames class news.molo.domain.model.ConfigurationType { *; }
-keepnames class news.molo.domain.model.ConfigurationType { *; }
-keepnames class news.molo.android.navigation.BottomNavItem { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
