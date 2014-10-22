package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin

import org.gradle.api.Task
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.artifacts.Dependency
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import spock.lang.*


import static org.gradle.api.logging.LogLevel.LIFECYCLE
import static org.junit.Assert.assertTrue

/**
 * @author R. Tyler Croy
 *
 */
class JRubyStormPluginSpec extends Specification {

    def project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }

    def "Basic sanity check"() {
        expect:
            project.tasks.jrubyStorm instanceof Task
            project.tasks.jrubyStorm.group == JRubyPlugin.TASK_GROUP_NAME
    }

    def "Check configurations exist"() {
      given:
        def configs = project.configurations
      expect:
        configs.getByName('jrubyStorm')
        configs.getByName('jrubyStormLocal')
    }

    def "Check jrubyStorm dependencies are correct"() {
      given:
        def deps = project.configurations.getByName('jrubyStorm').dependencies
      expect:
        deps.matching { Dependency d -> d.name == 'redstorm' }
    }

    def "Check jrubyStormLocal dependencies are correct"() {
      given:
        def deps = project.configurations.getByName('jrubyStormLocal').dependencies
      expect:
        deps.matching { Dependency d -> d.name == 'storm-core' }
    }
}

