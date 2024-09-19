package net.yukulab.config;

import net.yukulab.util.MathUtil;
import org.jetbrains.annotations.NotNull;

public class ClientConfig {
    private double riderPosY, riderPosYModifier;
    private double riderPosZ, riderPosZModifier;
    private double shoulderModeRiderY, shoulderModeRiderZ;

    private boolean shoulderRideMode, invisibleRiderOnRideMode;

    public ClientConfig(
            double riderPosY,
            double riderPosYModifier,
            double riderPosZ,
            double riderPosZModifier,
            boolean shoulderRideMode,
            double shoulderModeRiderY,
            double shoulderModeRiderZ,
            boolean invisibleRiderOnRideMode
            ) {
        this.riderPosY = riderPosY;
        this.riderPosYModifier = riderPosYModifier;
        this.riderPosZ = riderPosZ;
        this.riderPosZModifier = riderPosZModifier;
        this.shoulderRideMode = shoulderRideMode;
        this.shoulderModeRiderY = shoulderModeRiderY;
        this.shoulderModeRiderZ = shoulderModeRiderZ;
        this.invisibleRiderOnRideMode = invisibleRiderOnRideMode;
    }

    public static @NotNull ClientConfig getDefaultConfig() {
        return new ClientConfig(
                0.2,
                0.1,
                0.0,
                0.1,
                true,
                -0.325,
                -0.5,
                true
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

    public double getShoulderModeRiderY() {
        return shoulderModeRiderY;
    }

    public void setShoulderModeRiderY(double shoulderModeRiderY) {
        this.shoulderModeRiderY = shoulderModeRiderY;
    }

    public double getShoulderModeRiderZ() {
        return shoulderModeRiderZ;
    }

    public void setShoulderModeRiderZ(double shoulderModeRiderZ) {
        this.shoulderModeRiderZ = shoulderModeRiderZ;
    }

    public boolean getInvisibleRiderOnRideMode() {
        return invisibleRiderOnRideMode;
    }

    public void setInvisibleRiderOnRideMode(boolean invisibleRiderOnRideMode) {
        this.invisibleRiderOnRideMode = invisibleRiderOnRideMode;
    }
}
