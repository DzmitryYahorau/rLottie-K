
target_sources(rlottie
    PRIVATE
        "${CMAKE_CURRENT_LIST_DIR}/lottieitem.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottieloader.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottiemodel.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottieproxymodel.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottieparser.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottieanimation.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/lottiekeypath.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/native_lib.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/FileLog.cpp"
        "${CMAKE_CURRENT_LIST_DIR}/ByteArray.cpp"
    )

target_include_directories(rlottie
    PRIVATE
        "${CMAKE_CURRENT_LIST_DIR}"
    )


