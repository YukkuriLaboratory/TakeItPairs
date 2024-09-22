package net.yukulab.extension;

public interface TakeItPairs$StateHolder {
    default void takeitpairs$disableMovement(boolean disable) {
    }

    default boolean takeitpairs$isMovementDisabled() {
        return false;
    }

    default void takeitpairs$disableClick(boolean disable) {
    }

    default boolean takeitpairs$isClickDisabled() {
        return false;
    }
}
