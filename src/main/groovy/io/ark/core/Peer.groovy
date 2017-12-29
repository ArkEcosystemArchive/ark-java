package io.ark.core

import groovy.util.logging.Slf4j
import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.Method

import java.util.concurrent.Future

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

@Slf4j
class Peer extends Object {
    String ip
    int port
    String protocol = "http://"
    String status = "NEW"
    Network network
    AsyncHTTPBuilder httpBuilder

    static Peer create(String string, Network network){
        def data = string.split(":")
        def port = data[1] as int
      new Peer(data[0], port, network)
    }

    Peer(String ip, int port, Network network) {
      this.ip = ip
      this.port = port
      this.protocol = (port % 1000 == 443) ? "https://" : "http://"
      this.network = network
    }

  AsyncHTTPBuilder getHttp() {
    if(!httpBuilder) {
      httpBuilder = new AsyncHTTPBuilder(uri: "${protocol}${ip}:${port}")
      httpBuilder.setHeaders(network.getHeaders())
    }
    httpBuilder
  }

    // return Future that will deliver the JSON as a Map
    public Future request(Method method, String path, Map queryParams = [:], body = [:]){
      getHttp().request(method, JSON) {
          uri.path = path
          uri.query = queryParams
          body = body

        log.debug("Request: " + uri)

            response.success = { resp, json ->
                this.status = "OK"
                json
            }
        }
    }

    public Map getStatus(){
        def status = request(GET, "/peer/status").get()
        status
    }

    boolean isOk() {
      try {
        return getStatus().get("success") as boolean
      } catch (Exception e) {
        return false
      }
    }

    /*
     * TODO: Actually use this to get an updated list of peers instead of the
     * constantly breaking hardcoded one in the Network class
     */
    public Map getPeers(){
        request(GET, "/peer/list").get()
    }

    public Map getDelegates(){
        request(GET, "/api/delegates").get()
    }

    public Map postTransaction(Transaction transaction){
        Future future = httpBuilder.request(POST, JSON) {
            uri.path = "/peer/transactions"
            body = [transactions:[transaction.toObject()]]

            response.success = { resp, json ->
                json
            }
        }
        future.get()
    }

    public Map getTransactions(Account account, int amount)
    {
        Future future = httpBuilder.get(path: "/api/transactions",
                contentType: JSON,
                query: [recipientId:account.getAddress(),
                        senderId:account.getAddress(),
                        limit:amount])

        future.get()
    }

    public Map leftShift(Transaction transaction){
        postTransaction(transaction)
    }

}
