package net.tropicraft.core.common.dimension.biome.modifier;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public class TropiBiomeModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Constants.MODID);

	public static final RegistryObject<Codec<TropiBiomeModifier>> TROPI_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("tropi_modifier", () -> Codec.unit(TropiBiomeModifier.INSTANCE));

}
