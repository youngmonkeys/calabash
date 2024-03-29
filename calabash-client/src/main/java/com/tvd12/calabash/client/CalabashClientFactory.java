package com.tvd12.calabash.client;

import com.tvd12.calabash.client.annotation.CachedKey;
import com.tvd12.calabash.client.annotation.CachedValue;
import com.tvd12.calabash.client.annotation.Message;
import com.tvd12.calabash.client.factory.MessageChannelFactory;
import com.tvd12.calabash.client.manager.MessageChannelProvider;
import com.tvd12.calabash.client.setting.Settings;
import com.tvd12.calabash.client.setting.SettingsBuilder;
import com.tvd12.calabash.client.util.CachedValueAnnotations;
import com.tvd12.calabash.client.util.MessageAnnotations;
import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.response.MessageResponse;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.binding.writer.EzyDefaultWriter;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.quick.rpc.client.QuickRpcClient;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CalabashClientFactory {

    protected final Settings settings;
    protected final EzyEntityCodec entityCodec;
    protected final CalabashClientProxy clientProxy;
    protected final MessageChannelProvider messageChannelProvider;

    protected CalabashClientFactory(Builder builder) {
        this.settings = builder.settings;
        this.entityCodec = builder.entityCodec;
        this.clientProxy = builder.clientProxy;
        this.messageChannelProvider = builder.messageChannelProvider;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CalabaseClient newClient() {
        return CalabaseClient.builder()
            .settings(settings)
            .clientProxy(clientProxy)
            .entityCodec(entityCodec)
            .messageChannelProvider(messageChannelProvider)
            .build();
    }

    public static class Builder implements EzyBuilder<CalabashClientFactory> {

        protected EzyReflection reflection;
        protected Set<String> packagesToScan;
        protected Properties properties;
        protected Settings settings;
        protected EzyEntityCodec entityCodec;
        protected CalabashClientProxy clientProxy;
        protected SettingsBuilder settingsBuilder;
        protected EzyBindingContext bindingContext;
        protected EzyBindingContextBuilder bindingContextBuilder;
        protected MessageChannelFactory messageChannelFactory;
        protected MessageChannelProvider messageChannelProvider;

        public Builder() {
            this.properties = new Properties();
            this.packagesToScan = new HashSet<>();
        }

        public Builder scan(String packageName) {
            this.packagesToScan.add(packageName);
            return this;
        }

        public Builder scan(String... packageNames) {
            return scan(Sets.newHashSet(packageNames));
        }

        public Builder scan(Iterable<String> packageNames) {
            for (String packageName : packageNames) {
                this.scan(packageName);
            }
            return this;
        }

        public Builder properties(Properties properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder settings(Settings settings) {
            this.settings = settings;
            return this;
        }

        public Builder settingsBuilder(SettingsBuilder settingsBuilder) {
            this.settingsBuilder = settingsBuilder;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        public Builder clientProxy(CalabashClientProxy clientProxy) {
            this.clientProxy = clientProxy;
            return this;
        }

        @Override
        public CalabashClientFactory build() {
            if (packagesToScan.size() > 0) {
                this.reflection = new EzyReflectionProxy(packagesToScan);
            }
            this.prepareSettings();
            this.prepareBindingContext();
            this.prepareEntityCodec();
            this.prepareClientProxy();
            this.prepareMessageChannelFactory();
            this.prepareMessageChannelProvider();
            return new CalabashClientFactory(this);
        }

        private void prepareSettings() {
            if (settings != null) {
                return;
            }
            if (settingsBuilder == null) {
                settingsBuilder = new SettingsBuilder();
            }
            settingsBuilder.properties(properties);
            if (reflection != null) {
                Set<Class<?>> cachedClasses = reflection.getAnnotatedClasses(CachedValue.class);
                for (Class<?> cachedClass : cachedClasses) {
                    EzyField keyField = getMapKeyFieldOf(cachedClass);
                    String mapName = CachedValueAnnotations.getMapName(cachedClass);
                    settingsBuilder.mapSettingBuilder(mapName)
                        .keyType(keyField.getType())
                        .valueType(cachedClass);
                }
                Set<Class<?>> messageClasses = reflection.getAnnotatedClasses(Message.class);
                for (Class<?> messageClass : messageClasses) {
                    String channelName = MessageAnnotations.getChannelName(messageClass);
                    settingsBuilder.channelSettingBuilder(channelName)
                        .messageType(messageClass);
                }
            }
            this.settings = settingsBuilder.build();
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void prepareBindingContext() {
            bindingContextBuilder = EzyBindingContext.builder()
                .scan("com.tvd12.calabash.rpc.common")
                .addTemplate(BigDecimal.class, EzyDefaultWriter.getInstance())
                .addTemplate(BigInteger.class, EzyDefaultWriter.getInstance());
            if (reflection != null) {
                Set<Class<?>> valueClasses = reflection.getAnnotatedClasses(CachedValue.class);
                bindingContextBuilder
                    .addAllClasses(reflection)
                    .addClasses((Set) valueClasses)
                    .addClasses((Set) reflection.getAnnotatedClasses(CachedKey.class));
                for (Class<?> valueClass : valueClasses) {
                    EzyField keyField = getMapKeyFieldOf(valueClass);
                    CachedKey cachedKeyAnno = keyField.getAnnotation(CachedKey.class);
                    if (cachedKeyAnno != null && cachedKeyAnno.composite()) {
                        bindingContextBuilder.addClass(keyField.getType());
                    }
                }

            }
            bindingContext = bindingContextBuilder.build();
        }

        private void prepareEntityCodec() {
            if (entityCodec == null) {
                entityCodec = EzyBindingEntityCodec.builder()
                    .marshaller(bindingContext.newMarshaller())
                    .unmarshaller(bindingContext.newUnmarshaller())
                    .messageSerializer(new MsgPackSimpleSerializer())
                    .messageDeserializer(new MsgPackSimpleDeserializer())
                    .build();
            }
        }

        protected void prepareMessageChannelFactory() {
            messageChannelFactory = MessageChannelFactory.builder()
                .settings(settings)
                .clientProxy(clientProxy)
                .entityCodec(entityCodec)
                .build();
        }

        protected void prepareMessageChannelProvider() {
            messageChannelProvider = MessageChannelProvider.builder()
                .channelFactory(messageChannelFactory)
                .build();
        }

        private EzyField getMapKeyFieldOf(Class<?> mapValueClass) {
            EzyClass clazz = new EzyClass(mapValueClass);
            return clazz.getField(f ->
                    f.isAnnotated(CachedKey.class)
                        || f.isAnnotated(EzyId.class)
                        || f.isAnnotated(Id.class)
                )
                .orElseThrow(() -> new IllegalArgumentException(
                    "unknown key type of cached value type: " + mapValueClass.getName() +
                        ", annotate key field with @CachedKey or @EzyId or @Id"
                ));
        }

        @SuppressWarnings("rawtypes")
        private void prepareClientProxy() {
            if (clientProxy == null) {
                QuickRpcClient quickRpcClient = QuickRpcClient.builder()
                    .properties(properties)
                    .scan("com.tvd12.calabash.rpc.common")
                    .onResponseReceived(Commands.MESSAGE, r -> {
                        MessageResponse mr = r.getData(MessageResponse.class);
                        MessageChannel channel = messageChannelProvider.getChannel(mr.getChannelId());
                        if (channel != null) {
                            channel.receiveMessage(mr.getMessage());
                        }
                    })
                    .build();
                clientProxy = new CalabashClientRpc(quickRpcClient);
            }
        }
    }
}
