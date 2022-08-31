package de.kxmischesdomi.boatcontainer.common.compatability;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TwigsCompatibility implements IModCompatibility {

    @Override
    public String getModId() {
        return "twigs";
    }

    @Override
    public void loadCompatibility() {

    }

    @Override
    public void loadCompatibilityRecipes(Map<ResourceLocation, JsonElement> recipes) {
        recipes.put(new ResourceLocation("boatcontainer", "stripped_bamboo_furnace_boat"), CompatabilityHelper.createEnderChestBoatRecipe(
                new ResourceLocation("boatcontainer", "stripped_bamboo_furnace_boat"),
                new ResourceLocation("twigs", "stripped_bamboo_boat")
                ));
        recipes.put(new ResourceLocation("boatcontainer", "stripped_bamboo_ender_chest_boat"), CompatabilityHelper.createEnderChestBoatRecipe(
                new ResourceLocation("boatcontainer", "stripped_bamboo_ender_chest_boat"),
                new ResourceLocation("twigs", "stripped_bamboo_boat")
        ));
    }


}
