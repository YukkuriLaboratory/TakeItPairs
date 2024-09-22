package net.yukulab;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.yukulab.extension.TakeItPairs$RoleHolder;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.command.argument.EntityArgumentType.player;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class TakeItPairCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("takeitpairs")
                        .then(literal("role").then(
                                argument("target", player()).then(
                                        literal("carrier").executes(context -> {
                                            var holder = (TakeItPairs$RoleHolder) getPlayer(context, "target");
                                            holder.takeitpairs$setRole(PlayerRole.CARRIER);
                                            return 1;
                                        })
                                ).then(
                                        literal("rider").executes(context -> {
                                            var holder = (TakeItPairs$RoleHolder) getPlayer(context, "target");
                                            holder.takeitpairs$setRole(PlayerRole.RIDER);
                                            return 1;
                                        })
                                ).then(
                                        literal("reset").executes(context -> {
                                            var holder = (TakeItPairs$RoleHolder) getPlayer(context, "target");
                                            holder.takeitpairs$setRole(null);
                                            return 1;
                                        })
                                )
                        ))
        )));
    }
}
