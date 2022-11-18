package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public class TropicraftStructureType<S extends Structure> {

	public static final DeferredRegister<StructureType<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Constants.MODID);

	public static final RegistryObject<StructureType<HomeTreeStructure>> HOME_TREE = DEFERRED_REGISTRY_STRUCTURE.register("home_tree", () -> explicitStructureTypeTyping(HomeTreeStructure.CODEC));
	private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
		return () -> structureCodec;
	}
}
