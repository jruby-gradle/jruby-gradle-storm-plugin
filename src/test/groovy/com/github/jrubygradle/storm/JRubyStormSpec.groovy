package com.github.jrubygradle.storm

import org.gradle.api.artifacts.Dependency
import spock.lang.*

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

class JRubyStormSpec extends Specification {
    protected Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }

    def "instantiation should create a run{Name} task"() {
        when:
        project.task('tapa', type: JRubyStorm)

        then:
        project.tasks.findByName('runTapa')
    }

    def "instantiation should create an assemble{Name} task"() {
        when:
        project.task('tapa', type: JRubyStorm)

        then:
        project.tasks.findByName('assembleTapa')
    }

    def "creating a task with JRuby in the name should capitalize nicely"() {
        when:
        project.task('jrubyTapa', type: JRubyStorm)

        then: "JRuby should be cased properly so it doesn't annoy rtyler"
        project.tasks.findByName('assembleJRubyTapa')
    }

    def "creating a task with a jRuby string should also capitalize nicely"() {
        when:
        project.task('jRUBYTapa', type: JRubyStorm)

        then: "JRuby should be cased properly so it doesn't annoy rtyler"
        project.tasks.findByName('assembleJRubyTapa')
    }

    def "runTask should be a type of JRubyStormLocal"() {
        given:
        Task spock = project.task('spock', type: JRubyStorm) {
           topology 'spock.rb'
        }
        Task runTask = project.tasks.findByName('runSpock')

        expect:
        runTask instanceof JRubyStormLocal
        runTask.topology == 'spock.rb'
    }

    def "getStormVersion() should return the storm.defaultStormVersion by default"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)

        expect:
        task.stormVersion == project.storm.defaultVersion
    }

    def "if I've set a custom storm version, getStormVersion() should return that"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)
        String version = '0.1'

        when:
        task.stormVersion version

        then:
        task.stormVersion == version
    }

    def "getRedstormVersion() should return the storm.defaultRedstormVersion by default"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)

        expect:
        task.redstormVersion == project.storm.defaultRedstormVersion
    }

    def "if I've set a custom redstorm version, getRedstormVersion() should return that"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)
        String version = '0.1.'

        when:
        task.redstormVersion version

        then:
        task.redstormVersion == version
    }

    def "by default a configuration should be made for dependencies"() {
        when:
        project.task('spock', type: JRubyStorm)

        then:
        println project.configurations
        project.configurations.findByName(JRubyStorm.DEFAULT_CONFIGURATION_NAME)
    }

    def "evaluation of the project should result in dependencies being added to the configuration"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)
        def deps = task.configuration.dependencies

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'redstorm' }
    }

    def "evaluation of the project should result in local mode dependencies"() {
        given:
        JRubyStorm task = project.task('spock', type: JRubyStorm)

        when:
        project.evaluate()

        then:
        project.configurations.findByName('jrubyStormLocal')?.dependencies?.matching {
            it.name == 'storm-core'
        }
    }
}
