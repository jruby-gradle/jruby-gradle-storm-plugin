package com.github.jrubygradle.storm

import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec

import com.github.jrubygradle.internal.JRubyExecTraits

/**
 * JRubyStormLocal is a task for invoking the redstorm local topology mode for
 * a given codebase
 */
class JRubyStormLocal extends JavaExec implements JRubyExecTraits {
    static final String DEFAULT_JRUBYSTORMLOCAL_CONFIG = 'jrubyStormLocal'
    /** parent from which this task will inherit some configuration */
    JRubyStorm parentTask

    @Input
    @Override
    String getConfiguration() {
        /* Prevent the usage of jrubyExec as our configuration which is never correct */
        if (JRubyExecTraits.super.getConfiguration() == JRubyExecUtils.DEFAULT_JRUBYEXEC_CONFIG) {
            return DEFAULT_JRUBYSTORMLOCAL_CONFIG
        }
        return JRubyExecTraits.super.getConfiguration()
    }

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
        description 'Execute the topology in "local" mode'
    }

    @Override
    void exec() {
        logger.info("JRubyStormLocal executing with parent (${parentTask}) and topology: ${topology}")
        super.setArgs(['local', topology])
        super.setEnvironment getPreparedEnvironment(environment)

        if (parentTask) {
            super.classpath parentTask.localConfiguration.asPath
        }

        prepareDependencies(project)
        super.exec()
    }
}
