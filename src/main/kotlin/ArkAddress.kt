import org.bitcoinj.core.VersionedChecksummedBytes

class ArkAddress(network: Network, publicKey: ByteArray) : VersionedChecksummedBytes(network.prefix, publicKey)
