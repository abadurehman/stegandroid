package com.stegandroid.algorithms.cryptography;


public class AdvancedEncryptionStandard192 extends AdvancedEncryptionStandard {
	
	private final int KEY_LENGTH = 24;
	
	public AdvancedEncryptionStandard192() {
	}

	@Override
	public byte[] encrypt(byte[] message, byte[] key) {
		boolean valid = true;
		
		if (message == null) {
			System.err.println("AES 192: Message to encrypt is null");
			valid = false;
		}
		if (key == null) {
			System.err.println("AES 192: Key is null");
			valid = false;
		} else if (!checkKeyLength(key.length)) {
			System.err.println("AES 192: Key must be " + KEY_LENGTH + " length");			
			valid = false;
		} 
		return (valid ? super.encryptionProcess(AESType.AES_192, message, key) : null);
	}

	@Override
	public byte[] decrypt(byte[] cipher, byte[] key) {
		boolean valid = true;
		
		if (cipher == null) {
			System.err.println("AES 192: Cipher to decrypt is null");
			valid = false;
		}
		if (key == null) {
			System.err.println("AES 192: Key is null");
			valid = false;
		} else if (!checkKeyLength(key.length)) {
			System.err.println("AES 192: Key must be " + KEY_LENGTH + " length");			
			valid = false;
		} 
		return (valid ? super.decryptionProcess(AESType.AES_192, cipher, key) : null);
	}

	private boolean checkKeyLength(int keylength) {
		return keylength == KEY_LENGTH;
	}
}