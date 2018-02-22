package io.ark.core.network.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ark.core.network.response.v1.Block;
import io.ark.core.network.response.v1.Fee;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockExplorerResponse {

  private boolean success;
  private Fee fees;
  private Block block;
  private List<Block> blocks;
  private String epoch;
  private long height;
  private String nethash;
  private long fee;
  private int milestone;
  private int reward;
  private long supply;

}
