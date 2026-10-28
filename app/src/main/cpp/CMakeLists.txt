cmake_minimum_required(VERSION 3.22.1)
project(gorilla LANGUAGES CXX)

add_library(gorilla SHARED native.cpp)
target_compile_features(gorilla PRIVATE cxx_std_20)
target_compile_options(gorilla PRIVATE -Wall -Wextra -Wpedantic -fvisibility=hidden)

find_library(log-lib log)
target_link_libraries(gorilla PRIVATE ${log-lib})
