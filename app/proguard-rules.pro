# Desktop-only integrations are compile-time dependencies, never bundled in this Android draft.
-dontwarn org.lwjgl.**
-dontwarn net.dv8tion.**
-dontwarn club.minnced.**
-dontwarn java.awt.**
-dontwarn javax.**
-dontwarn java.lang.management.**
-dontwarn io.github.classgraph.**
# Registration uses names derived from the authoritative annotations.
-keep @net.ncplanner.plannerator.planner.ncpf.annotation.RegisterWith class * { *; }
-keepattributes RuntimeVisibleAnnotations,Signature,InnerClasses,EnclosingMethod

-keep @net.ncplanner.ncpf.registry.NcpfRegistered class * { *; }
-keep @interface net.ncplanner.ncpf.registry.NcpfRegistered
-keep @interface net.ncplanner.plannerator.planner.ncpf.annotation.RegisterWith

# Optional desktop-only DSSL native interop uses JDK internals.
-dontwarn sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
-dontwarn sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl

# The registration code inspects generic superclasses. Keep the generic parents too.
-keep class net.ncplanner.plannerator.planner.ncpf.Design { *; }
-keep class net.ncplanner.plannerator.planner.ncpf.design.MultiblockDesign { *; }

# NCPF JSON models are constructed and populated reflectively by Gson.
-keep class net.ncplanner.ncpf.structure.** { *; }
