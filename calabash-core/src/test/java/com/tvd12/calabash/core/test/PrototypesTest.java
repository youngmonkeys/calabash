package com.tvd12.calabash.core.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.calabash.core.prototype.Prototype;
import com.tvd12.calabash.core.prototype.PrototypeProxy;
import com.tvd12.calabash.core.prototype.Prototypes;
import com.tvd12.calabash.core.prototype.SimplePrototypes;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyMapBuilder;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unchecked")
public class PrototypesTest {

//	public static void main(String[] args) {
//		EzyObjectCopier copier = new EzyObjectCopier();
//		String script = copier.generateScript(ClassA.class, getter -> {
//			getter.insert(0, "prototypes.copy(");
//			getter.append(")");
//		});
//		System.out.println(script);
//	}
	
	@Test
	public void test() {
		Prototypes prototypes = Prototypes.builder()
				.addProxy(new ClassBProxy())
				.addProxy(new ClassAProxy())
				.build();
		ClassA origin = new ClassA();
		ClassA copy = prototypes.copy(origin);
		assert copy != origin;
	}
	
	@Test
	public void test1() {
		Prototypes prototypes = new SimplePrototypes();
		assert prototypes.copy(null) == null;
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		Prototypes.builder()
				.addProxy(new ClassCProxy())
				.build();
	}
	
	public static class ClassAProxy implements PrototypeProxy<ClassA> {
		@Override
		public ClassA clone(Prototypes prototypes, ClassA origin) {
			ClassA copy = new ClassA();
			copy.setList(prototypes.copy(origin.getList()));
			copy.setSet(prototypes.copy(origin.getSet()));
			copy.setMap(prototypes.copy(origin.getMap()));
			copy.setArrayC(prototypes.copy(origin.getArrayC()));
			copy.setPrimitiveBooleanArray(prototypes.copy(origin.getPrimitiveBooleanArray()));
			copy.setPrimitiveByteArray(prototypes.copy(origin.getPrimitiveByteArray()));
			copy.setPrimitiveCharArray(prototypes.copy(origin.getPrimitiveCharArray()));
			copy.setPrimitiveDoubleArray(prototypes.copy(origin.getPrimitiveDoubleArray()));
			copy.setPrimitiveFloatArray(prototypes.copy(origin.getPrimitiveFloatArray()));
			copy.setPrimitveIntArray(prototypes.copy(origin.getPrimitveIntArray()));
			copy.setPrimitiveLongArray(prototypes.copy(origin.getPrimitiveLongArray()));
			copy.setPrimitiveShortArray(prototypes.copy(origin.getPrimitiveShortArray()));
			copy.setWrapperBooleanArray(prototypes.copy(origin.getWrapperBooleanArray()));
			copy.setWrapperByteArray(prototypes.copy(origin.getWrapperByteArray()));
			copy.setWrapperCharArray(prototypes.copy(origin.getWrapperCharArray()));
			copy.setWrapperDoubleArray(prototypes.copy(origin.getWrapperDoubleArray()));
			copy.setWrapperFloatArray(prototypes.copy(origin.getWrapperFloatArray()));
			copy.setWrapperIntegerArray(prototypes.copy(origin.getWrapperIntegerArray()));
			copy.setWrapperLongArray(prototypes.copy(origin.getWrapperLongArray()));
			copy.setWraperShortArray(prototypes.copy(origin.getWraperShortArray()));
			copy.setStringArray(prototypes.copy(origin.getStringArray()));
			return copy;
		}
	}
	
	public static class ClassBProxy implements PrototypeProxy<ClassB> {
		@Override
		public ClassB clone(Prototypes prototypes, ClassB origin) {
			ClassB copy = new ClassB();
			return copy;
		}
	}

	@Getter
	@Setter
	public static class ClassA {
		private List<ClassB> list = Lists.newArrayList(new ClassB(), new ClassB());
		private Set<ClassB> set = Sets.newHashSet(new ClassB());
		private Map<Integer, ClassC> map = EzyMapBuilder.mapBuilder()
				.put(1, new ClassC())
				.put(2, new ClassC())
				.build();
		private ClassC[] arrayC = new ClassC[] {new ClassC(), new ClassC(), new ClassC()};
		private boolean[] primitiveBooleanArray = new boolean[] {true, false, true};
		private byte[] primitiveByteArray = new byte[] {1, 2, 3};
		private char[] primitiveCharArray = new char[] {1, 2, 3};
		private double[] primitiveDoubleArray = new double[] {1, 2, 3};
		private float[] primitiveFloatArray = new float[] {1, 2, 3};
		private int[] primitveIntArray = new int[] {1, 2, 3};
		private long[] primitiveLongArray = new long[] {1, 2, 3};
		private short[] primitiveShortArray = new short[] {1, 2, 3};
		
		private Boolean[] wrapperBooleanArray = new Boolean[] {true, false, true};
		private Byte[] wrapperByteArray = new Byte[] {1, 2, 3};
		private Character[] wrapperCharArray = new Character[] {1, 2, 3};
		private Double[] wrapperDoubleArray = new Double[] {1.0D, 2.0D, 3.0D};
		private Float[] wrapperFloatArray = new Float[] {1.0F, 2.0F, 3.0F};
		private Integer[] wrapperIntegerArray = new Integer[] {1, 2, 3};
		private Long[] wrapperLongArray = new Long[] {1L, 2L, 3L};
		private Short[] wraperShortArray = new Short[] {1, 2, 3};
		private String[] stringArray = new String[] {"a", "b", "c"};
	}
	
	@SuppressWarnings("rawtypes")
	public static class ClassCProxy implements PrototypeProxy {

		@Override
		public Object clone(Prototypes prototypes, Object origin) {
			return new ClassC();
		}
		
	}
	
	public static class ClassB {
	}
	
	public static class ClassC implements Prototype {
		
		@Override
		public Object clone() {
			ClassC copy = new ClassC();
			return copy;
		}
	}
	
}
