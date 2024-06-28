package net.yukulab;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeItPairsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("TakeItPairs-Client");
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}