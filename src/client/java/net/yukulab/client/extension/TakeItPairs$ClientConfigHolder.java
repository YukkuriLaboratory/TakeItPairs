package net.yukulab.client.extension;

import net.yukulab.config.ClientConfig;
import org.jetbrains.annotations.NotNull;

public interface TakeItPairs$ClientConfigHolder {
    default ClientConfig takeitpairs$getClientConfig() {
        return null;
    }

    default void takeitpairs$setClientConfig(@NotNull ClientConfig newConfig) {}
}
