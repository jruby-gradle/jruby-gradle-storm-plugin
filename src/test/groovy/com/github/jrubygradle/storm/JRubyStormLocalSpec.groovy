package com.github.jrubygradle.storm

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
}

