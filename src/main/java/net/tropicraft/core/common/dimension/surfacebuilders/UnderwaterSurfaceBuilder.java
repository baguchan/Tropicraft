package net.tropicraft.core.common.dimension.surfacebuilders;

public class UnderwaterSurfaceBuilder {// extends SurfaceBuilder<UnderwaterSurfaceBuilder.Config> {
//    public UnderwaterSurfaceBuilder(Codec<Config> codec) {
//        super(codec);
//    }
//
//    @Override
//    public void apply(RandomSource random, ChunkAccess chunk, Biome biome, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, Config config) {
//        SurfaceBuilderBaseConfiguration selectedConfig = config.beach;
//        if (startHeight > seaLevel + 5) {
//            selectedConfig = config.land;
//        }
//        if (chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) + 1 < seaLevel) {
//            selectedConfig = config.underwater;
//        }
//
//        SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minSurfaceLevel, seed, selectedConfig);
//    }
//
//    public static final class Config implements SurfaceBuilderConfiguration {
//        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> {
//            return instance.group(
//                    SurfaceBuilderBaseConfiguration.CODEC.fieldOf("beach").forGetter(c -> c.beach),
//                    SurfaceBuilderBaseConfiguration.CODEC.fieldOf("land").forGetter(c -> c.land),
//                    SurfaceBuilderBaseConfiguration.CODEC.fieldOf("underwater").forGetter(c -> c.underwater)
//            ).apply(instance, Config::new);
//        });
//
//        public final SurfaceBuilderBaseConfiguration beach;
//        public final SurfaceBuilderBaseConfiguration land;
//        public final SurfaceBuilderBaseConfiguration underwater;
//
//        public Config(SurfaceBuilderBaseConfiguration beach, SurfaceBuilderBaseConfiguration land, SurfaceBuilderBaseConfiguration underwater) {
//            this.beach = beach;
//            this.land = land;
//            this.underwater = underwater;
//        }
//
//        @Override
//        public BlockState getTopMaterial() {
//            return this.beach.getTopMaterial();
//        }
//
//        @Override
//        public BlockState getUnderMaterial() {
//            return this.beach.getUnderMaterial();
//        }
//
//        @Override
//        public BlockState getUnderwaterMaterial() {
//            return this.underwater.getUnderwaterMaterial();
//        }
//    }
}
