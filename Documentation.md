# Library Specification

## Networks

### Constants

`Network.Mainnet` : Network

Provides a Network object that represents the ARK Mainnet

`Network.Devnet` : Network

Provides a Network object that represents the ARK Devnet

### Capabilities

* `GetHeaders()` : Map
* `warmup()` : Boolean
* `leftShift(Transaction)` : int
* `getRandomPeer()` : Peer

## Transactions

### Fields

`timestamp` : int

When the transaction took place

`recipientId` : String

The id of the recipient of the transaction

`amount` : long

Amount associated with the transaction

`fee` : long

Fee charged to send the transaction

`type` : TransactionType

Specifies if transaction is a normal, vote, or delegate creation

`vendorField` : String

Data to include with a transaction

`signature` : String

The signature using passphrase from sender

`signSignature` : String

The signature using passphrase from sender

`senderPublicKey` : String

The senders public key

`requesterPublicKey` : String

The receiver public key

`asset` : Map<String, Object>

TODO

`id` : String

ID of the transaction itself


### Capabilities

* `toBytes()` : byte[]
* `toObject()` : Map
* `sign(passphrase)` : String
* `secondSign(passphrase)` : String
* `toJson()` : String
* `fromJson(json)` : Transaction
* `createTransaction(recipientId, satoshiAmount, vendorField, passphrase)` : Transaction
* `createVote(ArrayList votes, passphrase)` : Transaction
* `createDelegate(String username, String passphrase` : Transaction
* `createSecondSignature(String secondPassphrase, String passphrase)` : Transaction

## TransactionTypes

### Defined Enums

* `TransactionType.NORAML` : 0
* `TransactionType.SECONDSIGNATURE` : 1
* `TransactionType.VOTE` : 2
* `TransactionType.DELEGATE` : 3

### Capabilities

* `getByteValue()` : Byte

Returns a byte representation of the enum value. 


## Accounts

### Fields

`address` : String

The address of the account

`publicKey` : String

The public key of the account

`balance` : long

The balance in the account

`username` : String

Username associated with this account

`vote` : long
 
The id who this account is voting for

`votes` : List

The id's of previous votes 

`rate` : int

TODO

### Capabilities

* `applyTransaction()` : boolean
* `undoTransaction()` : boolean
* `verifyTransaction()` : Verification

## Crypto

### Constants

`networkVersion` : Byte

### Capabilities

* `sign(Transaction t, String passphrase)` : ECKey.DCDSASignature
* `secondSign(Transaction t, String passphrase)` : ECKey.DCDSASignature
* `signBytes(bytes[] bytes, String passphrase)` : ECKey.DCDSASignature
* `verify(Transaction t)` : boolean
* `Secondverify(Transaction t, String secondPublicKeyHex)` : boolean
* `verifyBytes(bytes[] bytes, byte[] signature, byte[] publicKey)` : boolean
* `getBytes(Transaction t)` : byte[]
* `getId(Transaction t)` : String
* `getKeys(String passphrase)` : ECKey
* `getAddress(ECKey keys)` : String
* `getAddress(String publicKey)` : String

## Peers

### Constants

`ip` : String

`port` : int

`protocol` : String

`status` : String

### Capabilities

* `create(String string, networkHeaders = Network.Mainnet.headers)` : Peer
* `request(String method, String path, body = [:]))` : Future<Request>
* `getStatus()` : Map
* `postTransaction(Transaction transaction)` : Map
* `getPeers()` : Map
* `leftShift(Transaction transaction)` : Map

