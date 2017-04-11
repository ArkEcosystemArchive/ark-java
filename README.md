# ark-java
:coffee: Lite client library in Java


# Authors
FX Thoorens fx@ark.io


# Example
## Using Java
- use maven repository `https://dl.bintray.com/arkecosystem/ark-java/`
- use package group:io.ark.lite, artifactId:client, version:0.1
- add `inmport io.ark.*`

### maven pom
Add this under config `<dependencies>`
```
<dependency>
  <groupId>io.ark.lite</groupId>
  <artifactId>client</artifactId>
  <version>0.1</version>
  <scope>compile</scope>
</dependency>
```

### gradle dependencies
add this line under `dependencies`
`compile io.ark.lite:client:0.1`

See an example gradle app https://github.com/arkecosystem/ark-java-example

## Using Groovy
Install groovy http://groovy-lang.org/install.html

See example/Example.groovy:
```
@GrabResolver(name='ark-java', root='https://dl.bintray.com/arkecosystem/ark-java/')
@Grapes([
  @Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7'),
  @Grab('io.ark.lite:client:0.1')
])

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.JSON
import io.ark.*

//connect to one mainnet node
def http = new HTTPBuilder('https://node1.arknet.cloud')
def mainnethearders = [version:'0.1', port:1, nethash:'6e84d08bd299ed97c212c886c98a57e36545c8f5d645ca7eeae63a8bd62d8988']

// retrieve a list of peers in network
http.request(GET, JSON) {
  uri.path = '/peer/list'
  headers << mainnethearders

  response.success = { resp, json ->
    println "Network Peers"
    json.peers.each {
      println " ${it.ip}:${it.port} -> ${it.status}"
    }
  }
}

// create a transaction
def transaction = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")

// POST the transaction
http.request(POST, JSON) {
  uri.path = '/peer/transactions'
  headers << mainnethearders
  body = [transactions: [transaction]]

  response.success = { resp, json ->
    println "Result after posting transaction:"
    if(json.success){
      println "SUCCESS"
    }
    else {
      println "FAIL - ${json.message}: ${json.error}"
    }

  }
}
```

then run:
`groovy example/Example.groovy`

# License

The MIT License (MIT)

Copyright (c) 2017 Ark

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
