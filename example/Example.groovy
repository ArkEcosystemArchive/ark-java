#!/usr/bin/env groovy
@GrabResolver(name='ark-java', root='https://dl.bintray.com/arkecosystem/ark-java/')
@Grab('io.ark.lite:client:0.2')
import io.ark.core.*

// grab mainnet network settings and warm it up
def mainnet = Network.Mainnet
mainnet.warmup()

//provision I/O with console
def console = System.console()
sc = new Scanner(console.reader)

def ask(sentence){
  println sentence
  def out = sc.nextLine()
  println ""
  out
}

try {
  String address = ask "Hi there! Which address are you sending to?"
  long amount = ask("Alright! How much ARK do you want to send to $address?") as long
  long satoshiamount = amount * 100000000l

  println "Got it! Please put your passphrase here:"
  String passphrase = String.valueOf(console.readPassword("[%s]", "Passphrase"))
  println ""

  // create a transaction
  def transaction = Transaction.createTransaction(address, satoshiamount, null, passphrase)

  def answer = ask "Everything in order now.\nDo you want to send $amount ARK to $address now? y/N"

  if(answer == "y" || answer == "Y"){
    // Post transaction to a peer
    result = mainnet.randomPeer << transaction

    if(result.success){
      println "Transaction sent with success!"
      // broadcast transaction to several peers on mainnet
      mainnet << transaction
    }
    else{
      println "Transaction not accepted by network:"
      println result
    }
  }
  else {
    println "Aborted."
  }
} catch(error) {
  println "Error: ${error}"
  println "Aborted."
}
println ""
println "Good bye and see you soon!"
System.exit(0)
