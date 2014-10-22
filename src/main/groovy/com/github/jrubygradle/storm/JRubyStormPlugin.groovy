package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin

import groovy.transform.PackageScope

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/**
 * @author R. Tyler Croy
 */
class JRubyStormPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.apply plugin : 'com.github.jruby-gradle.base'
        project.apply plugin : 'com.github.jruby-gradle.jar'
        project.apply plugin : 'com.github.johnrengelman.shadow'

        project.configurations.maybeCreate('jrubyStorm')
        project.configurations.maybeCreate('jrubyStormLocal')

        project.configurations {
          jrubyStormLocal.extendsFrom jrubyStorm
        }


        project.task('jrubyStorm', type: ShadowJar) {
            group JRubyPlugin.TASK_GROUP_NAME
            description 'Create a JRuby-based Storm topology'
            dependsOn project.tasks.jrubyPrepare
            exclude '*.sw*', '*.gitkeep', '*.md', 'META-INF/BCKEY*'

            into('topologies') {
              from 'topologies'
            }
            into('bolts') {
              from 'bolts'
            }

            jruby {
                // Use the default GEM installation directory
                defaultGems()
                mainClass 'redstorm.TopologyLauncher'
            }
        }

        updateRepositories(project)
        updateDependencies(project)

        project.afterEvaluate {
          JRubyStormLocal.updateDependencies(project)

          // Add the jrubyStorm configuration to the jrubyStorm task to make
          // sure ShadowJar will unzip/incorporate the right data unpacked into
          // the artifact
          project.tasks.jrubyStorm.configurations.add(project.configurations.getByName('jrubyStorm'))
        }
    }

    @PackageScope
    void updateRepositories(Project project) {
      project.repositories {
        // jcenter contains the redstorm and gradle dependencies
        jcenter()
        // Repositories for Storm dependencies
        mavenCentral()
        maven { url 'http://clojars.org/repo/' }
        maven { url 'http://conjars.org/repo/' }
      }
    }

    @PackageScope
    void updateDependencies(Project project) {
      project.dependencies {
        jrubyStorm (group: 'com.github.jruby-gradle',
                name: 'redstorm',
                version: '0.7.+') {
            exclude module: 'storm-core'
        }

        // Forcing 0.9.1 because of API incompatibilities with redstorm and
        // Storm 0.9.2 which have yet to be resolved
        jrubyStormLocal group: 'org.apache.storm',
                name: 'storm-core',
                version: '0.9.1-incubating',
                force: true
      }
    }
}
