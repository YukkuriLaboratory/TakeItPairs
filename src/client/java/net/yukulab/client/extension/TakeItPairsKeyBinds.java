package net.yukulab.client.extension;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class TakeItPairsKeyBinds {
    private static final String GENERAL_CATEGORY = "key.takeitpairs.category.render",
            DEBUG_CATEGORY = "key.takeitpairs.category.debug"; // TODO add to lang file
    private static KeyBinding RIDER_POS_Y_INCREASE,
            RIDER_POS_Y_DECREASE,
            RIDER_POS_Z_INCREASE,
            RIDER_POS_Z_DECREASE,
            RIDE_ON_SHOULDERS;

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
        RIDE_ON_SHOULDERS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.riderpos.ride_on_shoulders",
                GLFW.GLFW_KEY_UNKNOWN,
                GENERAL_CATEGORY
        ));
    }

    public static void onClientTick(MinecraftClient client) {
        var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();

        // Render
        while (RIDE_ON_SHOULDERS.wasPressed()) {
            config.setShoulderRideMode(!config.isShoulderRideMode());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }

        // Debug
        while (RIDER_POS_Y_INCREASE.wasPressed()) {
            config.setRiderPosY(config.getRiderPosY() + config.getRiderPosYModifier());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }
        while (RIDER_POS_Y_DECREASE.wasPressed()) {
            config.setRiderPosY(config.getRiderPosY() - config.getRiderPosYModifier());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }
        while (RIDER_POS_Z_INCREASE.wasPressed()) {
            config.setRiderPosZ(config.getRiderPosZ() + config.getRiderPosZModifier());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }
        while (RIDER_POS_Z_DECREASE.wasPressed()) {
            config.setRiderPosZ(config.getRiderPosZ() - config.getRiderPosZModifier());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }
    }
}
