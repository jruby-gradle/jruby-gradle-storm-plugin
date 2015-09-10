package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.jar.JRubyJar
import com.github.jrubygradle.storm.JRubyStormPlugin
import groovy.transform.InheritConstructors

import com.github.jrubygradle.storm.JRubyStorm

@InheritConstructors
class JRubyStormJar extends JRubyJar {
    static final String REDSTORM_MAIN = 'redstorm.TopologyLauncher'
    private static final List<String> DEFAULT_EXCLUDES = ['*.sw*',
                                                          '*.gitkeep',
                                                          '*.md',
                                                          'META-INF/BCKEY*', ]
    /** parent from which this task will inherit some configuration */
    JRubyStorm parentTask

    /**
     * Return the version of JRuby safe for usage in redstorm
     */
    @Override
    String getJrubyVersion() {
        final String inheritedVersion = super.getJrubyVersion()

        /* if our parent has a default version that's 1.7.x, use it */
        if (inheritedVersion.matches(/1.7.(\d+)/)) {
            return inheritedVersion
        }
        /*  Default to 1.7.22 <https://github.com/jruby-gradle/redstorm/issues/11> */
        return '1.7.22'
    }

    @Override
    String getMainClass() {
        return REDSTORM_MAIN
    }

    @Override
    String getConfiguration() {
        if (parentTask) {
            return parentTask.configuration.name
        }
        return JRubyStorm.DEFAULT_CONFIGURATION_NAME
    }

    JRubyStormJar() {
        super()
        appendix = ''
        description 'Package a Storm topology jar'

        project.afterEvaluate {
            this.includeRedstorm()
            this.includeTopology()
            this.includeClasspathDependencies()
        }
    }

    void includeRedstorm() {
        from {
            File redstorm = this.parentTask.configuration.find {
                it.name.matches(/redstorm-(.*).jar/)
            }
            project.zipTree(redstorm)
        }
    }

    void includeTopology() {
        if (parentTask.topology) {
            into('') { from parentTask.topology }
        }
    }

    void includeClasspathDependencies() {
        project.configurations.findByName(JRubyStormPlugin.CLASSPATH_CONFIGURATION).files.each { File f ->
            if (f.name.endsWith(".jar")) {
                from { project.zipTree(f) }
            }
        }
    }
}
