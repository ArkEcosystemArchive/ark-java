package io.ark.core.manager;

import io.ark.core.model.NetworkStatus;
import io.ark.core.network.NetworkConnections;
import io.ark.core.network.response.BlockExplorerResponse;
import io.ark.core.network.response.v1.Block;
import io.ark.core.network.response.v1.Fee;
import io.ark.core.requests.dto.BlockQueryParams;
import java.util.List;

public class BlockExplorer extends Manager {

  private static final String getBlockById = "/api/blocks/get?id=";
  private static final String getAllBlocks = "/api/blocks";
  private static final String getEpoch = "/api/blocks/getEpoch";
  private static final String getHeight = "/api/blocks/getHeight";
  private static final String getNethash = "/api/blocks/getNethash";
  private static final String getFee = "/api/blocks/getFee";
  private static final String getFees = "/api/blocks/getFees";
  private static final String getMilestone = "/api/blocks/getMilestone";
  private static final String getReward = "/api/blocks/getReward";
  private static final String getSupply = "/api/blocks/getSupply";
  private static final String getStatus = "/api/blocks/getStatus";

  public BlockExplorer(NetworkConnections connections) {
    super(connections);
  }

  public Block getBlockById(String id) {
    BlockExplorerResponse res = doRequest(getBlockById + id);

    if (!res.isSuccess()) {

    }

    return res.getBlock();
  }

  public List<Block> getAllBlocks(BlockQueryParams params) {
    BlockExplorerResponse res = doRequest(getAllBlocks + params.getQueryString());

    if (!res.isSuccess()) {

    }

    return res.getBlocks();
  }

  public String getEpoch() {
    BlockExplorerResponse res = doRequest(getEpoch);

    if (!res.isSuccess()) {

    }

    return res.getEpoch();
  }

  public long getHeight() {
    BlockExplorerResponse res = doRequest(getHeight);

    if (!res.isSuccess()) {

    }

    return res.getHeight();
  }

  public String getNethash() {
    BlockExplorerResponse res = doRequest(getNethash);

    if (!res.isSuccess()) {

    }

    return res.getNethash();
  }

  public long getTransactionFee() {
    BlockExplorerResponse res = doRequest(getFee);

    if (!res.isSuccess()) {

    }

    return res.getFee();
  }

  public Fee getFees() {
    BlockExplorerResponse res = doRequest(getFees);

    if (!res.isSuccess()) {

    }

    return res.getFees();
  }

  public int getMilestone() {
    BlockExplorerResponse res = doRequest(getMilestone);

    if (!res.isSuccess()) {

    }

    return res.getMilestone();
  }

  public long getReward() {
    BlockExplorerResponse res = doRequest(getReward);

    if (!res.isSuccess()) {

    }

    return res.getReward();
  }

  public long getSupply() {
    BlockExplorerResponse res = doRequest(getSupply);

    if (!res.isSuccess()) {

    }

    return res.getSupply();
  }

  public NetworkStatus getStatus() {
    BlockExplorerResponse res = doRequest(getStatus);

    if (!res.isSuccess()) {

    }

    return NetworkStatus.builder()
        .epoch(res.getEpoch())
        .height(res.getHeight())
        .nethash(res.getNethash())
        .fee(res.getFee())
        .milestone(res.getMilestone())
        .reward(res.getReward())
        .supply(res.getSupply())
        .build();
  }

  private BlockExplorerResponse doRequest(String endpoint) {
    try {
      return http.get(endpoint, BlockExplorerResponse.class);
    } catch (Exception e) {
      return BlockExplorerResponse.builder().success(false).build();
    }
  }
}
