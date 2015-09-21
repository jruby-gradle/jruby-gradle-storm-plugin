package com.github.jrubygradle.storm

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.BuildResult
import spock.lang.*

/**
 */
class JRubyStormLocalIntegrationSpec extends JRubyStormIntegrationSpecification {
    def "executing runJRubyStorm with no topology should error"() {
        given:
        applyPluginTo(buildFile)
        buildFile << "jrubyStorm { topology 'foo.rb' }"

        when:
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('runJRubyStorm')
                .buildAndFail()

        then:
        result.task(":runJRubyStorm").outcome == TaskOutcome.FAILED
    }

    File createHelloWorldTopology() {
        File topologyFile = testProjectDir.newFile('topo.rb')
        topologyFile << """
require 'red_storm'

class HelloWorldSpout < RedStorm::DSL::Spout
  on_init {@words = ["hello", "world"]}
  on_send {@words.shift unless @words.empty?}
end

class HelloWorldBolt < RedStorm::DSL::Bolt
  on_receive :emit => false do |tuple|
    puts tuple
  end
end

class HelloWorldTopology < RedStorm::DSL::Topology
  spout HelloWorldSpout do
    output_fields :word
  end

  bolt HelloWorldBolt do
    source HelloWorldSpout, :global
  end

  configure do
    debug false
    max_task_parallelism 4
    num_workers 1
    max_spout_pending 1000
  end

  on_submit do
    Thread.start {
        sleep 20
        cluster.shutdown
    }
  end
end
"""
        return topologyFile
    }

    def "running in local mode with a basic topology"() {
        given:
        applyPluginTo(buildFile)
        File topo = createHelloWorldTopology()
        buildFile << "jrubyStorm { topology '${topo.absolutePath}' }"

        when: 'runJRubyStorm is invoked'
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('runJRubyStorm')
                .build()

        then: 'the task should succeed'
        result.task(":runJRubyStorm").outcome == TaskOutcome.SUCCESS

        and: "it should have logged hello world"
        result.standardOutput.contains('{"word"=>"hello"}')
        result.standardOutput.contains('{"word"=>"world"}')
    }

    @Issue('https://github.com/jruby-gradle/jruby-gradle-storm-plugin/issues/29')
    def "JRubyStormLocal tasks should not require a parent task"() {
        given:
        applyPluginTo(buildFile)
        File topo = createHelloWorldTopology()

        buildFile << """
import com.github.jrubygradle.storm.JRubyStormLocal
task run(type: JRubyStormLocal) {
    topology '${topo.absolutePath}'
}
"""

        when: 'the run task is invoked'
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('run')
                .build()

        then: 'the task should succeed'
        result.task(":run").outcome == TaskOutcome.SUCCESS
    }
}
