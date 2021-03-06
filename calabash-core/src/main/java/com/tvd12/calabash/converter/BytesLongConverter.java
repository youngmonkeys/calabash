package com.tvd12.calabash.converter;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesLongConverter {

	BytesLongConverter DEFAULT = new BytesLongConverter() {
		@Override
		public long bytesToLong(byte[] bytes) {
			return ByteArray.bytesToLong(bytes);
		}
		
		@Override
		public byte[] longToBytes(long value) {
			return ByteArray.numberToBytes(value);
		}
	};
	
	long bytesToLong(byte[] bytes);
	
	byte[] longToBytes(long value);
}
