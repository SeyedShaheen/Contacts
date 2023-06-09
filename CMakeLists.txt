cmake_minimum_required(VERSION 3.16)

project(Contacts VERSION 0.1 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(Qt6 6.4 REQUIRED COMPONENTS Quick)

qt_standard_project_setup()

qt_add_executable(appContacts
    main.cpp
    contactmodel.h contactmodel.cpp
)

qt_add_resources(appContacts "android"
    PREFIX /
    FILES
        android/src/com/example/myappication/MainActivity.java
        android/AndroidManifest.xml
        android/build.gradle
        android/res/values/libs.xml
)

qt_add_qml_module(appContacts
    URI Contacts
    VERSION 1.0
    QML_FILES Main.qml
)

set_target_properties(appContacts PROPERTIES
    MACOSX_BUNDLE_GUI_IDENTIFIER my.example.com
    MACOSX_BUNDLE_BUNDLE_VERSION ${PROJECT_VERSION}
    MACOSX_BUNDLE_SHORT_VERSION_STRING ${PROJECT_VERSION_MAJOR}.${PROJECT_VERSION_MINOR}
    MACOSX_BUNDLE TRUE
    WIN32_EXECUTABLE TRUE

    QT_ANDROID_PACKAGE_SOURCE_DIR ${CMAKE_CURRENT_SOURCE_DIR}/android
)

target_link_libraries(appContacts
    PRIVATE Qt6::Quick
)

install(TARGETS appContacts
    BUNDLE DESTINATION .
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR})
