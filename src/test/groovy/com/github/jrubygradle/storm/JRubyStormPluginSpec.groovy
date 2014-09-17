package com.github.jrubygradle.storm

import org.gradle.api.Task
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import spock.lang.*


import static org.gradle.api.logging.LogLevel.LIFECYCLE
import static org.junit.Assert.assertTrue

/**
 * @author R. Tyler Croy
 *
 */
class JRubyStormPluginSpec extends Specification {

    def project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
    }

    def "Basic sanity check"() {
        expect:
            assert true
    }
}

