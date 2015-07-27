package com.github.jrubygradle.storm


/**
 *
 */
class JRubyStormExtension {
    /** Default version of Storm supported and included */
    String version = '0.9.2-incubating'

    /** Default version of redstorm to use */
    String redstormVersion = '0.7.1'

    /** Set the Storm dependency version */
    void version(Object stormVersion) {
        this.version = stormVersion
    }

    /** Set the version of the Redstorm library to use */
    void redstormVersion(Object newVersion) {
        this.redstormVersion = newVersion
    }
}
