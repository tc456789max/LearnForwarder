# ProGuard 混淆规则 - 简化版本

# 保留所有类和成员（用于调试和学习）
-keep class ** { *; }

# 保留注解
-keepattributes *Annotation*

# 保留行号信息
-keepattributes SourceFile,LineNumberTable

# 保留泛型信息
-keepattributes Signature

# 保留异常信息
-keepattributes Exceptions

# 不优化不混淆（用于学习和调试）
-dontoptimize
-dontobfuscate

# 不警告
-dontwarn **
-ignorewarnings
