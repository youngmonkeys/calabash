package com.tvd12.calabash.core.prototype;

import static com.tvd12.ezyfox.reflect.EzyGenerics.getGenericInterfacesArguments;

import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("rawtypes")
public interface PrototypesBuilder extends EzyBuilder<Prototypes> {
	
	PrototypesBuilder addProxy(Class objectType, PrototypeProxy proxy);
	
	default PrototypesBuilder addProxy(PrototypeProxy proxy) {
		Class<?> proxyClass = proxy.getClass();
		try {
			Class<?> objectType = getGenericInterfacesArguments(proxyClass, PrototypeProxy.class, 1)[0];
			addProxy(objectType, proxy);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("unknow object type of proxy: " + proxyClass.getName() + ", use addProxy(Class objectType, PrototypeProxy proxy) function instead");
		}
		return this;
	}

}
