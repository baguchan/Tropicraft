package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;

public final class PapayaTreeDecorator extends TreeDecorator {
    public static final Codec<PapayaTreeDecorator> CODEC = Codec.unit(new PapayaTreeDecorator());

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.PAPAYA.get();
    }

    @Override
    public void place(Context pContext) {
        int y = pContext.logs().get(pContext.logs().size() - 1).getY();

        for (BlockPos log : pContext.logs()) {
            if (log.getY() > y - 4) {
                if (pContext.random().nextInt(2) == 0) {
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pContext.random());

                    BlockPos pos = log.relative(direction);

                    if (pContext.isAir(pos)) {
                        BlockState blockstate = TropicraftBlocks.PAPAYA.get().defaultBlockState()
                                .setValue(PapayaBlock.AGE, pContext.random().nextInt(2))
                                .setValue(CocoaBlock.FACING, direction.getOpposite());

                        pContext.setBlock(pos, blockstate);
                    }
                }
            }
        }
    }
}
