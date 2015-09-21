package com.github.jrubygradle.storm

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.BuildResult
import spock.lang.*


/** Integration tests which actually execute Gradle via the GradleTestKit */
class JRubyStormTestKitSpec extends JRubyStormIntegrationSpecification {
    def "executing the task without a topolgoy should error"() {
        given:
        applyPluginTo(buildFile)
        buildFile << 'jrubyStorm { }'

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('jrubyStorm')
                .buildAndFail()

        then:
        result.task(":jrubyStorm").outcome == TaskOutcome.FAILED
    }

    def "executing the assemble task produces a jar artifact"() {
        given:
        applyPluginTo(buildFile)
        buildFile << """
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