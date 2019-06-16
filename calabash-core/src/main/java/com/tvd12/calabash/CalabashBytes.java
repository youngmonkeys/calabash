package com.tvd12.calabash;

import com.tvd12.calabash.core.BytesMap;

public interface CalabashBytes {

	default BytesMap getBytesMap(String name) {
		throw new UnsupportedOperationException();
	}
	
}
