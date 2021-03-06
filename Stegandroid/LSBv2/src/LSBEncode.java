
public class LSBEncode {

	private static final int BYTE_SIZE 		= 8;
	private static final int INT_SIZE 		= BYTE_SIZE * 4;

	private int _to_hide_byte_length 	= 0;
	private int _to_hide_bit_length 	= 0;
	private int _nbBitToHideInOneByte	= 1;
	private int _cursor					= 0;
	
	private byte[] _content				= null;
	public byte[] _to_hide				= null;


	/**
	 * @param content the content to hide
	 * @param nbBitToHideInOneByte the number of bit to hide in one byte
	 */
	public LSBEncode(byte[] content, int nbBitToHideInOneByte) {
		_content 				= content;
		_to_hide_byte_length 	= content.length;
		_nbBitToHideInOneByte 	= nbBitToHideInOneByte;

		constructToHide();
	}
	
	/**
	 * Fill _to_hide var which contains every element we must hide
	 */
	public void constructToHide() {
		byte[] one = Utils.intToByteArray(_to_hide_byte_length);
		byte[] two = Utils.intToByteArray(_nbBitToHideInOneByte);
		byte[] combined = new byte[one.length + two.length];

		System.arraycopy(one,0,combined,0         ,one.length);
		System.arraycopy(two,0,combined,one.length,two.length);
		
		_to_hide = new byte[combined.length + _content.length];
		System.arraycopy(combined,0,_to_hide,0         ,combined.length);
		System.arraycopy(_content,0,_to_hide,combined.length,_content.length);

		// re compute the length
		_to_hide_byte_length 	= _to_hide.length;
		_to_hide_bit_length 	= _to_hide_byte_length * BYTE_SIZE;
	}
	
	/**
	 * @param frame The frame which should contain data to hide
	 * @return
	 */
	public byte[] encodeNextFrame(byte[] frame) {
		if (_cursor > _to_hide_bit_length)
			return frame;
		
		for (int i = 0; i < frame.length; i++) {
			if (_cursor < INT_SIZE * 2) {
				int bitValue = Utils.getBitInByteArray(_to_hide, _cursor++);
				frame[i] = Utils.setSpecificBit(frame[i], bitValue, 0);
				
			} else {			
				for (int j = 0; j < _nbBitToHideInOneByte; j++) {
					if (_cursor >= _to_hide_bit_length)
						return frame;
					int bitValue = Utils.getBitInByteArray(_to_hide, _cursor++);
					frame[i] = Utils.setSpecificBit(frame[i], bitValue, j);
				}
			}
		}
		return frame;
	}
}
