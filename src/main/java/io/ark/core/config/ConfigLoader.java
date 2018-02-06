package io.ark.core.config;

import org.json.JSONObject;

import io.ark.core.network.ArkNetwork;
import io.ark.core.network.NetworkConfig;
import io.ark.core.network.NetworkInfo;
import io.ark.core.util.JsonUtils;

public class ConfigLoader {

  private static final String DEV_CONFIG = "config.devnet.json";
  private static final String MAIN_CONFIG = "config.mainnet.json";
  private static final String NETWORKS = "networks.json";

  private static JSONObject networkInformation;

  public static NetworkConfig loadNetworkConfig(ArkNetwork network) throws Exception {
    JSONObject json;
    switch (network) {
    case TEST:
      break;
    case DEV:
      json = JsonUtils.getResourceJSON(DEV_CONFIG);
      return JsonUtils.getObjectFromJson(json, NetworkConfig.class);
    case MAIN:
      json = JsonUtils.getResourceJSON(MAIN_CONFIG);
      return JsonUtils.getObjectFromJson(json, NetworkConfig.class);
    default:
      break;
    }
    return null;
  }

  public static NetworkInfo loadNetworkInformation(ArkNetwork network) throws Exception {
    if (networkInformation == null) {
      networkInformation = JsonUtils.getResourceJSON(NETWORKS);
    }

    return getNetworkInformationFromJSON(network);
  }

  private static NetworkInfo getNetworkInformationFromJSON(ArkNetwork network) throws Exception {
    JSONObject networkObject = networkInformation.getJSONObject(network.getName());
    return JsonUtils.getObjectFromJson(networkObject, NetworkInfo.class);
  }

}
