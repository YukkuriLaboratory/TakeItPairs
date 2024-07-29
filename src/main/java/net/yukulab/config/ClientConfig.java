package net.yukulab.config;

import net.yukulab.util.MathUtil;
import org.jetbrains.annotations.NotNull;

public class ClientConfig {
    private double riderPosY, riderPosYModifier;
    private double riderPosZ, riderPosZModifier;
    private boolean shoulderRideMode;

    public ClientConfig(double riderPosY, double riderPosYModifier, double riderPosZ, double riderPosZModifier, boolean shoulderRideMode) {
        this.riderPosY = riderPosY;
        this.riderPosYModifier = riderPosYModifier;
        this.riderPosZ = riderPosZ;
        this.riderPosZModifier = riderPosZModifier;
        this.shoulderRideMode = shoulderRideMode;
    }

    public static @NotNull ClientConfig getDefaultConfig() {
        return new ClientConfig(
                0.2,
                0.1,
                0.0,
                0.1,
                false
        );
    }

    public double getRiderPosY() {
        return riderPosY;
    }

    public void setRiderPosY(double riderPosY) {
        this.riderPosY = MathUtil.floor(3, riderPosY);
    }

    public double getRiderPosYModifier() {
        return riderPosYModifier;
    }

    public void setRiderPosYModifier(double riderPosYModifier) {
        this.riderPosYModifier = MathUtil.round(3, riderPosYModifier);
    }

    public double getRiderPosZ() {
        return riderPosZ;
    }

    public void setRiderPosZ(double riderPosZ) {
        this.riderPosZ = MathUtil.floor(3, riderPosZ);
    }

    public double getRiderPosZModifier() {
        return riderPosZModifier;
    }

    public void setRiderPosZModifier(double riderPosZModifier) {
        this.riderPosZModifier = MathUtil.round(3, riderPosZModifier);
    }

    public boolean isShoulderRideMode() {
        return shoulderRideMode;
    }

    public void setShoulderRideMode(boolean hasShoulderRideMode) {
        this.shoulderRideMode = hasShoulderRideMode;
    }
}
