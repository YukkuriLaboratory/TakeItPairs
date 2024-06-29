package net.yukulab.client.extension;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.yukulab.config.ClientConfig;
import org.lwjgl.glfw.GLFW;

public class TakeItPairsKeyBinds {
    private static final String GENERAL_CATEGORY = "key.takeitpair.category.general";
    private static KeyBinding RIDER_POS_Y_INCREASE;
    private static KeyBinding RIDER_POS_Y_DECREASE;

    public static void init() {
        RIDER_POS_Y_INCREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderposy.increase",
                GLFW.GLFW_KEY_UNKNOWN,
                GENERAL_CATEGORY
        ));
        RIDER_POS_Y_DECREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderposy.lower",
                GLFW.GLFW_KEY_UNKNOWN,
                GENERAL_CATEGORY
        ));
    }

    public static void onClientTick(MinecraftClient client) {
        while (RIDER_POS_Y_INCREASE.wasPressed()) {
            var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$setClientConfig(new ClientConfig(config.riderPosY() + Math.abs(config.riderPosYModifier()), config.riderPosYModifier()));
        }
        while (RIDER_POS_Y_DECREASE.wasPressed()) {
            var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$setClientConfig(new ClientConfig(config.riderPosY() - Math.abs(config.riderPosYModifier()), config.riderPosYModifier()));
        }
    }
}
