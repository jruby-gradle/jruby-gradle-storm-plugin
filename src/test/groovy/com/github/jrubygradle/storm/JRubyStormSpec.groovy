package com.github.jrubygradle.storm

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
        project.task('jrubyStorm', type: JRubyStorm)

        then: "JRuby should be cased properly so it doesn't annoy rtyler"
        project.tasks.findByName('assembleJRubyStorm')
    }

    def "creating a task with a jRuby string should also capitalize nicely"() {
        when:
        project.task('jRUbyStorm', type: JRubyStorm)

        then: "JRuby should be cased properly so it doesn't annoy rtyler"
        project.tasks.findByName('assembleJRubyStorm')
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
}
