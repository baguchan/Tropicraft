package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;

public final class TropicraftConfiguredCarvers {
    public static final DeferredRegister<ConfiguredWorldCarver<?>> REGISTER = DeferredRegister.create(Registry.CONFIGURED_CARVER_REGISTRY, Constants.MODID);

    public static final RegistryObject<ConfiguredWorldCarver<?>> CAVE = register("cave", TropicraftCarvers.CAVE,
            new CaveCarverConfiguration(
                    0.25F, UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(180)), UniformFloat.of(0.1F, 0.9F), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()), Registry.BLOCK.getOrCreateTag(TropicraftTags.Blocks.TROPICS_CARVER_REPLACEABLES), UniformFloat.of(0.7F, 1.4F), UniformFloat.of(0.8F, 1.3F), UniformFloat.of(-1.0F, -0.4F)));

    public static final RegistryObject<ConfiguredWorldCarver<?>> CANYON = register("canyon", TropicraftCarvers.CANYON,
            new CanyonCarverConfiguration(
                    0.02F, UniformHeight.of(VerticalAnchor.absolute(10), VerticalAnchor.absolute(67)), ConstantFloat.of(3.0F), VerticalAnchor.aboveBottom(8), CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()), Registry.BLOCK.getOrCreateTag(TropicraftTags.Blocks.TROPICS_CARVER_REPLACEABLES), UniformFloat.of(-0.125F, 0.125F), new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F), TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F)));

    public static void addLand(BiomeGenerationSettings.Builder generation) {
        generation.addCarver(GenerationStep.Carving.AIR, CAVE.getHolder().orElseThrow());
        generation.addCarver(GenerationStep.Carving.AIR, CANYON.getHolder().orElseThrow());
    }

    @Deprecated
    public static void addUnderwater(BiomeGenerationSettings.Builder generation) {
    }

    public static <C extends CarverConfiguration, WC extends WorldCarver<C>> RegistryObject<ConfiguredWorldCarver<?>> register(String id, RegistryObject<WC> carver, C config) {
        return REGISTER.register(id, () -> carver.get().configured(config));
    }
}
