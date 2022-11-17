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

	/**
	 * Registers the base structure itself and sets what its path is. In this case,
	 * this base structure will have the resourcelocation of structure_tutorial:sky_structures.
	 */
	public static final RegistryObject<StructureType<HomeTreeStructure>> HOME_TREE = DEFERRED_REGISTRY_STRUCTURE.register("sky_structures", () -> explicitStructureTypeTyping(HomeTreeStructure.CODEC));

	/**
	 * Originally, I had a double lambda ()->()-> for the RegistryObject line above, but it turns out that
	 * some IDEs cannot resolve the typing correctly. This method explicitly states what the return type
	 * is so that the IDE can put it into the DeferredRegistry properly.
	 */
	private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
		return () -> structureCodec;
	}
}
