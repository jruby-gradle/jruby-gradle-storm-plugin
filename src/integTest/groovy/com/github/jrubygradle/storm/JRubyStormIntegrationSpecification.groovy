package com.github.jrubygradle.storm

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.*

/**
 * Integration testing base class for common integration testing behaviors
 */
class JRubyStormIntegrationSpecification extends Specification {
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

    /**
     * Apply the necessary plugin configuration to integration test a Gradle build
     * with the given <pre>build</pre> {@code File}
     *
     * @param build Temporary file representing the build.gradle
     */
    void applyPluginTo(File build) {
        build << """
buildscript {
    dependencies {
        classpath files(${pluginDependencies})
    }
}
apply plugin: 'com.github.jruby-gradle.storm'

repositories {
    jcenter()
}

"""
    }

}
