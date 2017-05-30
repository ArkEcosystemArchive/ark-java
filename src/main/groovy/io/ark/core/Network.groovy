package io.ark.core


class Network extends Object {
  String nethash
  String name
  int port
  byte prefix
  String version = "1.0"
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
      "5.39.9.250:4001",
      "5.39.9.251:4001",
      "5.39.9.252:4001",
      "5.39.9.253:4001",
      "5.39.9.254:4001",
      "5.39.9.255:4001",
      "5.39.53.48:4001",
      "5.39.53.49:4001",
      "5.39.53.50:4001",
      "5.39.53.51:4001",
      "5.39.53.52:4001",
      "5.39.53.53:4001",
      "5.39.53.54:4001",
      "5.39.53.55:4001",
      "37.59.129.160:4001",
      "37.59.129.161:4001",
      "37.59.129.162:4001",
      "37.59.129.163:4001",
      "37.59.129.164:4001",
      "37.59.129.165:4001",
      "37.59.129.166:4001",
      "37.59.129.167:4001",
      "37.59.129.168:4001",
      "37.59.129.169:4001",
      "37.59.129.170:4001",
      "37.59.129.171:4001",
      "37.59.129.172:4001",
      "37.59.129.173:4001",
      "37.59.129.174:4001",
      "37.59.129.175:4001",
      "193.70.72.80:4001",
      "193.70.72.81:4001",
      "193.70.72.82:4001",
      "193.70.72.83:4001",
      "193.70.72.84:4001",
      "193.70.72.85:4001",
      "193.70.72.86:4001",
      "193.70.72.87:4001",
      "193.70.72.88:4001",
      "193.70.72.89:4001",
      "193.70.72.90:4001"
    ]
  )

  static Network Devnet = new Network(
    nethash: '578e820911f24e039733b45e4882b73e301f813a0d2c31330dafda84534ffa23',
    prefix: 0x1e,
    port: 4002,
    name: 'devnet',
    peerseed: [
      "167.114.29.51:4002",
      "167.114.29.52:4002",
      "167.114.29.53:4002",
      "167.114.29.54:4002",
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
