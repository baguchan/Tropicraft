package net.tropicraft.core.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import net.tropicraft.core.common.dimension.chunk.TropiChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerators.class)
public class ChunkGeneratorsMixin {
	@Inject(method = "bootstrap", at = @At("HEAD"))
	private static void bootstrap(Registry<Codec<? extends ChunkGenerator>> pRegistry, CallbackInfoReturnable<Codec<? extends ChunkGenerator>> callbackInfoReturnable) {
		Registry.register(pRegistry, "tropicraft:tropics", TropiChunkGenerator.CODEC);
	}
}