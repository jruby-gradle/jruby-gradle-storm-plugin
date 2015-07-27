package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin

import org.gradle.api.Project
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
 *
 */
class JRubyStormPluginSpec extends Specification {
    protected Project project

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

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'redstorm' }
    }

    def "Check jrubyStormLocal dependencies are correct"() {
        given:
        def deps = project.configurations.getByName('jrubyStormLocal').dependencies

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'storm-core' }
    }

    def "setting storm.version should add the right jrubyStormLocal dependency"() {
        given:
        String version = '0.1.1'
        def dependencies = project.configurations.findByName('jrubyStormLocal').dependencies

        when:
        project.storm.version version
        project.evaluate()

        then:
        project.storm.version == version
        dependencies.matching { Dependency d ->
            d.name == 'storm-core' && d.version == version
        }
    }
}

