# Blackboard Building Block Logging Utilities #
A Java library containing utility classes to assist with logging in 
Blackboard. At the moment it only contains two classes, both are 
[Logback PropertyDefiners](http://logback.qos.ch/manual/configuration.html#definingPropsOnTheFly)
used to get your Building Block's logs or config directory.

## Building

The build tool used is called [Gradle](http://www.gradle.org/). It 
will do all the work to build the library, including downloading 
dependencies, compiling java classes and zipping up the JAR. This is all
done with a single command - no installation necessary. From the root of
the project execute the following command:

**Windows**: `gradlew build`

**GNU/Linux & Mac OSX**: `./gradlew build`

The built file will be output to the 
`build/libs/b2-logging-utils-[version].jar` location.

## Cleaning

If you want to clean the build artifacts execute this command:

**Windows**: `gradlew clean`

**GNU/Linux & Mac OSX**: `./gradlew clean`

## Using Logback to write to your Building Block's log directory
(This assumes you are already using Logback in your project.)
 - Add the JAR as a dependency in your project. The steps to do this
will vary depending upon your build tool. In gradle, this can be done
by adding the following line to the dependencies: 

```
compile files('path/to/jar/b2-logging-utils-[version].jar)
```

 - Add the following XML to your logback configuration file:
 
 ```XML
<define name="pluginLogDir" class="com.alltheducks.logging.logback.BbPluginLogDirPropertyDefiner">
    <vendorId>me</vendorId>
    <handle>myplugin</handle>
</define>
 ```
 
 - Add the following XML to your B2 permissions in the bb-manifest.xml 
 (unfortunately, at this time, write permissions are required to the 
 plugins directory simply so that Logback can create the subdirectory
 for your plugin.):

```XML
<permission type="java.io.FilePermission" name="BB_HOME/logs/plugins/-" actions="write" />
```

 - Now you can use the `${pluginLogDir}` property to configure your 
 appenders how ever you like. [Blackboard has a prescribed format for
 logging in their SaaS environment](https://community.blackboard.com/docs/DOC-1595). 
 It probably also a good idea to have a new log file each day. Here is an example
 which brings this all together:
 
```XML
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <define name="pluginLogDir" class="com.alltheducks.logging.logback.BbPluginLogDirPropertyDefiner">
        <vendorId>me</vendorId>
        <handle>myplugin</handle>
    </define>

    <appender name="default" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${pluginLogDir}/me-myplugin.log</file>
        <append>true</append>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${pluginLogDir}/me-myplugin.%d{yyyy-MM-dd}.log</fileNamePattern>
        </rollingPolicy>
        <encoder>
            <!-- This pattern ensures logging will work in Learn SaaS -->
            <pattern>%date{yyyy-MM-dd HH:mm:ss} | %-5level | %-45(%L:%logger{40}) | %m%n%ex{10}</pattern>
        </encoder>
    </appender>
    <logger name="com.alltheducks.myplugin" level="DEBUG"/>
    <root level="WARN">
        <appender-ref ref="default"/>
    </root>
</configuration>
```

## Writing to your Building Block's config directory:
Occasionally, you may wish to write some logs to your Building Block's config
directory. This is rare and should be thought through beforehand, but if you
want to achieve this use the `com.alltheducks.logging.logback.BbPluginConfigDirPropertyDefiner`
instead.