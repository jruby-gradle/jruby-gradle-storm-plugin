package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyExec

import org.gradle.api.tasks.Input
import org.gradle.api.Project


/**
 * JRubyStormLocal is a task for invoking the redstorm local topology mode for
 * a given codebase
 */
class JRubyStormLocal extends JRubyExec  {
    static final String REDSTORM_MAIN = 'redstorm.TopologyLauncher'

    /** Update all the tasks in the project of type JRubyStormLocal with the
     * appropriate classpath configuration
     */
    static void updateDependencies(Project project) {
        project.tasks.withType(JRubyStormLocal) { JRubyStormLocal t ->
            t.classpath project.configurations.jrubyStormLocal
        }
    }

    /** Path (relative or absolute) to the .rb file defining a Redstorm topology */
    @Input
    String topology

    JRubyStormLocal() {
        super()
        super.dependsOn project.tasks.jrubyPrepare
    }

    @Override
    void exec() {
        /* Skip over JRubyExec's setMain which is too restrictive */
        super.super.setMain REDSTORM_MAIN
        /* forcefully overwrite any previous JRuby args, this way we're certain
         * that we don't execute JRuby with -S or something like that
         */
        this.jrubyArgs = ['local', this.topology]
        super.exec()
    }
}
