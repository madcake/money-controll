# Room entities and DAOs
-keep @androidx.room.Entity class *
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao interface *
-keep class * implements androidx.room.RoomDatabaseConstructor

# Keep entities fields from being obfuscated as Room uses reflection to map them
-keepclassmembers class * {
    @androidx.room.PrimaryKey *;
    @androidx.room.ColumnInfo *;
    @androidx.room.Embedded *;
    @androidx.room.Relation *;
}

# Koin
-keep class org.koin.** { *; }
-keep @org.koin.core.annotation.KoinViewModel class *
-keepclassmembers class * {
    @org.koin.core.annotation.InjectedParam *;
}

# Keep ViewModel constructors for Koin
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Kotlin Serialization (if used)
-keepattributes *Annotation*, EnclosingMethod, Signature
-keepnames class kotlinx.serialization.internal.GeneratedSerializer { *; }
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class * {
    @kotlinx.serialization.SerialName *;
}

# Data classes that might be used in reflection (e.g. for Room or UI state)
-keepclassmembers class shiny.mc.core.dto.** { *; }
-keepclassmembers class shiny.mc.infrastructure.persistent.room.entity.** { *; }
