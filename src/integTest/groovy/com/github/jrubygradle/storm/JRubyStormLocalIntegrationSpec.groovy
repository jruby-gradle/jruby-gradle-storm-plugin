package com.github.jrubygradle.storm

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.*

/**
 */
class JRubyStormLocalIntegrationSpec extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    String pluginDependencies

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.json")

        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        pluginDependencies = pluginClasspathResource.text
    }

    def "executing runJRubyStorm with no topology should error"() {
        given:
        buildFile << """
buildscript {
    dependencies {
        classpath files(${pluginDependencies})
    }
}
apply plugin: 'com.github.jruby-gradle.storm'

repositories {
    jcenter()
    mavenLocal()
}

jrubyStorm {
}
    """

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('runJRubyStorm')
                .build()

        then:
        result.task(":runJRubyStorm").outcome == TaskOutcome.FAILURE
    }
}
