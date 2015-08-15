package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.jar.JRubyJar
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.*

class JRubyStormJarSpec extends Specification {
    Project project

    def setup()  {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }
    def "when constructing the task"() {
        given:

        expect: "the task to be a JRubyJar"
        project.task('spock', type: JRubyStormJar) instanceof JRubyJar
    }

    def "mainClass should be the TopologyLauncher"() {
        given:
        Jar task = project.task('spock', type: JRubyStormJar)

        expect:
        task.mainClass == JRubyStormJar.REDSTORM_MAIN
    }

    def "appendix should be empty" () {
        given:
        Jar task = project.task('spock', type: JRubyStormJar)

        expect: "that it has no special appendix in the filename"
        task.appendix == ''
    }
}
