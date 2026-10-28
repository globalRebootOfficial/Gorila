#include <jni.h>
#include <unistd.h>

#include <cstdio>
#include <string>

namespace {
std::string read_own_status() {
    const char* path = "/proc/self/status";
    FILE* file = std::fopen(path, "r");
    if (file == nullptr) return "Unable to read /proc/self/status";

    std::string result;
    char line[512]{};
    while (std::fgets(line, sizeof(line), file) != nullptr) {
        result += line;
    }
    std::fclose(file);
    return result;
}

jstring to_java(JNIEnv* env, const std::string& value) {
    return env->NewStringUTF(value.c_str());
}
} // namespace

extern "C" JNIEXPORT jstring JNICALL
Java_dev_gorilla_tool_MainActivity_nativeVersion(JNIEnv* env, jobject) {
    return to_java(env, "Gorilla Android / ARM64 / C++20");
}

extern "C" JNIEXPORT jstring JNICALL
Java_dev_gorilla_tool_MainActivity_nativeReadOwnStatus(JNIEnv* env, jobject) {
    return to_java(env, read_own_status());
}
