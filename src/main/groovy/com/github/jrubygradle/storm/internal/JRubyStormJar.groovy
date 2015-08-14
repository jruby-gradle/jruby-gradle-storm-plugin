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

    JRubyStormJar() {
        super()
        appendix = ''
    }

    @Override
    void copy() {
        if (parentTask) {
            File redstorm = parentTask.configuration.find {
                it.name.matches(/redstorm-(.*).jar/)
            }
            from { project.zipTree(redstorm) }
        }
        if (parentTask.topology) {
            into('') { from parentTask.topology }
        }
        super.copy()
    }
}
