package io.ark.core.responses;

import static io.ark.core.util.Constants.ARKTOSHI;

import org.json.JSONObject;

import lombok.Data;

/**
 * This is a proposal for how to deal with all responses from the Ark Node.
 * This is a frustrating case due to the unit conversions and flat JSON response. 
 * Being the worse case scenario this is not too bad, and will allow us to use the futreGet added in HttpUtils.
 */
@Data
public class Fees {

  private float send;
  private float vote;
  private float secondsignature;
  private float delegate;
  private float multisignature;
  
  public static Fees fromResponse(String response) {
    JSONObject res = new JSONObject(response);
    JSONObject fees = res.getJSONObject("fees");
    float send = fees.getFloat("send");
    float vote = fees.getFloat("vote");
    float secondsignature = fees.getFloat("secondsignature");
    float delegate = fees.getFloat("delegate");
    float multisignature = fees.getFloat("multisignature");
    return new Fees(send, vote, secondsignature, delegate, multisignature);
  }
  
  private Fees(float send, float vote, float secondsignature, float delegate, float multisignature) {
    this.send = send / ARKTOSHI;
    this.vote = vote / ARKTOSHI;
    this.secondsignature = secondsignature / ARKTOSHI;
    this.delegate = delegate / ARKTOSHI;
    this.multisignature = multisignature / ARKTOSHI;
  }
  
}
