package net.yukulab.client.extension;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.yukulab.config.ClientConfig;
import org.lwjgl.glfw.GLFW;

public class TakeItPairsKeyBinds {
    private static final String GENERAL_CATEGORY = "key.takeitpairs.category.render",
                                DEBUG_CATEGORY = "key.takeitpairs.category.debug"; // TODO add to lang file
    private static KeyBinding RIDER_POS_Y_INCREASE,
            RIDER_POS_Y_DECREASE,
            RIDER_POS_Z_INCREASE,
            RIDER_POS_Z_DECREASE,
            CHANGE_RIDE_MODE;

    public static void init() {
        // TODO needs to change translation key in lang file
        // Rider Pos Y
        RIDER_POS_Y_INCREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.y.increase",
                GLFW.GLFW_KEY_UNKNOWN,
                DEBUG_CATEGORY
        ));
        RIDER_POS_Y_DECREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.y.decrease",
                GLFW.GLFW_KEY_UNKNOWN,
                DEBUG_CATEGORY
        ));

        // Rider Pos Z
        RIDER_POS_Z_INCREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.z.increase",
                GLFW.GLFW_KEY_UNKNOWN,
                DEBUG_CATEGORY
        ));
        RIDER_POS_Z_DECREASE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.z.decrease",
                GLFW.GLFW_KEY_UNKNOWN,
                DEBUG_CATEGORY
        ));

        // Mode Change
        CHANGE_RIDE_MODE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.change_ride_mode",
                GLFW.GLFW_KEY_UNKNOWN,
                GENERAL_CATEGORY
        ));
    }

    public static void onClientTick(MinecraftClient client) {
        while (RIDER_POS_Y_INCREASE.wasPressed()) {
            var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$setClientConfig(new ClientConfig(config.riderPosY() + Math.abs(config.riderPosYModifier()), config.riderPosYModifier(), config.riderPosZ(), config.riderPosZModifier(), config.rideOnShoulders()));
        }
        while (RIDER_POS_Y_DECREASE.wasPressed()) {
            var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$setClientConfig(new ClientConfig(config.riderPosY() - Math.abs(config.riderPosYModifier()), config.riderPosYModifier(), config.riderPosZ(), config.riderPosZModifier(), config.rideOnShoulders()));
        }
        // TODO need to change ClientConfig Class from record to normal class for reduce cost
//        while (RIDER_POS_Z_INCREASE)
    }
}
