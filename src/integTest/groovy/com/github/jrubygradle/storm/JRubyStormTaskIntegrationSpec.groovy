package com.github.jrubygradle.storm

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.BuildResult
import org.gradle.api.artifacts.Dependency
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.*


/** Integration tests which actually execute Gradle via the GradleTestKit */
class JRubyStormTestKitSpec extends Specification {
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

    def "executing the assemble task produces a jar artifact"() {
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
                .withArguments('assembleJRubyStorm')
                .build()

        then:
        File[] artifacts = (new File(testProjectDir.root, ['build', 'libs'].join(File.separator))).listFiles()
        artifacts && artifacts.size() == 1

        and:
        result.task(":assembleJRubyStorm").outcome == TaskOutcome.SUCCESS
    }
}