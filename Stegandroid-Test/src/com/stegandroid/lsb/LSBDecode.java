package com.stegandroid.lsb;

import com.stegandroid.tools.Utils;

public class LSBDecode {
	
	private static final int BYTE_SIZE 		= 8;
	private static final int INT_SIZE 		= BYTE_SIZE * 4;

	private int _to_unhide_byte_length 	= 0;
	private int _to_unhide_bit_length 	= 0;
	private int _nbBitToDecodeInOneByte	= 1;
	private int _cursor					= 0;
	private int _get_int_cursor			= 0;
	
	public byte[] _unhide_content		= null;
	
	private String _intRepr				= "";

	
	/**
	 * @param frame The frame which contains hidden data
	 * @return null until all the content is not recover,
	 * a byte array of length 0 if nothing is found,
	 *  or a byte array with the unhidden content otherwise
	 */
	public byte[] decodeFrame(byte[] frame) {
		if (_cursor > _to_unhide_bit_length)
			return _unhide_content;
		
		for (int i = 0; i < frame.length; i++) {
			if (_get_int_cursor < INT_SIZE * 2) {
				_intRepr += Utils.getBit(frame[i], 0);
				_get_int_cursor++;
				try {
					
					if (_get_int_cursor == INT_SIZE) {
						_to_unhide_byte_length = Integer.parseInt(_intRepr, 2);
						_to_unhide_bit_length = _to_unhide_byte_length * BYTE_SIZE;
						if (_to_unhide_byte_length > Utils.MAX_BYTE_TO_HIDE)
							return new byte[0];
						_unhide_content = new byte[_to_unhide_byte_length];
						_intRepr = "";
					} else if (_get_int_cursor == INT_SIZE * 2) {
						_nbBitToDecodeInOneByte = Integer.parseInt(_intRepr, 2);
						// The nb of bit to hide in one byte is limited to 4
						// If its > 4, there is a problem
						if (_nbBitToDecodeInOneByte > 4)
							return new byte[0];
					}
					
				} catch (NumberFormatException e) {
					return new byte[0];
				}
			} else {
				for (int j = 0; j < _nbBitToDecodeInOneByte; j++) {
					if (_cursor >= _to_unhide_bit_length) {
						return _unhide_content;
					}
					int bitValue = Utils.getBit(frame[i], j);
					_unhide_content = Utils.setBitInByteArray(_unhide_content, bitValue, _cursor++);
				}
			}
		}
		
		return null;
	}
}
