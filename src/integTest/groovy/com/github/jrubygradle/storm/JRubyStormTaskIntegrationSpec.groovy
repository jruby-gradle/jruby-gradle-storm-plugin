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

/**
 */
class JRubyStormTaskIntegrationSpec extends Specification {
    def "evaluation of the project should result in an assemble and run task"() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'

        when:
        project.evaluate()

        then:
        project.tasks.findByName('assembleJRubyStorm')
        project.tasks.findByName('runJRubyStorm')
    }

    def "evaluation of the project should result in dependencies being added to the configuration"() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
        JRubyStorm task = project.task('spock', type: JRubyStorm)
        def deps = task.configuration.dependencies

        when:
        project.evaluate()

        then:
        deps.matching { Dependency d -> d.name == 'redstorm' }
    }

    def "evaluation of the project should result in local mode dependencies"() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.github.jruby-gradle.storm'
        JRubyStorm task = project.task('spock', type: JRubyStorm)

        when:
        project.evaluate()

        then:
        project.configurations.findByName('jrubyStormLocal')?.dependencies?.matching {
            it.name == 'storm-core'
        }
    }
}

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