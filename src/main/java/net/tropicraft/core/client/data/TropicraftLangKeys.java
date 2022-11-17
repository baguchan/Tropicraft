package net.tropicraft.core.client.data;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.common.Util;

public enum TropicraftLangKeys {

	NA("general", "na", "N/A"),

	SCUBA_AIR_TIME("scuba", "air_time", "Air Remaining: %s"),
	SCUBA_DIVE_TIME("scuba", "dive_time", "Dive Time: %s"),
    SCUBA_DEPTH("scuba", "depth", "Current Depth: %s"),
    SCUBA_MAX_DEPTH("scuba", "max_depth", "Max Depth: %s"),
    SCUBA_VISIBILITY_STAT("scuba", "scuba.visibility", "Underwater Fog Reduction")
    ;

    protected final String key, value;
	private final Component component;

    private TropicraftLangKeys(String type, String key) {
        this(type, key, Util.toEnglishName(key));
    }

    private TropicraftLangKeys(String type, String key, String value) {
        this.key = net.minecraft.Util.makeDescriptionId(type, new ResourceLocation(Constants.MODID, key));
		this.value = value;
		this.component = Component.translatable(this.key);
	}

	public String getKey() {
		return key;
	}

	public Component getComponent() {
		return component;
	}

	public Component format(Object... args) {
		return Component.translatable(getComponent().getString(), args);
	}

	public String getLocalizedText() {
		return getComponent().getString();
	}

	protected void register(TropicraftLangProvider prov) {
		prov.add(key, value);
	}

    public static void generate(TropicraftLangProvider prov) {
        for (TropicraftLangKeys lang : values()) {
            lang.register(prov);
        }
    }
}
