package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Implement the custom behaviors needed to build a JRubyStorm topology
 */
class JRubyStorm extends DefaultTask {
    /** Dynamically created dependent task for running the topology in local mode*/
    private Task runTask
    /** Dynamically created dependent task for building the topology jar */
    private Task assembleTask

    private static final String REDSTORM_MAIN = 'redstorm.TopologyLauncher'
    private static final List<String> DEFAULT_EXCLUDES = ['*.sw*',
                                                  '*.gitkeep',
                                                  '*.md',
                                                  'META-INF/BCKEY*',
                                                  ]

    /** Path (absolute or relative) to the Ruby file containing the topology */
    @Input
    String topology

    /** Default version of Storm supported and included */
    @Input
    @Optional
    String stormVersion = '0.9.2-incubating'

    /** Default version of redstorm to use */
    @Input
    @Optional
    String redstormVersion = '0.7.1'

    JRubyStorm() {
        super()
        this.group JRubyPlugin.TASK_GROUP_NAME
        this.runTask = this.createRunTask(this.project, this.name)
        this.assembleTask = this.createAssembleTask(this.project, this.name)
    }

    /**
     * Set the group of this task and delegate that Group assignment to child tasks
     *
     * @param newGroup group name
     */
    @Override
    void setGroup(String newGroup) {
        super.setGroup(newGroup)

        /* delegate group configuration */
        this.runTask?.group newGroup
        this.assembleTask?.group newGroup
    }

    private Task createRunTask(Project project, String baseName) {
        JRubyStormLocal runTask = project.task("run${prepareNameForSuffix(baseName)}",
                            type: JRubyStormLocal)
        runTask.parentTask = this
        return runTask
    }

    private Task createAssembleTask(Project project, String baseName) {
        return project.task("assemble${prepareNameForSuffix(baseName)}")
    }

    /**
     * Prepare a name for suffixing to a task name, i.e. with a baseName of
     * "foo" if I need a task to prepare foo, this will return 'Foo' so I can
     * make a "prepareFoo" task and it cases properly
     *
     * This method has a special handling for the string 'jruby' where it will
     * case it properly like "JRuby" instead of "Jruby"
     */
    private String prepareNameForSuffix(String baseName) {
        return baseName.replaceAll("(?i)jruby", 'JRuby').capitalize()
    }
}
