package com.github.jrubygradle.storm

import com.github.jrubygradle.JRubyPlugin
import org.gradle.api.tasks.Input

//import com.github.jrubygradle.jar.JRubyJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/**
 * Implement the custom behaviors needed to build a JRubyStorm topology
 */
class JRubyStorm extends ShadowJar {
    static final String REDSTORM_MAIN = 'redstorm.TopologyLauncher'
    static final List<String> DEFAULT_EXCLUDES = ['*.sw*',
                                                  '*.gitkeep',
                                                  '*.md',
                                                  'META-INF/BCKEY*',
                                                  ]
    @Input
    String topology

    JRubyStorm() {
        super()
        this.group JRubyPlugin.TASK_GROUP_NAME
        this.dependsOn this.project.tasks.jrubyPrepare
        this.exclude DEFAULT_EXCLUDES
        this.mainClass  REDSTORM_MAIN
    }
}
