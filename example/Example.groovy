@GrabResolver(name='ark-java', root='https://dl.bintray.com/arkecosystem/ark-java/')
@Grab('io.ark.lite:client:0.2')

import io.ark.core.*

// create a transaction
def transaction = Transaction.createTransaction("AXoXnFi4z1Z6aFvjEYkDVCtBGW2PaRiM25", 133380000000, "This is first transaction from JAVA", "this is a top secret passphrase")

// grab mainnet network settings and warm it up
def mainnet = Network.Mainnet
mainnet.warmup()

// Post transaction to a peer
def peer = mainnet.randomPeer
println peer << transaction

// broadcast transaction to several peers on mainnet
println mainnet << transaction

System.exit(0)
