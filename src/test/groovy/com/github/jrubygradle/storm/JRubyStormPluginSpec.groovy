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

    def "Check configurations exist"() {
        expect:
        project.configurations.findByName('jrubyStorm')
    }

    @Ignore
    def "Check jrubyStorm dependencies are correct"() {
        given:
        def deps = project.configurations.getByName('jrubyStorm').dependencies

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'redstorm' }
    }

    @Ignore
    def "Check jrubyStormLocal dependencies are correct"() {
        given:
        def deps = project.configurations.getByName('jrubyStormLocal').dependencies

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'storm-core' }
    }

    @Ignore
    def "setting storm.version should add the right jrubyStormLocal dependency"() {
        given:
        String version = '0.1.1'
        def dependencies = project.configurations.findByName('jrubyStormLocal').dependencies

        when:
        project.storm.defaultVersion version
        project.evaluate()

        then:
        project.storm.defaultVersion == version
        dependencies.matching { Dependency d ->
            d.name == 'storm-core' && d.version == version
        }
    }

    @Ignore
    def "setting storm.redstormVersion should add the right jrubyStorm dependnecy"() {
        given:
        String version = '0.1.1'
        def dependencies = project.configurations.findByName('jrubyStorm').dependencies

        when:
        project.storm.defaultRedstormVersion version
        project.evaluate()

        then:
        project.storm.defaultRedstormVersion == version
        dependencies.matching { Dependency d ->
            d.name == 'redstorm' && d.version == version
        }
    }
}

