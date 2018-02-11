package io.ark.core.requests;

import static io.ark.core.util.Constants.ARKTOSHI;
import io.ark.core.model.Block;
import io.ark.core.model.Fees;
import io.ark.core.model.NetworkStatus;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.requests.dto.BlockQueryParams;
import io.ark.core.responses.BlockExplorerResponse;
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

  public BlockExplorer(NetworkConfig config, NetworkInfo info) {
    super(config, info);
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

  public double getTransactionFee() {
    BlockExplorerResponse res = doRequest(getFee);

    if (!res.isSuccess()) {

    }

    return (double) res.getFee() / ARKTOSHI;
  }

  public Fees getFees() {
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

  public double getReward() {
    BlockExplorerResponse res = doRequest(getReward);

    if (!res.isSuccess()) {

    }

    return (double) res.getReward() / ARKTOSHI;
  }

  public long getSupply() {
    BlockExplorerResponse res = doRequest(getSupply);

    if (!res.isSuccess()) {

    }

    return res.getSupply() / ARKTOSHI;
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
    return http.getFuture(endpoint, BlockExplorerResponse.class);
  }
}
