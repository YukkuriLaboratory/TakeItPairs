package net.yukulab;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.yukulab.client.extension.TakeItPairsKeyBinds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeItPairsClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("TakeItPairs-Client");
	@Override
	public void onInitializeClient() {
		TakeItPairsKeyBinds.init();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(TakeItPairsKeyBinds::onClientTick);
	}
}