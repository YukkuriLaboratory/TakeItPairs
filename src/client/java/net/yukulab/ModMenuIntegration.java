package net.yukulab;

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
            // === Load Config ===
            ClientConfig clientConfig = ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$getClientConfig();
            ClientConfig defaultClientConfig = ClientConfig.getDefaultConfig();

            // === Menu ===
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.takeitpairs.config"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Render Category
            ConfigCategory renderCategory = builder.getOrCreateCategory(Text.translatable("category.takeitpairs.render"));

            renderCategory.addEntry(entryBuilder
                    .startBooleanToggle(Text.translatable("option.takeitpairs.render.ride_on_shoulders"), clientConfig.isShoulderRideMode())
                    .setDefaultValue(defaultClientConfig.isShoulderRideMode())
                    .setSaveConsumer(clientConfig::setShoulderRideMode)
                    .build()
            );

            // Debug Category
            ConfigCategory debugCategory = builder.getOrCreateCategory(Text.translatable("category.takeitpairs.debug"));

            // Rider Pos Y
            debugCategory.addEntry(entryBuilder
                    .startDoubleField(Text.translatable("option.takeitpairs.debug.riderpos.y"), clientConfig.getRiderPosY())
                    .setDefaultValue(defaultClientConfig.getRiderPosY())
                    .setSaveConsumer(clientConfig::setRiderPosY)
                    .build()
            );
            debugCategory.addEntry(entryBuilder
                    .startDoubleField(Text.translatable("option.takeitpairs.debug.riderpos.y.modifier"), clientConfig.getRiderPosYModifier())
                    .setDefaultValue(defaultClientConfig.getRiderPosYModifier())
                    .setSaveConsumer(clientConfig::setRiderPosYModifier)
                    .build()
            );

            // Rider Pos Z
            debugCategory.addEntry(entryBuilder
                    .startDoubleField(Text.translatable("option.takeitpairs.debug.riderpos.z"), clientConfig.getRiderPosZ())
                    .setDefaultValue(defaultClientConfig.getRiderPosZ())
                    .setSaveConsumer(clientConfig::setRiderPosZ)
                    .build()
            );
            debugCategory.addEntry(entryBuilder
                    .startDoubleField(Text.translatable("option.takeitpairs.debug.riderpos.z.modifier"), clientConfig.getRiderPosZModifier())
                    .setDefaultValue(defaultClientConfig.getRiderPosZModifier())
                    .setSaveConsumer(clientConfig::setRiderPosZModifier)
                    .build()
            );


            // === Save Config ===
            builder.setSavingRunnable(() -> {
                ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$updateClientConfig();
            });

            return builder.build();
        };
    }
}
