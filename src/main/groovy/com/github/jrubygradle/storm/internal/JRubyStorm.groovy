package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.storm.JRubyStormLocal
import org.gradle.api.Project
import org.gradle.api.Task


class JRubyStorm {
    static Task createAssembleTask(Project project, Task parent) {
        JRubyStormJar task = project.task("assemble${prepareNameForSuffix(parent.name)}",
                type: JRubyStormJar)
        task.parentTask = parent
        return task
    }

    /**
     * Prepare a name for suffixing to a task name, i.e. with a baseName of
     * "foo" if I need a task to prepare foo, this will return 'Foo' so I can
     * make a "prepareFoo" task and it cases properly
     *
     * This method has a special handling for the string 'jruby' where it will
     * case it properly like "JRuby" instead of "Jruby"
     */
    static String prepareNameForSuffix(String baseName) {
        return baseName.replaceAll("(?i)jruby", 'JRuby').capitalize()
    }

    static Task createRunTask(Project project, Task parent) {
        JRubyStormLocal runTask = project.task("run${prepareNameForSuffix(parent.name)}",
                type: JRubyStormLocal)
        runTask.parentTask = parent
        return runTask
    }
}