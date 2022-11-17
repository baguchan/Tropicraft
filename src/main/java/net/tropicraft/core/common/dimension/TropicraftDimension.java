package net.tropicraft.core.common.dimension;

import com.mojang.serialization.DataResult;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftSurfaces;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");

    public static final ResourceKey<Level> WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, ID);
    public static final ResourceKey<LevelStem> DIMENSION = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, ID);
    public static final ResourceKey<NoiseGeneratorSettings> DIMENSION_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ID);

    public static final DeferredRegister<NoiseGeneratorSettings> NOISE_GENERATORS = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Constants.MODID);
    public static final DeferredRegister<DimensionType> DIMENSION_TYPES = DeferredRegister.create(Registry.DIMENSION_TYPE_REGISTRY, Constants.MODID);

    public static final RegistryObject<NoiseGeneratorSettings> TROPICS_NOISE_GEN = NOISE_GENERATORS.register("tropics", TropicraftDimension::tropics);
    public static final RegistryObject<DimensionType> TROPICS_DIM_TYPE = DIMENSION_TYPES.register("tropics", TropicraftDimension::tropicsDimensionType);

    static final NoiseSettings TROPI_NOISE_SETTINGS = create(-80, 384, 1, 2);

    public static NoiseGeneratorSettings tropics() {
        return new NoiseGeneratorSettings(TROPI_NOISE_SETTINGS, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), TropicraftNoiseGen.tropics(), TropicraftSurfaces.tropics(), (new TropicraftBiomeBuilder()).spawnTarget(), 64, false, true, false, false);
    }

    private static DimensionType tropicsDimensionType() {
        return new DimensionType(
                OptionalLong.empty(),
                true, //skylight
                false, //ceiling
                false, //ultrawarm
                true, //natural
                1.0D, //coordinate scale
                true, //bed works
                false, //respawn anchor works
                -64,
                384,
                384, // Logical Height
                BlockTags.INFINIBURN_OVERWORLD, //infiburn
                new ResourceLocation("tropicraft:tropics"), // DimensionRenderInfo
                0f, // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 7)
        );
    }

    private static DataResult<NoiseSettings> guardY(NoiseSettings p_158721_) {
        if (p_158721_.minY() + p_158721_.height() > DimensionType.MAX_Y + 1) {
            return DataResult.error("min_y + height cannot be higher than: " + (DimensionType.MAX_Y + 1));
        } else if (p_158721_.height() % 16 != 0) {
            return DataResult.error("height has to be a multiple of 16");
        } else {
            return p_158721_.minY() % 16 != 0 ? DataResult.error("min_y has to be a multiple of 16") : DataResult.success(p_158721_);
        }
    }

    public static NoiseSettings create(int p_224526_, int p_224527_, int p_224528_, int p_224529_) {
        NoiseSettings noisesettings = new NoiseSettings(p_224526_, p_224527_, p_224528_, p_224529_);
        guardY(noisesettings).error().ifPresent((p_158719_) -> {
            throw new IllegalStateException(p_158719_.message());
        });
        return noisesettings;
    }

    /**
     * Method that handles teleporting the player to and from the tropics depending on certain parameters.
     * Finds the top Y position relative to the dimension the player is teleporting to and places the entity at that position.
     * <p>
     * The position will be based on finding the top Y position relative
     * to the dimension the player is teleporting to and places the entity at that position. Avoids portal generation
     * by using player.teleport() instead of player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param dimensionType The Tropicraft Dimension Type for reference
     */
    public static void teleportPlayer(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ServerLevel destLevel = getTeleportDestination(player, dimensionType);
        if (destLevel == null) return;

        ResourceKey<Level> destDimension = destLevel.dimension();
        if (!ForgeHooks.onTravelToDimension(player, destDimension)) return;

        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());

        LevelChunk chunk = destLevel.getChunk(x >> 4, z >> 4);
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);
        player.teleportTo(destLevel, x + 0.5, topY + 1.0, z + 0.5, player.getYRot(), player.getXRot());

        ForgeEventFactory.firePlayerChangedDimensionEvent(player, destDimension, destDimension);
    }

    /**
     * Method that handles teleporting the player to and from the tropics depending on certain parameters.
     * Finds the top Y position relative to the dimension the player is teleporting to and places the entity at that position.
     * <p>
     * A portal will be generated on the players teleport with such position based on the portal's info position.
     *
     * @param player The player that will be teleported
     * @param dimensionType The Tropicraft Dimension Type for reference
     */
    public static void teleportPlayerWithPortal(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ServerLevel destLevel = getTeleportDestination(player, dimensionType);
        if (destLevel == null) return;

        if (!player.isOnPortalCooldown()) {
            player.unRide();
            player.changeDimension(destLevel, new TropicsTeleporter(destLevel));

            //Note: Stops the player from teleporting right after going through the portal
            player.portalCooldown = 160;
        }
    }

    @Nullable
    private static ServerLevel getTeleportDestination(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ResourceKey<Level> destination;
        if (player.level.dimension() == dimensionType) {
            destination = Level.OVERWORLD;
        } else {
            destination = dimensionType;
        }

        ServerLevel destLevel = player.server.getLevel(destination);
        if (destLevel == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.location());
            return null;
        }
        return destLevel;
    }

    // hack to get the correct sea level given a world: the vanilla IWorldReader.getSeaLevel() is deprecated and always returns 63 despite the chunk generator
    public static int getSeaLevel(LevelReader world) {
        if (world instanceof ServerLevel) {
            ServerChunkCache chunkProvider = ((ServerLevel) world).getChunkSource();
            return chunkProvider.getGenerator().getSeaLevel();
        } else if (world instanceof Level) {
            ResourceKey<Level> dimensionKey = ((Level) world).dimension();
            if (dimensionKey == WORLD) {
                return 127;
            }
        }
        return world.getSeaLevel();
    }
}
