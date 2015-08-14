package com.github.jrubygradle.storm

import spock.lang.*

class JRubyStormExtensionSpec extends Specification {
    protected JRubyStormExtension ext = new JRubyStormExtension()

    def "defaultVersion should give me a Storm version"() {
        expect:
        ext.defaultVersion
    }

    def "versionshould set the version"() {
        given:
        String version = '0.1.1'

        when:
        ext.defaultVersion version

        then:
        ext.defaultVersion == version

    }

    def "redstormVersion should give me a Redstorm version"() {
        expect:
        ext.defaultRedstormVersion
    }

    def "redstormVersion should set the redstorm version"() {
        given:
        String version = '0.1'

        when:
        ext.defaultRedstormVersion version

        then:
        ext.defaultRedstormVersion == version
    }
}
