package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Implement the custom behaviors needed to build a JRubyStorm topology
 */
class JRubyStorm extends DefaultTask {
    static final String DEFAULT_CONFIGURATION_NAME = 'jrubyStorm'

    /** Dynamically created dependent task for running the topology in local mode*/
    private Task runTask
    /** Dynamically created dependent task for building the topology jar */
    private Task assembleTask

    private static final String REDSTORM_MAIN = 'redstorm.TopologyLauncher'
    private static final List<String> DEFAULT_EXCLUDES = ['*.sw*',
                                                  '*.gitkeep',
                                                  '*.md',
                                                  'META-INF/BCKEY*', ]

    /** Default version of redstorm to use */
    protected String customRedstormVersion
    /** Default version of Storm supported  included */
    protected String customStormVersion
    /** Configuration which has all of our dependencies */
    protected Configuration configuration


    /** Path (absolute or relative) to the Ruby file containing the topology */
    @Input
    String topology

    void stormVersion(String version) {
        this.customStormVersion = version
    }

    @Input
    @Optional
    String getStormVersion() {
        return customStormVersion ?: project.storm.defaultVersion
    }

    void setRedstormVersion(String version) {
        this.customRedstormVersion = version
    }

    @Input
    @Optional
    String getRedstormVersion() {
        return customRedstormVersion ?: project.storm.defaultRedstormVersion
    }

    void setConfiguration(Configuration newConfiguration) {
        this.configuration = newConfiguration
    }

    @Input
    @Optional
    Configuration getConfiguration() {
        return configuration ?: project.configurations.findByName(DEFAULT_CONFIGURATION_NAME)
    }

    JRubyStorm() {
        super()
        configuration = project.configurations.maybeCreate(DEFAULT_CONFIGURATION_NAME)
        this.group JRubyPlugin.TASK_GROUP_NAME
        this.runTask = this.createRunTask(this.project, this.name)
        this.assembleTask = this.createAssembleTask(this.project, this.name)

        project.afterEvaluate { this.updateDependencies() }
    }

    Configuration getLocalConfiguration() {
        project.configurations.maybeCreate("${configuration.name}Local")
    }

    void updateDependencies() {
        // Excluding storm-core for the configuration where we create the
        // topology jar. This is because the running storm cluster will provide
        // the classes from this dependency. If we attempt to includ ethis, the
        // skorm classes will not initialize properly and you'll get exceptions
        // like: "cannot load or initialize class backtype.storm.LocalCluster
        project.dependencies.add(configuration.name, "com.github.jruby-gradle:redstorm:${redstormVersion}") {
            exclude module: 'storm-core'
        }

        project.dependencies.add(localConfiguration.name, "org.apache.storm:storm-core:${stormVersion}")
        localConfiguration.extendsFrom configuration
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
