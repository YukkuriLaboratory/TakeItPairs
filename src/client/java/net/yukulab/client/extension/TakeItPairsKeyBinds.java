package net.yukulab.client.extension;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.yukulab.extension.TakeItPairs$StateHolder;
import net.yukulab.network.packet.play.SyncStateC2SPacket;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class TakeItPairsKeyBinds {
    private static final String GENERAL_CATEGORY = "key.takeitpairs.category.render",
            DISABLE_CATEGORY = "key.takeitpairs.category.disable",
            DEBUG_CATEGORY = "key.takeitpairs.category.debug"; // TODO add to lang file
    private static KeyBinding
            RIDER_TOGGLE_INVISIBLE,
            RIDER_POS_Y_INCREASE,
            RIDER_POS_Y_DECREASE,
            RIDER_POS_Z_INCREASE,
            RIDER_POS_Z_DECREASE,
            RIDE_ON_SHOULDERS,
            DISABLE_MOVEMENT,
            DISABLE_CLICK;

    public static void init() {
        // TODO needs to change translation key in lang file
        RIDER_TOGGLE_INVISIBLE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.rider.invisible",
                GLFW.GLFW_KEY_UNKNOWN,
                GENERAL_CATEGORY
        ));
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

        // Disable
        DISABLE_MOVEMENT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.disable.movement",
                GLFW.GLFW_KEY_UNKNOWN,
                DISABLE_CATEGORY
        ));
        DISABLE_CLICK = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.takeitpairs.disable.click",
                GLFW.GLFW_KEY_UNKNOWN,
                DISABLE_CATEGORY
        ));
    }

    public static void onClientTick(MinecraftClient client) {
        var config = ((TakeItPairs$ClientConfigHolder) client).takeitpairs$getClientConfig();

        // Render
        while (RIDE_ON_SHOULDERS.wasPressed()) {
            config.setShoulderRideMode(!config.isShoulderRideMode());
            ((TakeItPairs$ClientConfigHolder) client).takeitpairs$updateClientConfig();
        }
        while (RIDER_TOGGLE_INVISIBLE.wasPressed()) {
            config.setInvisibleRiderOnRideMode(!config.getInvisibleRiderOnRideMode());
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

        while (DISABLE_MOVEMENT.wasPressed()) {
            var player = client.player;
            if (player instanceof TakeItPairs$StateHolder holder) {
                holder.takeitpairs$disableMovement(!holder.takeitpairs$isMovementDisabled());
                var key = holder.takeitpairs$isMovementDisabled() ? "notify.takeitpairs.disable.movement" : "notify.takeitpairs.enable.movement";
                var message = Text.translatable(key);
                player.sendMessage(message, true);
                ClientPlayNetworking.send(new SyncStateC2SPacket(holder.takeitpairs$isMovementDisabled(), holder.takeitpairs$isClickDisabled()));
            }
        }

        while (DISABLE_CLICK.wasPressed()) {
            var player = client.player;
            if (player instanceof TakeItPairs$StateHolder holder) {
                holder.takeitpairs$disableClick(!holder.takeitpairs$isClickDisabled());
                var key = holder.takeitpairs$isClickDisabled() ? "notify.takeitpairs.disable.click" : "notify.takeitpairs.enable.click";
                var message = Text.translatable(key);
                player.sendMessage(message, true);
                ClientPlayNetworking.send(new SyncStateC2SPacket(holder.takeitpairs$isMovementDisabled(), holder.takeitpairs$isClickDisabled()));
            }
        }
    }
}
