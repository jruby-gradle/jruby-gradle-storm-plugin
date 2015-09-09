package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Incubating
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import com.github.jrubygradle.storm.internal.JRubyStorm as JRubyStormInternal
import org.gradle.api.tasks.bundling.AbstractArchiveTask

/**
 * Implement the custom behaviors needed to build a JRubyStorm topology
 */
@Incubating
class JRubyStorm extends DefaultTask {
    static final String DEFAULT_CONFIGURATION_NAME = 'jrubyStorm'

    /** Dynamically created dependent task for running the topology in local mode*/
    private Task runTask
    /**
     * Dynamically created dependent task for building the topology jar
     */
    @Delegate
    AbstractArchiveTask assembleTask

    /** Default version of redstorm to use */
    protected String customRedstormVersion
    /** Default version of Storm supported  included */
    protected String customStormVersion
    /** Configuration which has all of our dependencies */
    protected Configuration configuration

    Task getAssembleTask() {
        return assembleTask
    }

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
        this.runTask = JRubyStormInternal.createRunTask(this.project, this)
        this.assembleTask = JRubyStormInternal.createAssembleTask(this.project, this)
        this.dependsOn assembleTask

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
        localConfiguration.extendsFrom project.configurations.findByName(JRubyStormPlugin.CLASSPATH_CONFIGURATION)
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
}
