package net.yukulab.extension;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public interface TakeItPairs$Feeding {
    default void takeitpairs$startFeeding(ServerPlayerEntity target, ItemStack food, Hand hand) {}
}
