package com.github.jrubygradle.storm

import org.gradle.api.Task
import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 *
 */
class JRubyStormLocalSpec extends Specification {
    Project project
    JRubyStormLocal task

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
        task = project.task('spock', type: JRubyStormLocal)
    }

    def "jrubyStormLocal task should be a proper instance"() {
        expect:
        task instanceof JRubyStormLocal
    }

    def "the task should inherit the topology configured on the parent"() {
        given:
        JRubyStorm parent = project.task('spock-parent', type: JRubyStorm)
        parent.topology = 'foo.rb'

        when:
        task.parentTask = parent

        then:
        task.topology == parent.topology
    }

    def "I should be able to set a topology to run without a parent task"() {
        given:
        String topologyFile = 'topology.rb'

        when:
        task.topology = topologyFile

        then:
        task.topology == topologyFile
    }

    @Issue('https://github.com/jruby-gradle/jruby-gradle-storm-plugin/issues/12')
    def "default configuration should not be from JRubyExec"() {
        expect:
        task.configuration == JRubyStormLocal.DEFAULT_JRUBYSTORMLOCAL_CONFIG
    }

    def "setting the configuration should work"() {
        given:
        final String configName = 'someConfiguration'

        when:
        task.configuration configName

        then:
        task.configuration == configName
    }
}
