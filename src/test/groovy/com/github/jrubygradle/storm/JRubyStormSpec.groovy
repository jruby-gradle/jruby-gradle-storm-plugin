package com.github.jrubygradle.storm

import spock.lang.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class JRubyStormSpec extends Specification {
    protected Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }
}
