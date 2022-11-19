package net.tropicraft.core.common.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
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
	public static final Codec<TropiChunkGenerator> CODEC = RecordCodecBuilder.create((p_224323_) -> {
		return commonCodec(p_224323_).and(p_224323_.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
			return p_188716_.noises;
		}), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
			return p_188711_.biomeSource;
		}), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_224278_) -> {
			return p_224278_.settings;
		}))).apply(p_224323_, p_224323_.stable(TropiChunkGenerator::new));
	});
	private final Registry<NormalNoise.NoiseParameters> noises;
	public VolcanoGenerator volcanoGenerator;

	public TropiChunkGenerator(Registry<StructureSet> p_224206_, Registry<NormalNoise.NoiseParameters> p_224207_, BiomeSource p_224208_, Holder<NoiseGeneratorSettings> p_224209_) {
		super(p_224206_, p_224207_, p_224208_, p_224209_);
		this.noises = p_224207_;
	}

	/*
	 * When VolcanoGenerator's server start up event is success. fill the volcano
	 */
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
		return super.fillFromNoise(pExecutor, pBlender, pRandom, pStructureManager, pChunk).thenApply(volcanoChunk -> {
			if (this.volcanoGenerator != null) {
				ChunkPos chunkPos = volcanoChunk.getPos();
				WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(pRandom.legacyLevelSeed()));
				// keysmashes ftw
				volcanoGenerator.generate(chunkPos.x, chunkPos.z, pChunk, random);
			}
			return volcanoChunk;
		});
	}

	public VolcanoGenerator getVolcano() {
		return volcanoGenerator;
	}
}
