package io.ark.core

import groovyx.net.http.AsyncHTTPBuilder
import groovy.transform.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.JSON
import java.util.concurrent.Future

@Canonical
class Peer extends Object {
  String ip
  int port
  String protocol = "http://"
  String status = "NEW"

  private AsyncHTTPBuilder http
  private Map networkHeaders

  public static Peer create(String string, networkHeaders = Network.Mainnet.headers){
    def data = string.split(":")
    def port = data[1] as int
    def protocol = "http://"
    if(port%1000 == 443) protocol = "https://"
    new Peer(ip: data[0], port: port, protocol: protocol, networkHeaders:networkHeaders)
  }

  // return Future that will deliver the JSON as a Map
  public Future request(String method, String path, body = [:]){
    if(!http)
      http = new AsyncHTTPBuilder(uri: "${protocol}${ip}:${port}")

    def _method
    switch(method){
      case "POST":
        _method = POST
        break
      case "PUT":
        _method = PUT
        break
      case "GET":
        _method = GET
    }

    def that = this

    http.request(_method, JSON) {
      uri.path = path
      headers << networkHeaders
      body = body

      response.success = { resp, json ->
        that.status = "OK"
        json
      }
    }
  }

  public Map getStatus(){
    request("GET", "/peer/status").get()
  }

  public Map postTransaction(Transaction transaction){
    if(!http)
      http = new AsyncHTTPBuilder(uri: "${protocol}${ip}:${port}")
    Future future = http.request(POST, JSON) {
      uri.path = "/peer/transactions"
      headers << networkHeaders
      body = [transactions:[transaction.toObject()]]

      response.success = { resp, json ->
        json
      }
    }
    future.get()
  }

  public Map getPeers(){
    request("GET", "/peer/list").get()
  }

  public Map leftShift(Transaction transaction){
    postTransaction(transaction)
  }

}
