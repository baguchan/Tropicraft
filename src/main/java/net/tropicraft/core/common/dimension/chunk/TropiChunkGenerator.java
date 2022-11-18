package net.tropicraft.core.common.dimension.chunk;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TropiChunkGenerator extends NoiseBasedChunkGenerator {
	public VolcanoGenerator volcanoGenerator;

	public TropiChunkGenerator(Registry<StructureSet> p_224206_, Registry<NormalNoise.NoiseParameters> p_224207_, BiomeSource p_224208_, Holder<NoiseGeneratorSettings> p_224209_) {
		super(p_224206_, p_224207_, p_224208_, p_224209_);
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
		if (this.volcanoGenerator != null) {
			this.volcanoGenerator = new VolcanoGenerator(pRandom.legacyLevelSeed(), this.biomeSource, this, pRandom);
		}

		return super.fillFromNoise(pExecutor, pBlender, pRandom, pStructureManager, pChunk).thenApply(volcanoChunk -> {
			ChunkPos chunkPos = volcanoChunk.getPos();
			WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(pRandom.legacyLevelSeed()));
			// keysmashes ftw
			random.setFeatureSeed(chunkPos.toLong(), 59317, 31931);
			volcanoGenerator.generate(chunkPos.x, chunkPos.z, pChunk, random);
			return volcanoChunk;
		});
	}
}
