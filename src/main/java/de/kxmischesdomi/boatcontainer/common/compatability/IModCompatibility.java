package de.kxmischesdomi.boatcontainer.common.compatability;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface IModCompatibility {
    String getModId();
    void loadCompatibility();
    void loadCompatibilityRecipes(Map<ResourceLocation, JsonElement> recipes);
}
