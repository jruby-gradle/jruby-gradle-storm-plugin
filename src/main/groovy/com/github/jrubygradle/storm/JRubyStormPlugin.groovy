package com.github.jrubygradle.storm

import groovy.transform.PackageScope

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 */
class JRubyStormPlugin implements Plugin<Project> {
    static final CLASSPATH_CONFIGURATION = 'jrubyStormClasspath'
    void apply(Project project) {
        project.apply plugin : 'com.github.jruby-gradle.base'
        project.apply plugin : 'com.github.jruby-gradle.jar'

        project.extensions.create('storm', JRubyStormExtension)
        project.task('jrubyStorm', type: JRubyStorm)

        project.configurations.maybeCreate(CLASSPATH_CONFIGURATION)

        updateRepositories(project)
    }

    @PackageScope
    void updateRepositories(Project project) {
      project.repositories {
        // Repositories for Storm dependencies
        maven { url 'http://clojars.org/repo/' }
        maven { url 'http://conjars.org/repo/' }
      }
    }
}
