package com.github.jrubygradle.storm


/**
 *
 */
class JRubyStormExtension {
    /** Default version of Storm supported and included */
    String version = '0.9.2-incubating'

    /** Set the Storm dependency version */
    void version(Object stormVersion) {
        this.version = stormVersion
    }
}
