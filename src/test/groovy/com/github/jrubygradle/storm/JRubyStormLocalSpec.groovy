package com.github.jrubygradle.storm

import org.gradle.api.Task
import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 *
 */
class JRubyStormLocalSpec extends Specification {
    protected Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }

    def "jrubyStormLocal task should be a proper instance"() {
        when:
        project.task('jrubyStormLocal', type: JRubyStormLocal)

        then:
        project.tasks.jrubyStormLocal instanceof JRubyStormLocal
    }

    def "the task should inherit the topology configured on the parent"() {
        given:
        JRubyStorm parent = project.task('spock-parent', type: JRubyStorm)
        parent.topology = 'foo.rb'
        JRubyStormLocal task = project.task('spock', type: JRubyStormLocal)

        when:
        task.parentTask = parent

        then:
        task.topology == parent.topology
    }

    def "I should be able to set a topology to run without a parent task"() {
        given:
        JRubyStormLocal task = project.task('spock', type: JRubyStormLocal)
        String topologyFile = 'topology.rb'

        when:
        task.topology = topologyFile

        then:
        task.topology == topologyFile
    }
}
