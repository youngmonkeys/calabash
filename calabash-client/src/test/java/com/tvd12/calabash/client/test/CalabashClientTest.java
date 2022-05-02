package com.tvd12.calabash.client.test;

import com.tvd12.calabash.client.CalabaseClient;
import com.tvd12.calabash.client.CalabashClientFactory;
import com.tvd12.calabash.client.MessageChannel;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyMapBuilder;

public class CalabashClientTest {

    public static void main(String[] args) throws Exception {
        new CalabashClientTest().test();
    }

    @SuppressWarnings("unchecked")
    public void test() throws Exception {
        CalabaseClient client = CalabashClientFactory.builder()
            .build()
            .newClient();
        EntityMap<String, String> map = client.getMap("world", String.class, String.class);
        System.out.println("Map: " + map);
        map.set("Hello", "World");
        System.out.println("set ok");
        map.put("Foo", "Bar");
        System.out.println("put ok");
        map.putAll(EzyMapBuilder.mapBuilder()
            .put("Who", "Are You?")
            .build());
        System.out.println("Hello " + map.get("Hello"));
        System.out.println("Hello " + map.get(Sets.newHashSet(
            "Hello", "Who")));

        IAtomicLong atomicLong = client.getAtomicLong("hello");
        System.out.println("AtomicLong.id = " + atomicLong);
        System.out.println("AtomicLong.value1 = " + atomicLong.incrementAndGet());
        System.out.println("AtomicLong.value2 = " + atomicLong.addAndGet(100));

        MessageChannel<String> messageChannel = client.getMessageChannel("hello", String.class);
        messageChannel.addSubscriber(message -> {
            System.out.println("received message: " + message);
        });
        System.out.println("message channel: " + messageChannel);
        Thread.sleep(500);
        messageChannel.publish("Don't do that");
        System.out.println("publish message ok");
    }
}
