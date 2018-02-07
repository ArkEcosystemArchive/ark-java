package io.ark.core.requests;

import static io.ark.core.requests.AccessType.INTEGER;
import static io.ark.core.requests.AccessType.LONG;
import static io.ark.core.requests.AccessType.OBJECT;
import static io.ark.core.requests.AccessType.STRING;
import static io.ark.core.util.Constants.ARKTOSHI;

import io.ark.core.model.Block;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.responses.Fees;

public class BlockExplorer extends Manager {

  private static final String getBlockById = "/api/blocks/get?id=";

  // TODO : Deal with this later
  // private static final String getAllBlocks = "api/blocks";

  private static final String getEpoch = "/api/blocks/getEpoch";
  private static final String getHeight = "/api/blocks/getHeight";
  private static final String getNethash = "/api/blocks/getNethash";
  private static final String getFee = "/api/blocks/getFee";
  private static final String getFees = "/api/blocks/getFees";
  private static final String getMilestone = "/api/blocks/getMilestone";
  private static final String getReward = "/api/blocks/getReward";
  private static final String getSupply = "/api/blocks/getSupply";
  private static final String getStatus = "/api/blocks/getStatus";

  public BlockExplorer(NetworkConfig config, NetworkInfo info) {
    super(config, info);
  }

  public Block getBlockById(String id) {
    return http.get(getBlockById + id, "block", Block.class, OBJECT);
  }

  public String getEpoch() {
    return http.get(getEpoch, "epoch", String.class, STRING);
  }

  public long getHeight() {
    return http.get(getHeight, "height", long.class, LONG);
  }

  public String getNethash() {
    return http.get(getNethash, "nethash", String.class, STRING);
  }

  public double getTransactionFee() {
    long fee = http.get(getFee, "fee", long.class, LONG);
    return (double) fee / ARKTOSHI;
  }

  // TODO : Deal with inconsistent JSON responses later.
  /**
   * Trying out a executor based request with Object responsible for creation.
   * @return
   */
  public Fees getFees() {
    /**
     * This specific JSON is flat even though there is `fees` object that COULD be
     * created to make mapping easier.
     */
    return Fees.fromResponse(http.getFuture(getFees));
  }

  public int getMilestone() {
    return http.get(getMilestone, "milestone", int.class, INTEGER);
  }

  public double getReward() {
    long reward = http.get(getReward, "reward", int.class, INTEGER);
    return (double) reward / ARKTOSHI;
  }

  public long getSupply() {
    long supply = http.get(getSupply, "supply", long.class, LONG);
    return supply / ARKTOSHI;
  }

  // TODO : Inconsistent JSON format
  public void getStatus() {

  }
}
