package com.github.jrubygradle.storm.internal

import com.github.jrubygradle.jar.JRubyJar
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

    String mainClass = REDSTORM_MAIN
    /* Default to 1.7.21 <https://github.com/jruby-gradle/redstorm/issues/11> */
    String jrubyVersion = '1.7.21'

    @Override
    String getConfiguration() {
        return parentTask.configuration.name
    }

    JRubyStormJar() {
        super()
        appendix = ''

        project.afterEvaluate {
            this.includeRedstorm()
            this.includeTopology()
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
}
