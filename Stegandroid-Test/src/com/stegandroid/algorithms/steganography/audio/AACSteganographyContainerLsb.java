package com.stegandroid.algorithms.steganography.audio;

import java.nio.ByteBuffer;

import com.googlecode.mp4parser.authoring.Sample;
import com.stegandroid.lsb.LSBEncode;

public class AACSteganographyContainerLsb extends AACSteganographyContainer {

	private static final int BYTE_SIZE = 8;
	private int _nbBitToHideInOneByte;
	private int _dataToHideBitOffset;
	
	public AACSteganographyContainerLsb() {
		_nbBitToHideInOneByte = 2;
		_dataToHideBitOffset = 0;
	}
	
	@Override
	public void hideData(byte[] data) {
		Sample sample;
		ByteBuffer dataBuffer;
		ByteBuffer sampleBuffer;
		
		if (_sampleList == null || _sampleListPosition >= _sampleList.size() || data == null) {
			return;
		}
	
		sample = _sampleList.get(_sampleListPosition);
		sampleBuffer = sample.asByteBuffer();
		dataBuffer = ByteBuffer.wrap(data);

		if (_sampleOffset == 0) {
			writeHeader(sampleBuffer.capacity());
		}
		
		while (dataBuffer.remaining() > 0 && _sampleListPosition < _sampleList.size()) {
			
			if (sampleBuffer.remaining() == 0) {
				_sampleOffset = 0;
				_sampleListPosition++;
				if (_sampleListPosition >= _sampleList.size()) {
					continue;
				}
				sample = _sampleList.get(_sampleListPosition);
				sampleBuffer = sample.asByteBuffer();
				writeHeader(sampleBuffer.capacity());
			}
			applyLsb((ByteBuffer) sampleBuffer.slice().position(_sampleOffset), dataBuffer);
			System.out.println(sampleBuffer.capacity() + " | " +_sampleOffset);
			if (_sampleOffset == sampleBuffer.capacity()) {
				sampleBuffer.position(sampleBuffer.position() + _sampleOffset);
				_sampleOffset = 0;			
			}
		}
		if (sampleBuffer.remaining() == 0) {
			_sampleListPosition++;
		}
	}

	private void applyLsb(ByteBuffer sample, ByteBuffer data) {
		byte[] dataToHide = null;
		byte[] signal = null;
		int sizeToWrite = 0;
		int requiredSize;

		if (_dataToHideBitOffset != 0) {
			applyLsbBitByBit(sample, data, true);			
		}
		
		requiredSize = data.remaining() * BYTE_SIZE / _nbBitToHideInOneByte;
		if (requiredSize <= sample.remaining()) {
			sizeToWrite = requiredSize;
			_sampleOffset += sizeToWrite;
			signal = new byte[sizeToWrite];
			sample.get(signal);
			dataToHide = new byte[data.remaining()];
			data.get(dataToHide);
			encode(signal, dataToHide);
			this.addData(signal);
		} else {
			applyLsbBitByBit(sample, data, false);
		}
	}
	
	private void applyLsbBitByBit(ByteBuffer sample, ByteBuffer data, boolean adjust) {
		byte[] dataToHide = null;
		byte[] signal = null;

		signal = new byte[1];
		dataToHide = new byte[1];
		while (sample.hasRemaining() && data.hasRemaining()) {
			sample.get(signal);
			data.get(dataToHide);
			_dataToHideBitOffset += _nbBitToHideInOneByte;
			if (_dataToHideBitOffset >= 8) {
				_dataToHideBitOffset = _dataToHideBitOffset % 8;
			} else {
				data.position(data.position() - 1);
			}
			dataToHide[0] = (byte) (dataToHide[0] >> ((7 - _dataToHideBitOffset) & (0xFF >> 8 - _nbBitToHideInOneByte)));
			encode(signal, dataToHide);
			this.addData(signal);
			_sampleOffset++;
			if (adjust && _dataToHideBitOffset == 0) {
				break;
			}
		}
	}
	
	private void encode(byte[] signal, byte[] content) {
		LSBEncode encode = new LSBEncode(content, _nbBitToHideInOneByte);
		
		encode.encodeNextFrame(signal);
	}

}
