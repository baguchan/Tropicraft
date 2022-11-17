package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

public class TropicraftBiomeTagsProvider extends BiomeTagsProvider {

    public TropicraftBiomeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Add forge tags for our ores
        tag(TropicraftTags.Biomes.HAS_HOME_TREE).add(TropicraftBiomes.RAINFOREST.get(), TropicraftBiomes.BAMBOO_RAINFOREST.get(), TropicraftBiomes.OSA_RAINFOREST.get());
        tag(TropicraftTags.Biomes.HAS_KOA_VILLAGE).add(TropicraftBiomes.BEACH.get());
    }


    @Override
    public String getName() {
        return "Tropicraft Biome Tags";
    }
}
