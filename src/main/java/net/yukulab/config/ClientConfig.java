package net.yukulab.config;

public record ClientConfig(double riderPosY, double riderPosYModifier) {
    public static ClientConfig asDefault() {
        return new ClientConfig(-0.2, 0.1);
    }
}
