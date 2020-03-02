package com.tvd12.calabash.server.core.impl;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.io.EzyBytes;
import com.tvd12.ezyfox.io.EzyLongs;

import lombok.Getter;

public class AtomicLongImpl implements IAtomicLong {

	@Getter
	protected final String name;
	protected final BytesMap map;
	protected final ByteArray nameBytes;
	
	public AtomicLongImpl(String name, BytesMap map) {
		this.map = map;
		this.name = name;
		this.nameBytes = ByteArray.wrap(name);
	}
	
	@Override
	public long addAndGet(long delta) {
		synchronized (this) {
			Long current = null;
			byte[] currentBytes = map.get(nameBytes);
			if(currentBytes != null)
				current = EzyLongs.bin2long(currentBytes);
			Long newValue = current == null ? delta : current + delta;
			byte[] newValueBytes = EzyBytes.getBytes(newValue);
			map.put(nameBytes, newValueBytes);
			return newValue;
		}
	}
	
}
