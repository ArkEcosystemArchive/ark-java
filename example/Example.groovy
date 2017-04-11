@GrabResolver(name='ark-java', root='https://dl.bintray.com/arkecosystem/ark-java/')
@Grapes([
  @Grab('org.bitcoinj:bitcoinj-core:0.14.4'),
  @Grab('com.madgag.spongycastle:core:1.54.0.0'),
  @Grab('com.google.code.gson:gson:2.8.0'),
  @Grab('org.slf4j:slf4j-api:1.7.+'),
  @Grab('ch.qos.logback:logback-classic:1.+'),
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
