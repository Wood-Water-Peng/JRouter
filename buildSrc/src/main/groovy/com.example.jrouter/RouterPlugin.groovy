package com.example.jrouter

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        def ext  = project.extensions.findByType(BaseExtension.class)
//        LibraryExtension libraryExtension = project.extensions.findByName(LibraryExtension.class)
        System.out.println("RouterPlugin extensions->"+{ext.toString()})
        ext.registerTransform(new RouterTransform())
//        libraryExtension.registerTransform(new RouterTransform())
    }
}