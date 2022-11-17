package net.tropicraft.core.common.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.function.Supplier;

public final class TropicraftLogBlock extends RotatedPillarBlock {
    private final Supplier<RotatedPillarBlock> strippedBlock;

    public TropicraftLogBlock(Properties properties, Supplier<RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP) {
            return this.strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return null;
    }
}
