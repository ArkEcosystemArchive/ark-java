# ark-java

[ ![Download](https://api.bintray.com/packages/arkecosystem/ark-java/ark-java/images/download.svg) ](https://bintray.com/arkecosystem/ark-java/ark-java/_latestVersion)
[![Build Status](https://travis-ci.org/ArkEcosystem/ark-java.svg?branch=master)](https://travis-ci.org/ArkEcosystem/ark-java)

Library for interacting with an [Ark Ecosystem](https://ark.io) Blockchain using the JVM.

## Installation

You must add a Bintray repository in order to retrieve the dependency:

#### build.gradle
```
    repositories {
        maven {
            url https://dl.bintray.com/arkecosystem/ark-java/
        }
    }
```

#### pom.xml
```xml
    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>ark-java</id>
                    <name>ark-java</name>
                    <url>https://dl.bintray.com/arkecosystem/ark-java/</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>ark-io</id>
                    <name>plugins</name>
                    <url>https://dl.bintray.com/arkecosystem/ark-java</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
```

Include the dependency in your project:

#### build.gradle
    
```json
    compile 'io.ark:ark-java:1.0.0'
```

#### pom.xml

```xml
    <dependency>
        <groupId>io.ark</groupId>
        <artifactId>ark-java</artifactId>
        <version>1.0.0</version>
        <type>pom</type>
    </dependency>
```

## Authors
- FX Thoorens <fx@ark.io>
- Guppster <http://www.gurpreetsingh.me>
- dafty <https://dafty.net>

## License

The MIT License (MIT)

Copyright (c) 2017-2018 ARK.io<br />

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.