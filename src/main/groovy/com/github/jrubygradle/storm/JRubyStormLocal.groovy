package com.github.jrubygradle.storm

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec

import com.github.jrubygradle.internal.JRubyExecTraits

/**
 * JRubyStormLocal is a task for invoking the redstorm local topology mode for
 * a given codebase
 */
class JRubyStormLocal extends JavaExec implements JRubyExecTraits {
    /** parent from which this task will inherit some configuration */
    JRubyStorm parentTask

    /** Set a custom path (relative or absolute) to the file defining a Redstorm topology
     *
     * If this is not set, the parentTask's topology will be used
     */
    protected String customTopology

    /** Path (relative or absolute) to the .rb file defining a Redstorm topology */
    @Input
    String getTopology() {
        return customTopology ?: parentTask?.topology
    }

    void setTopology(String topology) {
        this.customTopology = topology
    }

    JRubyStormLocal() {
        super()
        super.setMain 'redstorm.TopologyLauncher'
    }

    @Override
    void exec() {
        logger.info("JRubyStormLocal parentTask: ${parentTask} (${parentTask.topology})")
        super.setArgs(['local', topology])
        super.setEnvironment getPreparedEnvironment(environment)

        if (parentTask) {
            super.classpath parentTask.localConfiguration.asPath
        }

        prepareDependencies(project)
        super.exec()
    }
}
