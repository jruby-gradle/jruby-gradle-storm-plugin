package com.github.jrubygradle.storm

import spock.lang.*

class JRubyStormExtensionSpec extends Specification {
    protected JRubyStormExtension ext = new JRubyStormExtension()

    def "version should give me a Storm version"() {
        expect:
        ext.version
    }

    def "versionshould set the version"() {
        given:
        String version = '0.1.1'

        when:
        ext.version version

        then:
        ext.version == version

    }

    def "redstormVersion should give me a Redstorm version"() {
        expect:
        ext.redstormVersion
    }

    def "redstormVersion should set the redstorm version"() {
        given:
        String version = '0.1'

        when:
        ext.redstormVersion version

        then:
        ext.redstormVersion == version
    }
}
