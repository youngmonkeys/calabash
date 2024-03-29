package com.tvd12.calabash.client;

import com.tvd12.ezyfox.util.EzyCloseable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CalabashClientProxy extends EzyCloseable {

    int mapGetId(String mapName);

    void mapSet(int mapId, byte[] key, byte[] value);

    byte[] mapPut(int mapId, byte[] key, byte[] value);

    void mapPutAll(int mapId, Map<byte[], byte[]> m);

    byte[] mapGet(int mapId, byte[] key);

    List<byte[]> mapGet(int mapId, byte[]... keys);

    byte[] mapGetByQuery(int mapId, byte[] key, byte[] query);

    boolean mapContainsKey(int mapId, byte[] key);

    boolean mapContainsValue(int mapId, byte[] value);

    byte[] mapRemove(int mapId, byte[] key);

    void mapRemove(int mapId, byte[]... keys);

    Set<byte[]> mapKeySet(int mapId);

    List<byte[]> mapValues(int mapId);

    Map<byte[], byte[]> mapGetAll(int mapId);

    void mapClear(int mapId);

    long mapSize(int mapId);

    int atomicLongGetId(String atomicLongName);

    long atomicLongGet(int atomicLongId);

    long atomicLongIncrementAndGet(int atomicLongId);

    long atomicLongAddAndGet(int atomicLongId, long delta);

    int messageChannelGetId(String channelName);

    void messageChannelPublish(int channelId, byte[] message);

    void messageChannelSubscribe(int channelId, MessageChannelSubscriber subscriber);

    void close();
}
