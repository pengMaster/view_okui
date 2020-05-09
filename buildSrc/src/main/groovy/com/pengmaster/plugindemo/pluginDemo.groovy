package com.pengmaster.plugindemo

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class pluginDemo implements Plugin<Project> {
    @Override
    public void apply(Project project) {

        def extension = project.extensions.create("name",Extension)
        project.afterEvaluate {
            println(" hello ${extension.name}")
        }
    }
}