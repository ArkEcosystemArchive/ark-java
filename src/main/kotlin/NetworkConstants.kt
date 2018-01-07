/**
 * Contains constants for Ark official networks and provides a template describing how others can
 * create their own [Network] instances
 */
object NetworkConstants
{
    val mainnet = Network(nethash = "6e84d08bd299ed97c212c886c98a57e36545c8f5d645ca7eeae63a8bd62d8988",
            prefix = 0x17,
            port = 4001,
            name = "mainnet",
            peerseed = listOf(
                    "5.39.9.240:4001",
                    "5.39.9.241:4001",
                    "5.39.9.242:4001",
                    "5.39.9.243:4001",
                    "5.39.9.244:4001",
                    "5.39.9.250:4001",
                    "5.39.9.251:4001",
                    "5.39.9.252:4001",
                    "5.39.9.253:4001",
                    "5.39.9.254:4001",
                    "5.39.9.255:4001",
                    "5.39.53.48:4001",
                    "5.39.53.49:4001",
                    "5.39.53.50:4001",
                    "5.39.53.51:4001"
            ),
            peerListProviders = listOf(
                    "https://node1.arknet.cloud",
                    "https://node2.arknet.cloud"
            ))

    val devnet = Network(nethash = "578e820911f24e039733b45e4882b73e301f813a0d2c31330dafda84534ffa23",
            name = "devnet",
            port = 4002,
            prefix = 0x1e,
            peerseed = listOf(
                    "167.114.29.52:4002",
                    "167.114.29.53:4002",
                    "167.114.29.54:4002",
                    "167.114.29.55:4002"),
            peerListProviders = listOf())
}