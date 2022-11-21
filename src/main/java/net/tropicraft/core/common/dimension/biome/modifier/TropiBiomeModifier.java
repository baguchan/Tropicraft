package net.tropicraft.core.common.dimension.biome.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;

public class TropiBiomeModifier implements BiomeModifier {
	public static final TropiBiomeModifier INSTANCE = new TropiBiomeModifier();

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && (biome.is(BiomeTags.IS_JUNGLE) && biome.is(BiomeTags.IS_OVERWORLD))) {
			builder.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TropicraftVegetationPlacements.PATCH_PINEAPPLE.getHolder().get());
		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return TropiBiomeModifiers.TROPI_MODIFIER_TYPE.get();
	}
}