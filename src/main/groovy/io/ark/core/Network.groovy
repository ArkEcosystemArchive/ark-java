package io.ark.core

import groovy.util.logging.Slf4j
import groovyx.net.http.AsyncHTTPBuilder

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

@Slf4j
class Network extends Object {
  String nethash
  String name
  int port
  byte prefix
  String version = "1.0.1"
  int broadcastMax = 10
  List<String> peerListProviders = []
  List<String> peerseed = []
  List<Peer> peers = []
  AsyncHTTPBuilder httpBuilder

  static final long TIMEOUT = 30000
  static Random random = new Random()
  static Network Mainnet = new Network(
      nethash: '6e84d08bd299ed97c212c886c98a57e36545c8f5d645ca7eeae63a8bd62d8988',
      prefix: 0x17,
      port: 4001,
      name: 'mainnet',
      peerListProviders: [
          "https://node1.arknet.cloud/api/peers",
          "https://node2.arknet.cloud/api/peers"
      ],
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
      ],
      httpBuilder: new AsyncHTTPBuilder()
  )

  static Network Devnet = new Network(
      nethash: '578e820911f24e039733b45e4882b73e301f813a0d2c31330dafda84534ffa23',
      prefix: 0x1e,
      port: 4002,
      name: 'devnet',
      peerListProviders: [],
      peerseed: [
          "167.114.29.52:4002",
          "167.114.29.53:4002",
          "167.114.29.54:4002",
          "167.114.29.55:4002"
      ],
      httpBuilder: new AsyncHTTPBuilder())

  Map getHeaders() {
    [nethash: nethash, version: version, port: port]
  }

  public boolean warmup(int numberOfPeers = 20) {
    if (peers.size() > 0) return false

    httpBuilder.headers = getHeaders()

    getFreshPeers(numberOfPeers).each {
      verifyAndAddFreshPeer(it)
    }

    if(peers.isEmpty()) {
      def limit = (numberOfPeers < peerseed.size()) ? numberOfPeers : peerseed.size()
      peerseed.subList(0, limit).each {
        verifyAndAddSeedPeer(it)
      }
    }

    return true
  }

  private void verifyAndAddFreshPeer(it, boolean localhostAllowed = false) {
    log.debug("[$name] Fresh peer selected: ${it.ip}:${it.port}")
    def peer = new Peer(it.ip, it.port, this)
    boolean allowLocalhost = (it.ip == "127.0.0.1" && localhostAllowed) || (it.ip != "127.0.0.1")
    if (peer.isOk() && allowLocalhost) {
      peers << peer
    }
  }

  private void verifyAndAddSeedPeer(String it) {
    log.debug("[$name] Hardcoded seed peer selected: $it")
    def peer = Peer.create(it, this)
    if (peer.isOk()) {
      peers << peer
    }
  }

  List getFreshPeers(int limitResults = 20, long timeout = TIMEOUT) {
    Collections.shuffle(peerListProviders)
    for(url in peerListProviders) {
      try {
        def result = getFreshPeersFromUrl(url, limitResults, timeout)
        log.debug("URL for peers list selected: {}", url)
        return result
      } catch (Exception e) {
        log.debug("URL '$url' failed when trying to fetch peers")
      }
    }

    return []
  }

  List getFreshPeersFromUrl(String url, int limitResults, long timeout) {
    Future future = httpBuilder.request(url, GET, JSON) {
      response.success = { resp, json ->
        json.peers.sort { it.delay }
      }
    }

    future.get(timeout, TimeUnit.MILLISECONDS)
        .subList(0, limitResults)
  }

  // broadcast to many nodes
  public int leftShift(Transaction transaction) {
    [1..broadcastMax].each {
      getRandomPeer() << transaction
    }
    return broadcastMax
  }

  public Peer getRandomPeer() {
    peers[random.nextInt(peers.size())]
  }

  public Object get(String path, Map queryParams = [:]) {
    getRandomPeer().request(GET, path, queryParams).get(TIMEOUT, TimeUnit.MILLISECONDS)
  }

  public Object post(String path, body = [:]) {
    getRandomPeer().request(POST, path, body).get(TIMEOUT, TimeUnit.MILLISECONDS)
  }

}
