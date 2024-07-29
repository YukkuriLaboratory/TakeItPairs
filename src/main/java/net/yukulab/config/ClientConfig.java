package net.yukulab.config;

public record ClientConfig(double riderPosY, double riderPosYModifier, double riderPosZ, double riderPosZModifier, boolean rideOnShoulders) {
    public static ClientConfig asDefault() {
        return new ClientConfig(0.2, 0.1, 0.0, 0.1, false);
    }
}
