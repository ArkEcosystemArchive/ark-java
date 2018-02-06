package io.ark.core.model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bip32 {

	private BigInteger publicKey;
	private BigInteger privateKey;

}
