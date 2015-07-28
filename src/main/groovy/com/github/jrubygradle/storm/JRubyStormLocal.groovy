package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyExec

import org.gradle.api.tasks.Input
import org.gradle.api.Project


/**
 * JRubyStormLocal is a task for invoking the redstorm local topology mode for
 * a given codebase
 */
class JRubyStormLocal extends JRubyExec  {
    /** parent from which this task will inherit some configuration */
    JRubyStorm parentTask

    /** Set a custom path (relative or absolute) to the file defining a Redstorm topology
     *
     * If this is not set, the parentTask's topology will be used
     */
    String topology

    /** Path (relative or absolute) to the .rb file defining a Redstorm topology */
    @Input
    String getTopology() {
        return this.parentTask?.topology ?: topology
    }

    JRubyStormLocal() {
        super()
        super.dependsOn project.tasks.jrubyPrepare
    }

    @Override
    void exec() {
        /* Skip over JRubyExec's setMain which is too restrictive */
        super.super.setMain JRubyStorm.REDSTORM_MAIN

        /* forcefully overwrite any previous JRuby args, this way we're certain
         * that we don't execute JRuby with -S or something like that
         */
        this.jrubyArgs = ['local', this.topology]
        super.exec()
    }
}
