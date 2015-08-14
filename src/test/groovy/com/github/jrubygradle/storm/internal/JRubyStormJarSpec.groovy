package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.jar.JRubyJar
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.*

class JRubyStormJarSpec extends Specification {
    def "when constructing the task"() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'

        expect: "the task to be a JRubyJar"
        project.task('spock', type: JRubyStormJar) instanceof JRubyJar
    }
}
