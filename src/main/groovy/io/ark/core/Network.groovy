package io.ark.core


class Network extends Object {
  String nethash
  String name
  int port
  byte prefix
  String version = "1.0.1"
  int broadcastMax = 10
  List<String> peerseed = []
  List<Peer> peers = []

  static Random random = new Random()
  static Network Mainnet = new Network(
    nethash: '6e84d08bd299ed97c212c886c98a57e36545c8f5d645ca7eeae63a8bd62d8988',
    prefix: 0x17,
    port: 4001,
    name: 'mainnet',
      peerseed: [
      "5.39.9.240:4001",
      "5.39.9.241:4001",
      "5.39.9.242:4001",
      "5.39.9.243:4001",
      "5.39.9.244:4001",
      "5.39.9.245:4001",
      "5.39.9.246:4001",
      "5.39.9.247:4001",
      "5.39.9.248:4001",
      "5.39.9.249:4001",
      "5.39.9.250:4001",
      "5.39.9.251:4001",
      "5.39.9.252:4001",
      "5.39.9.253:4001",
      "5.39.9.254:4001",
      "5.39.9.255:4001"
    ]
  )

  static Network Devnet = new Network(
    nethash: '578e820911f24e039733b45e4882b73e301f813a0d2c31330dafda84534ffa23',
    prefix: 0x1e,
    port: 4002,
    name: 'devnet',
    peerseed: [
      "167.114.29.52:4002",
      "167.114.29.53:4002",
      "167.114.29.55:4002"
    ])

  public Map getHeaders(){
    [nethash:nethash, version:version, port:port]
  }

  public boolean warmup(){
    if(peers.size()>0) return false
    peerseed.each {
      peers << Peer.create(it, this.headers)
    }
    return true
  }

  // broadcast to many nodes
  public int leftShift(Transaction transaction){
    [1..broadcastMax].each {
      getRandomPeer() << transaction
    }
    return broadcastMax
  }

  public Peer getRandomPeer(){
    peers[random.nextInt(peers.size())]
  }
}
