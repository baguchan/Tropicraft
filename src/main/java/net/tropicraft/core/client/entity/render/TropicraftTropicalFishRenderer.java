package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropicraftTropicalFishModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftTropicalFishEntity;

public class TropicraftTropicalFishRenderer extends TropicraftFishRenderer<TropicraftTropicalFishEntity, TropicraftTropicalFishModel> {
    public TropicraftTropicalFishRenderer(final EntityRendererProvider.Context context) {
        super(context, new TropicraftTropicalFishModel(context.bakeLayer(TropicraftRenderLayers.TROPICAL_FISH_LAYER)), 0.2f);
    }
}
