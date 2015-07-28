package com.github.jrubygradle.storm

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class JRubyStormTaskIntegrationSpec extends Specification {
    protected Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }

    @Ignore('not clear if this is the right direction')
    def "project should have a default jrubyStorm task"() {
        expect:
        project.tasks.findByName('jrubyStorm')
    }
}
