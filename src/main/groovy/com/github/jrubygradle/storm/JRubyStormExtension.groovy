package com.github.jrubygradle.storm

import org.gradle.api.Incubating

/**
 * Extension to provide the `project.storm` DSL into a Gradle project
 */
class JRubyStormExtension {
    @Incubating
    /** Default version of Storm supported and included */
    String defaultVersion = '0.9.2-incubating'

    @Incubating
    /** Default version of redstorm to use */
    String defaultRedstormVersion = '0.9.1'

    /** Set the Storm dependency version */
    void defaultVersion(Object stormVersion) {
        this.defaultVersion = stormVersion
    }

    /** Set the version of the Redstorm library to use */
    void defaultRedstormVersion(Object newVersion) {
        this.defaultRedstormVersion = newVersion
    }
}
