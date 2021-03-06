package com.stegandroid.algorithms.steganography.audio;

public final class AACSteganographyContainerLsb2Bits extends AACSteganographyContainerLsb {

	private final int BITS_TO_HIDE_IN_ONE_BYTE = 2;
	
	public AACSteganographyContainerLsb2Bits() {
		super();
		_nbBitToHideInOneByte = BITS_TO_HIDE_IN_ONE_BYTE;
	}
}
