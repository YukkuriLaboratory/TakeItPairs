package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import net.yukulab.config.ConfigIO;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements TakeItPairs$ClientConfigHolder {
    @Unique
    private ClientConfig takeitpairs$clientConfig = ConfigIO.readConfig(ClientConfig.class)
            .orElseGet(() -> {
                // If not config generated
                var config = ClientConfig.asDefault();
                ConfigIO.writeConfig(config);
                return config;
            });

    @Override
    public ClientConfig takeitpairs$getClientConfig() {
        return takeitpairs$clientConfig;
    }

    @Override
    public void takeitpairs$setClientConfig(@NotNull ClientConfig newConfig) {
        takeitpairs$clientConfig = newConfig;
        // Save config
        ConfigIO.writeConfig(newConfig);
    }
}
