package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import net.yukulab.config.ConfigIO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements TakeItPairs$ClientConfigHolder {
    @Unique
    private final ClientConfig takeitpairs$clientConfig = ConfigIO.readConfigOrDefault(ClientConfig.class, ClientConfig.getDefaultConfig());

    @Override
    public ClientConfig takeitpairs$getClientConfig() {
        return takeitpairs$clientConfig;
    }

    @Override
    public void takeitpairs$updateClientConfig() {
        ConfigIO.writeConfig(takeitpairs$clientConfig);
    }
}
