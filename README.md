# ark-java
:coffee: Lite client library in Java

[ ![Download](https://api.bintray.com/packages/arkecosystem/ark-java/client/images/download.svg) ](https://bintray.com/arkecosystem/ark-java/client/_latestVersion)

# Authors
FX Thoorens fx@ark.io


# Example
## Using Java
- use maven repository `https://dl.bintray.com/arkecosystem/ark-java/`
- add `import io.ark.*`

### maven pom
Add this under config `<dependencies>`
```
<dependency>
  <groupId>io.ark.lite</groupId>
  <artifactId>client</artifactId>
  <version>0.3</version>
  <scope>compile</scope>
</dependency>
```

### gradle dependencies
add this line under `dependencies`
`compile io.ark.lite:client:0.3`

See an example gradle app https://github.com/arkecosystem/ark-java-example

## Using Groovy
Install groovy http://groovy-lang.org/install.html

Example:
```
@GrabResolver(name='ark-java', root='https://dl.bintray.com/arkecosystem/ark-java/')
@Grab('io.ark.lite:client:0.3')

import io.ark.core.*

// grab mainnet network settings and warm it up
def mainnet = Network.Mainnet
mainnet.warmup()

// create a transaction
def transaction = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")

// Post transaction to a peer
def peer = mainnet.randomPeer
println peer << transaction

// broadcast transaction to several peers on mainnet
println mainnet << transaction
```

Try from source:
`groovy example/Example.groovy`

# License

The MIT License (MIT)

Copyright (c) 2017 Ark

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
