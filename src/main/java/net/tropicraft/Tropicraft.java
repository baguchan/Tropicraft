package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.TropicraftBlockEntityTypes;
import net.tropicraft.core.common.command.TropicraftCommands;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.data.TropicraftBiomeTagsProvider;
import net.tropicraft.core.common.data.TropicraftBlockTagsProvider;
import net.tropicraft.core.common.data.TropicraftEntityTypeTagsProvider;
import net.tropicraft.core.common.data.TropicraftItemTagsProvider;
import net.tropicraft.core.common.data.TropicraftLootTableProvider;
import net.tropicraft.core.common.data.TropicraftRecipeProvider;
import net.tropicraft.core.common.data.loot.TropicraftLootConditions;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.biome.modifier.TropiBiomeModifiers;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftMiscPlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureSets;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureType;
import net.tropicraft.core.common.dimension.feature.TropicraftTreeFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftTreePlacements;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftVegetationPlacements;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.AirToCaveAirProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SinkInGroundProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureVoidProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorTypes;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePieceTypes;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePoolElementTypes;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.network.TropicraftPackets;

import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final CreativeModeTab TROPICRAFT_ITEM_GROUP = (new CreativeModeTab("tropicraft") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(TropicraftBlocks.PALM_SAPLING.get());
        }
    });

    public Tropicraft() {
        // Compatible with all versions that match the semver (excluding the qualifier e.g. "-beta+42")
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(Tropicraft::getCompatVersion, (s, v) -> Tropicraft.isCompatibleVersion(s)));
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // General mod setup
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modBus.addListener(this::setupClient);
            modBus.addListener(this::registerItemColors);
        });

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);

        // Registry objects
        TropicraftBlocks.BLOCKS.register(modBus);
        TropicraftBlocks.BLOCKITEMS.register(modBus);
        TropicraftItems.ITEMS.register(modBus);
        ScubaGogglesItem.ATTRIBUTES.register(modBus);
        MixerRecipes.addMixerRecipes();
        TropicraftBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        TropicraftEntities.ENTITIES.register(modBus);
        TropicraftCarvers.CARVERS.register(modBus);
        TropicraftFoliagePlacers.REGISTER.register(modBus);
        TropicraftTrunkPlacers.REGISTER.register(modBus);
        TropicraftTreeDecorators.REGISTER.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftBlockStateProviders.BLOCK_STATE_PROVIDERS.register(modBus);
        TropicraftStructurePieceTypes.REGISTER.register(modBus);
        TropicraftStructurePoolElementTypes.REGISTER.register(modBus);
        TropicraftProcessorTypes.REGISTER.register(modBus);
        TropicraftDimension.DIMENSION_TYPES.register(modBus);
        TropicraftDimension.NOISE_GENERATORS.register(modBus);

        TropicraftMiscFeatures.REGISTER.registerTo(modBus);
        TropicraftMiscPlacements.REGISTER.registerTo(modBus);
		TropicraftTreeFeatures.REGISTER.registerTo(modBus);
		TropicraftTreePlacements.REGISTER.registerTo(modBus);
		TropicraftVegetationFeatures.REGISTER.registerTo(modBus);
		TropicraftVegetationPlacements.REGISTER.registerTo(modBus);
		TropicraftProcessorLists.REGISTER.register(modBus);
		TropicraftConfiguredCarvers.REGISTER.register(modBus);
		TropicraftTemplatePools.REGISTER.register(modBus);
		TropicraftBiomes.REGISTER.register(modBus);
		TropicraftStructureSets.REGISTER.register(modBus);
		TropicraftStructureType.DEFERRED_REGISTRY_STRUCTURE.register(modBus);
		TropiBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modBus);


		// Hack in our item frame models the way vanilla does
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);

			ModelBakery.STATIC_DEFINITIONS = ImmutableMap.<ResourceLocation, StateDefinition<Block, BlockState>>builder()
					.putAll(ModelBakery.STATIC_DEFINITIONS)
					.put(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState)
					.build();
        });
    }

    private static final Pattern QUALIFIER = Pattern.compile("-\\w+\\+\\d+");

    public static String getCompatVersion() {
        return getCompatVersion(ModList.get().getModContainerById(Constants.MODID).orElseThrow(IllegalStateException::new).getModInfo().getVersion().toString());
    }

    private static String getCompatVersion(String fullVersion) {
        return QUALIFIER.matcher(fullVersion).replaceAll("");
    }

    public static boolean isCompatibleVersion(String version) {
        return getCompatVersion().equals(getCompatVersion(version));
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        ClientSetup.setupBlockRenderLayers();

        ClientSetup.setupDimensionRenderInfo();
    }

    @OnlyIn(Dist.CLIENT)
    private void registerItemColors(RegisterColorHandlersEvent.Item evt) {
        BasicColorHandler basic = new BasicColorHandler();
        for (RegistryObject<Item> ro : TropicraftItems.ITEMS.getEntries()) {
            Item item = ro.get();
            if (item instanceof IColoredItem) {
                evt.register(basic, item);
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        TropicraftPackets.init();
        TropicraftEntities.registerSpawns();
        TropicraftFeatures.init();
        TropicraftBiomes.init();

        Reflection.initialize(
                SingleNoAirJigsawPiece.class, NoRotateSingleJigsawPiece.class, HomeTreeBranchPiece.class,
                AdjustBuildingHeightProcessor.class, AirToCaveAirProcessor.class, SinkInGroundProcessor.class,
                SmoothingGravityProcessor.class, SteepPathProcessor.class, StructureSupportsProcessor.class,
                StructureVoidProcessor.class,
                TropicraftTrunkPlacers.class,
                TropicraftFoliagePlacers.class,
                TropicraftTreeDecorators.class,
                TropicraftLootConditions.class
        );
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ScubaData.class);
    }

    private void onServerStarting(final ServerStartingEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            TropicraftBlockstateProvider blockstates = new TropicraftBlockstateProvider(gen, existingFileHelper);
            gen.addProvider(event.includeClient(), blockstates);
            gen.addProvider(event.includeClient(), new TropicraftItemModelProvider(gen, blockstates.getExistingHelper()));
            gen.addProvider(event.includeClient(), new TropicraftLangProvider(gen));
        }
        if (event.includeServer()) {
            TropicraftBlockTagsProvider blockTags = new TropicraftBlockTagsProvider(gen, existingFileHelper);
            gen.addProvider(event.includeServer(), blockTags);
            gen.addProvider(event.includeServer(), new TropicraftItemTagsProvider(gen, blockTags, existingFileHelper));
            gen.addProvider(event.includeServer(), new TropicraftBiomeTagsProvider(gen, existingFileHelper));
            gen.addProvider(event.includeServer(), new TropicraftRecipeProvider(gen));
            gen.addProvider(event.includeServer(), new TropicraftLootTableProvider(gen));
            gen.addProvider(event.includeServer(), new TropicraftEntityTypeTagsProvider(gen, existingFileHelper));
        }
    }
}
