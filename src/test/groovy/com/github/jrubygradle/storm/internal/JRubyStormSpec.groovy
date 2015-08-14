package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.storm.JRubyStormLocal
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.*

class JRubyStormSpec extends Specification {
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }
    def "createAssembleTask() should return a Jar type task"() {
        expect:
        JRubyStorm.createAssembleTask(project, 'spock') instanceof Jar
    }

    def "createRunTask() should return a JRubyStormLocal type task"() {
        given:
        Task task = project.task('spockParent', type: com.github.jrubygradle.storm.JRubyStorm)

        expect:
        JRubyStorm.createRunTask(project, 'spock', task) instanceof JRubyStormLocal
    }
}
