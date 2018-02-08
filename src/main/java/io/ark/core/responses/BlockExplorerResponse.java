package io.ark.core.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ark.core.model.Block;
import io.ark.core.model.Fees;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockExplorerResponse {

  private boolean success;
  private Fees fees;
  private Block block;
  private String epoch;
  private long height;
  private String nethash;
  private long fee;
  private int milestone;
  private int reward;
  private long supply;
  
}
