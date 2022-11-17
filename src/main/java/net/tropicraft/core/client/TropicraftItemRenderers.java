package net.tropicraft.core.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tropicraft.core.client.tileentity.SimpleItemStackRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.function.Supplier;

public final class TropicraftItemRenderers {
    public static IClientItemExtensions bambooChest() {
        return renderItemAsBlockEntity(() -> new BambooChestBlockEntity(BlockPos.ZERO, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState()));
    }

    public static IClientItemExtensions drinkMixer() {
        return renderItemAsBlockEntity(() -> new DrinkMixerBlockEntity(BlockPos.ZERO, TropicraftBlocks.DRINK_MIXER.get().defaultBlockState()));
    }

    public static IClientItemExtensions airCompressor() {
        return renderItemAsBlockEntity(() -> new AirCompressorBlockEntity(BlockPos.ZERO, TropicraftBlocks.AIR_COMPRESSOR.get().defaultBlockState()));
    }

    public static IClientItemExtensions renderItemAsBlockEntity(Supplier<BlockEntity> blockEntityFactory) {
        return renderItem(() -> new SimpleItemStackRenderer<>(blockEntityFactory));
    }

    public static IClientItemExtensions renderItem(Supplier<BlockEntityWithoutLevelRenderer> rendererFactory) {
        return new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return rendererFactory.get();
            }
        };
    }
}
