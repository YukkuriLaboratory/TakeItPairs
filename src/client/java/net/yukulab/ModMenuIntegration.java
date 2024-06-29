package net.yukulab;

import com.google.common.util.concurrent.AtomicDouble;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Load configurations
            ClientConfig clientConfig = ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$getClientConfig();
            ClientConfig defaultClientConfig = ClientConfig.asDefault();

            // Setup config menu
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.takeitpairs.config"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Render category
            ConfigCategory renderCategory = builder.getOrCreateCategory(Text.translatable("category.takeitpairs.render"));

            AtomicDouble riderPosY = new AtomicDouble(clientConfig.riderPosY());
            renderCategory.addEntry(entryBuilder
                            .startDoubleField(Text.translatable("option.takeitpairs.render.riderposy"), riderPosY.get())
                            .setDefaultValue(defaultClientConfig.riderPosY())
                    .setSaveConsumer(riderPosY::set)
                    .build()
            );

            AtomicDouble riderPosYModifier = new AtomicDouble(clientConfig.riderPosYModifier());
            renderCategory.addEntry(entryBuilder
                    .startDoubleField(Text.translatable("option.takeitpairs.render.riderposy.modifier"), riderPosYModifier.get())
                    .setDefaultValue(defaultClientConfig.riderPosYModifier())
                    .setSaveConsumer(riderPosYModifier::set)
                    .build()
            );

            // Save configuration
            builder.setSavingRunnable(() -> {
                ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$setClientConfig(new ClientConfig(riderPosY.get(), riderPosYModifier.get()));
            });

            return builder.build();
        };
    }
}
