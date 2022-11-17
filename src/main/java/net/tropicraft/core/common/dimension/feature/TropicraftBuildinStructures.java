package net.tropicraft.core.common.dimension.feature;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.tropicraft.Constants;

public interface TropicraftBuildinStructures {
	ResourceKey<Structure> HOME_TREE = createKey("home_tree");
	ResourceKey<Structure> KOA_VILLAGE = createKey("koa_village");

	private static ResourceKey<Structure> createKey(String pName) {
		return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Constants.MODID, pName));
	}
}
