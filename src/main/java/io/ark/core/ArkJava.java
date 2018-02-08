package io.ark.core;

import io.ark.core.network.ArkNet;
import io.ark.core.requests.BlockExplorer;
import io.ark.core.requests.dto.BlockQueryParams;

// TODO : Fix SLF4J error messages on start.
public class ArkJava {

	public static void main(String[] args) throws Exception {
	  ArkNet ark = new ArkNet();
	  BlockExplorer ex = ark.getBlockExplorer();
	  BlockQueryParams params = BlockQueryParams.builder()
	                                  .height(2528057)
	                                  .build();
	  ex.getAllBlocks(params);
	}
}
