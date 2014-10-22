package com.github.jrubygradle.storm

import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.Input
import org.gradle.api.Project


/**
 * @author R. Tyler Croy
 */
class JRubyStormLocal extends JavaExec {

  static void updateDependencies(Project project) {
    project.tasks.withType(JRubyStormLocal) { JRubyStormLocal t ->
      t.classpath project.configurations.jrubyStormLocal.asPath
    }
  }

  JRubyStormLocal() {
    super()
    super.setMain 'redstorm.TopologyLauncher'
  }

  @Override
  void exec() {
    super.setArgs(['local'] + getArgs())
    super.exec()
  }
}
