import org.bitcoinj.core.VersionedChecksummedBytes

/**
 * Allows other classes to reference a VersionedChecksummedBytes address associated with a network
 * without having to instantiate that Class since it is protected. Also allows cleaner representation of
 * which address is associated with which network
 */
class ArkAddress(network: Network, publicKey: ByteArray) : VersionedChecksummedBytes(network.prefix, publicKey)
