package net.yukulab.network.packet.play;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.yukulab.extension.TakeItPairs$StateHolder;
import net.yukulab.network.Networking;

public class SyncStateC2SPacket implements CustomPayload {
    public static final Id<SyncStateC2SPacket> ID = new Id<>(Networking.SYNC_DISABLE_STATE);
    public static final PacketCodec<RegistryByteBuf, SyncStateC2SPacket> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeBoolean(value.disableMovement);
                buf.writeBoolean(value.disableClick);
            },
            buf -> new SyncStateC2SPacket(buf.readBoolean(), buf.readBoolean())
    );

    private final boolean disableMovement;
    private final boolean disableClick;

    public SyncStateC2SPacket(boolean disableMovement, boolean disableClick) {
        this.disableMovement = disableMovement;
        this.disableClick = disableClick;
    }

    public boolean isDisableMovement() {
        return disableMovement;
    }

    public boolean isDisableClick() {
        return disableClick;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void onReceive(SyncStateC2SPacket payload, ServerPlayNetworking.Context context) {
        var player = context.player();
        if (player instanceof TakeItPairs$StateHolder holder) {
            holder.takeitpairs$disableMovement(payload.isDisableMovement());
            holder.takeitpairs$disableClick(payload.isDisableClick());
        }
    }
}
