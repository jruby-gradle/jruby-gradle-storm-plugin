package com.github.jrubygradle.storm


/**
 *
 */
class JRubyStormExtension {
    /** Default version of Storm supported and included */
    String defaultVersion = '0.9.2-incubating'

    /** Default version of redstorm to use */
    String defaultRedstormVersion = '0.7.2'

    /** Set the Storm dependency version */
    void defaultVersion(Object stormVersion) {
        this.defaultVersion = stormVersion
    }

    /** Set the version of the Redstorm library to use */
    void defaultRedstormVersion(Object newVersion) {
        this.defaultRedstormVersion = newVersion
    }
}
