package net.tropicraft.core.common.dimension.feature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.tree.CurvedPalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.LargePalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.NormalPalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.PalmTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.RainforestTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.RainforestVinesFeature;
import net.tropicraft.core.common.dimension.feature.tree.TallRainforestTreeFeature;
import net.tropicraft.core.common.dimension.feature.tree.TualungFeature;
import net.tropicraft.core.common.dimension.feature.tree.UpTreeFeature;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TropicraftFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);
    public static final RegistryObject<PalmTreeFeature> NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<PalmTreeFeature> LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> UP_TREE = register("up_tree", () -> new UpTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestTreeFeature> SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 16, 9));
    public static final RegistryObject<RainforestTreeFeature> LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 25, 11));
    public static final RegistryObject<RainforestTreeFeature> TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<EIHFeature> EIH = register("eih", () -> new EIHFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<UndergrowthFeature> UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<SingleUndergrowthFeature> SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RainforestVinesFeature> VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final RegistryObject<UndergroundSeaPickleFeature> UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoneFeatureConfiguration.CODEC));

    public static final Holder<Structure> KOA_VILLAGE = register(TropicraftBuildinStructures.KOA_VILLAGE, new JigsawStructure(structure(TropicraftTags.Biomes.HAS_KOA_VILLAGE, TerrainAdjustment.NONE), holderOf(TropicraftTemplatePools.KOA_TOWN_CENTERS), 6, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));

    public static final Holder<Structure> HOME_TREE = register(TropicraftBuildinStructures.HOME_TREE, new JigsawStructure(structure(TropicraftTags.Biomes.HAS_HOME_TREE, TerrainAdjustment.NONE), holderOf(TropicraftTemplatePools.HOME_TREE_STARTS), 4, ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));

	public static final RegistryObject<CoffeePlantFeature> COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<ReedsFeature> REEDS = register("reeds", () -> new ReedsFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<HugePlantFeature> HUGE_PLANT = register("huge_plant", () -> new HugePlantFeature(SimpleBlockConfiguration.CODEC));

    private static <T extends Feature<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return FEATURES.register(name, sup);
    }

    private static Holder<Structure> register(ResourceKey<Structure> pKey, Structure pStructure) {
        return BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, pKey, pStructure);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> pKey, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pDecoration, TerrainAdjustment pTerrainAdjustment) {
        return new Structure.StructureSettings(biomes(pKey), pSpawnOverrides, pDecoration, pTerrainAdjustment);
    }

    private static HolderSet<Biome> biomes(TagKey<Biome> pKey) {
        return BuiltinRegistries.BIOME.getOrCreateTag(pKey);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> pKey, GenerationStep.Decoration pDecoration, TerrainAdjustment pTerrainAdjustment) {
        return structure(pKey, Map.of(), pDecoration, pTerrainAdjustment);
    }

    private static Structure.StructureSettings structure(TagKey<Biome> pKey, TerrainAdjustment pTerrainAdjustment) {
        return structure(pKey, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, pTerrainAdjustment);
    }

    //register those Builtin Structure
    public static void init() {

    }


    public static <T> Holder<T> holderOf(RegistryObject<T> object) {
        return object.getHolder().orElseThrow();
    }

    public static class Register {
        private final DeferredRegister<ConfiguredFeature<?, ?>> features = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Constants.MODID);
        private final DeferredRegister<PlacedFeature> placed = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Constants.MODID);

        private Register() {
        }

        public static Register create() {
            return new Register();
        }

        public void registerTo(IEventBus bus) {
            features.register(bus);
            placed.register(bus);
        }

        public <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<ConfiguredFeature<?, ?>> feature(String id, F feature, Supplier<C> config) {
            return features.register(id, () -> new ConfiguredFeature<>(feature, config.get()));
        }

        public <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<ConfiguredFeature<?, ?>> feature(String id, Supplier<F> feature, Supplier<C> config) {
            return features.register(id, () -> new ConfiguredFeature<>(feature.get(), config.get()));
        }

        public <C extends FeatureConfiguration> RegistryObject<ConfiguredFeature<?, ?>> copyFeature(String id, Holder<ConfiguredFeature<C, ?>> source) {
            return features.register(id, () -> {
                final ConfiguredFeature<C, ?> value = source.value();
                return new ConfiguredFeature<>(value.feature(), value.config());
            });
        }

        public <F extends Feature<NoneFeatureConfiguration>> RegistryObject<ConfiguredFeature<?, ?>> feature(String id, Supplier<F> feature) {
            return feature(id, feature, () -> NoneFeatureConfiguration.INSTANCE);
        }

        public RegistryObject<ConfiguredFeature<?, ?>> randomFeature(String id, Supplier<List<Holder<PlacedFeature>>> choicesSupplier) {
            return features.register(id, () -> {
                final List<Holder<PlacedFeature>> choices = choicesSupplier.get();
                if (choices.size() == 2) {
                    Holder<PlacedFeature> left = choices.get(0);
                    Holder<PlacedFeature> right = choices.get(1);
                    return new ConfiguredFeature<>(Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(left, right));
                } else {
                    return new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(choices)));
                }
            });
        }

        public RegistryObject<ConfiguredFeature<?, ?>> randomFeature(String id, List<Pair<RegistryObject<PlacedFeature>, Float>> choices, RegistryObject<PlacedFeature> defaultFeature) {
            return randomFeature(id,
                    () -> choices.stream().map(pair -> {
                        final Holder<PlacedFeature> holder = TropicraftFeatures.holderOf(pair.getFirst());
                        return new WeightedPlacedFeature(holder, pair.getSecond());
                    }).toList(),
                    () -> TropicraftFeatures.holderOf(defaultFeature)
            );
        }

        public RegistryObject<ConfiguredFeature<?, ?>> randomFeature(String id, Supplier<List<WeightedPlacedFeature>> choicesSupplier, Supplier<Holder<PlacedFeature>> defaultFeature) {
            return features.register(id, () -> {
                final List<WeightedPlacedFeature> choices = choicesSupplier.get();
                return new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(choices, defaultFeature.get()));
            });
        }

        @SafeVarargs
        public final RegistryObject<ConfiguredFeature<?, ?>> randomFeature(String id, RegistryObject<ConfiguredFeature<?, ?>>... choices) {
            return randomFeature(id, () -> Arrays.stream(choices).map(this::inlinePlaced).toList());
        }

        @SafeVarargs
        public final RegistryObject<ConfiguredFeature<?, ?>> randomPlacedFeature(String id, RegistryObject<PlacedFeature>... choices) {
            return randomFeature(id, () -> Arrays.stream(choices).map(TropicraftFeatures::holderOf).toList());
        }

        @SafeVarargs
        public final RegistryObject<PlacedFeature> randomChecked(String id, RegistryObject<PlacedFeature>... choices) {
            final RegistryObject<ConfiguredFeature<?, ?>> configured = randomPlacedFeature(id, choices);
            return placed.register(id, () -> new PlacedFeature(holderOf(configured), List.of()));
        }

        public RandomPatchConfiguration randomPatch(final Supplier<? extends Block> block) {
            return randomPatch(BlockStateProvider.simple(block.get()));
        }

        public RandomPatchConfiguration randomPatch(final BlockStateProvider blockStateProvider) {
            return FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider));
        }

        public OreConfiguration ore(final int blobSize, final Supplier<? extends Block> block) {
            // TODO add deepslate / tropicraft equivalent replacement here
            return new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState(), blobSize);
        }

        public Holder<PlacedFeature> inlinePlaced(RegistryObject<ConfiguredFeature<?, ?>> object) {
            return PlacementUtils.inlinePlaced(holderOf(object));
        }

        public BlockStateProvider stateProvider(Supplier<? extends Block> block) {
            return BlockStateProvider.simple(block.get());
        }

        public RegistryObject<PlacedFeature> placed(String id, RegistryObject<ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> placement) {
            return placed.register(id, () -> new PlacedFeature(holderOf(feature), placement.get()));
        }

        public List<PlacementModifier> sparseTreePlacement(float chance) {
            return treePlacement(0, chance, 1);
        }

        public List<PlacementModifier> treePlacement(int count, float extraChance, int extraCount) {
            return List.of(
                    PlacementUtils.countExtra(count, extraChance, extraCount),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            );
        }

        public List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier height) {
            return orePlacement(RarityFilter.onAverageOnceEvery(rarity), height);
        }

        public List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
            return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
        }

        public List<PlacementModifier> commonOrePlacement(int rarity, PlacementModifier height) {
            return orePlacement(CountPlacement.of(rarity), height);
        }
    }
}
