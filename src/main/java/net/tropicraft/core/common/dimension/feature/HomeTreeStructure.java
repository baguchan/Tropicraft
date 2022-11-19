package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.PieceWithGenerationBounds;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePieceTypes;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;

import java.util.Optional;

public class HomeTreeStructure extends Structure {
    public static final Codec<HomeTreeStructure> CODEC = simpleCodec(HomeTreeStructure::new);

    public HomeTreeStructure(Structure.StructureSettings p_227385_) {
        super(p_227385_);
    }

    private static void generatePieces(StructurePiecesBuilder p_227392_, BlockPos p_227531_, Rotation p_227532_, Structure.GenerationContext p_227393_) {
        StructurePoolElement structurepoolelement = TropicraftTemplatePools.HOME_TREE_STARTS.get().getRandomTemplate(p_227393_.random());
        p_227392_.addPiece(new Piece(p_227393_.structureTemplateManager(), structurepoolelement, p_227531_, 0, p_227532_, structurepoolelement.getBoundingBox(p_227393_.structureTemplateManager(), p_227531_, p_227532_)));
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        Rotation rotation = Rotation.getRandom(pContext.random());
        BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(pContext, rotation);

        if (!checkLocation(pContext)) {
            return Optional.empty();
        }

        return blockpos.getY() < 60 ? Optional.empty() : Optional.of(new Structure.GenerationStub(blockpos, (p_227538_) -> {
            this.generatePieces(p_227538_, blockpos, rotation, pContext);
        }));
    }


    private static boolean isValid(ChunkGenerator generator, BlockPos pos, int startY, final LevelHeightAccessor level, RandomState randomState) {
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, randomState);
        return y >= generator.getSeaLevel()
                && Math.abs(y - startY) < 10
                && y < 150
                && y > generator.getSeaLevel() + 2;
    }

    public static boolean checkLocation(Structure.GenerationContext pContext) {
        final ChunkGenerator generator = pContext.chunkGenerator();
        final BlockPos pos = pContext.chunkPos().getWorldPosition();
        final LevelHeightAccessor level = pContext.heightAccessor();
        final RandomState randomState = pContext.randomState();
        int y = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level, randomState);
        return isValid(generator, pos.offset(-4, 0, -4), y, level, randomState) &&
                isValid(generator, pos.offset(-4, 0, 4), y, level, randomState) &&
                isValid(generator, pos.offset(4, 0, 4), y, level, randomState) &&
                isValid(generator, pos.offset(4, 0, -4), y, level, randomState);
    }

    @Override
    public StructureType<?> type() {
        return TropicraftStructureType.HOME_TREE.get();
    }

    public static class Piece extends PoolElementStructurePiece {
        public Piece(StructureTemplateManager templates, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds) {
            super(templates, piece, pos, groundLevelDelta, rotation, bounds);
            this.boundingBox = this.fixGenerationBoundingBox(templates);
        }

        public Piece(StructurePieceSerializationContext pContext, CompoundTag data) {
            super(pContext, data);
            this.boundingBox = this.fixGenerationBoundingBox(pContext.structureTemplateManager());
        }

        private BoundingBox fixGenerationBoundingBox(StructureTemplateManager templates) {
            if (this.element instanceof PieceWithGenerationBounds piece) {
                return piece.getGenerationBounds(templates, this.position, Rotation.NONE);
            } else {
                return boundingBox;
            }
        }

        @Override
        public Rotation getRotation() {
            if (this.element instanceof NoRotateSingleJigsawPiece) {
                return Rotation.NONE;
            }
            return super.getRotation();
        }

        @Override
        public StructurePieceType getType() {
            return TropicraftStructurePieceTypes.HOME_TREE.get();
        }
    }
}
