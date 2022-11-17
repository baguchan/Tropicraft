package net.tropicraft.core.common.dimension.carver;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;

public class TropicsCaveCarver extends CaveWorldCarver {

    public TropicsCaveCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);
    }

//    @Override
//    protected int getCaveY(RandomSource rand) {
//        if (rand.nextInt(5) == 0) {
//            return rand.nextInt(240 + 8); // Add some evenly distributed caves in, in addition to the ones biased towards lower Y
//        }
//        return rand.nextInt(rand.nextInt(240) + 8);
//    }
}
