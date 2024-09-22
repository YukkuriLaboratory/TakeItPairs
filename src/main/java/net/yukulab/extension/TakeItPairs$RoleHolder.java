package net.yukulab.extension;

import net.yukulab.PlayerRole;
import org.jetbrains.annotations.Nullable;

public interface TakeItPairs$RoleHolder {
    default void takeitpairs$setRole(@Nullable PlayerRole role) {
    }

    @Nullable
    default PlayerRole takeitpairs$getRole() {
        return null;
    }
}
