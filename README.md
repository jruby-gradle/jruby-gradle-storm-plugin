jruby-gradle-storm-plugin
=========================

[![Build Status](https://buildhive.cloudbees.com/job/jruby-gradle/job/jruby-gradle-war-plugin/badge/icon)](https://buildhive.cloudbees.com/job/jruby-gradle/job/jruby-gradle-storm-plugin/) [![Gitter chat](https://badges.gitter.im/jruby-gradle/jruby-gradle-plugin.png)](https://gitter.im/jruby-gradle/jruby-gradle-plugin)

JRuby Gradle plugin to manage creating Storm topology jars

## Usage

**NOTE:** This plugin is stili in a very early stages.

Including the plugin:

```groovy
buildscript {
    repositories { jcenter() }
    dependencies {
        classpath 'com.github.jruby-gradle:jruby-gradle-storm-plugin:0.1.3+'
    }
}  
```

### Running a "local" topology

The `JRubyStormLocal` task provides the underlying machinery to run a
[redstorm](https://github.com/jruby-gradle/redstorm) topology in "local" mode:

```groovy
import com.github.jrubygradle.storm.JRubyStormLocal

task runLocalTopology(type: JRubyStormLocal) {
  // path is considered relative to the working directory
  // the task is executed in
  topology 'topologies/example_topology.rb'
}
```

Then you can run the local topology with: `./gradlew runLocalTopology`

**Note:** The Gradle script will block until the local topology is terminated
(Ctrl+C)

### Creating a topology .jar

Creating a topology `.jar` file is useflu when you plan on uploading your
topology to a running Storm cluster. *Currently* this plugin will only create
that jar for you, it will not upload it.

By default the plugin will include `bolts/` and `/topologies/` in the generated
`.jar` file.

 * `./gradlew jrubyStorm` will create a `.jar` in `build/libs/`
