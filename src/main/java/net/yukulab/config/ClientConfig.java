package net.yukulab.config;

public record ClientConfig(double riderPosY) {
    public static ClientConfig asDefault() {
        return new ClientConfig(0.0);
    }
}
