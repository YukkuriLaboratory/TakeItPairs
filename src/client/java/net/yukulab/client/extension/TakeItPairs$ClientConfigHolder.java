package net.yukulab.client.extension;

import net.yukulab.config.ClientConfig;

public interface TakeItPairs$ClientConfigHolder {
    default ClientConfig takeitpairs$getClientConfig() {
        return null;
    }

    default void takeitpairs$updateClientConfig() {
    }
}
