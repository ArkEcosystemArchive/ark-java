package io.ark.core.network;

import lombok.Getter;

public enum ArkNetwork {
	MAIN("ark"),
	DEV("devnet"),
	TEST("testnet");

	@Getter
	private String name;

	ArkNetwork(String name) {
		this.name = name;
	}
}
