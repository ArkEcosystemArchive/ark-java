# ark-java

[ ![Download](https://api.bintray.com/packages/singh/ark-io/ark-java/images/download.svg) ](https://bintray.com/singh/ark-io/ark-java/_latestVersion)
[![Build Status](https://travis-ci.org/Guppster/ark-java.svg?branch=master)](https://travis-ci.org/Guppster/ark-java)

Library for interacting with a [Ark](Ark.io) Blockchain using the JVM.

## Installation

#### Configure Bintray Repo

first you need to include one of these code blocks in your build system's config file in order to find the jar

_build.gradle_

    repositories {
        maven {
            url https://dl.bintray.com/singh/ark-io/
        }
    }
    
_pom.xml_

    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-singh-ark-io</id>
                    <name>bintray</name>
                    <url>https://dl.bintray.com/singh/ark-io</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-singh-ark-io</id>
                    <name>bintray-plugins</name>
                    <url>https://dl.bintray.com/singh/ark-io</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>

#### Include the Dependency

##### Gradle
    
_build.gradle_

    compile 'io.ark:ark-java:1.0.0'

##### Maven

_pom.xml_

    <dependency>
        <groupId>io.ark</groupId>
        <artifactId>ark-java</artifactId>
        <version>1.0.0</version>
        <type>pom</type>
    </dependency>

## Contributing

Contributions are greatly appreciated. Please refer to the CONTRIBUTING.md file before creating a pull request.
