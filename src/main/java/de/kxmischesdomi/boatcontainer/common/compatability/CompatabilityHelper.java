package de.kxmischesdomi.boatcontainer.common.compatability;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CompatabilityHelper {

    public static JsonObject createFurnaceBoatRecipe(ResourceLocation output, ResourceLocation boat) {
        return createBoatRecipe("furnace_boat", output, boat, new ResourceLocation("furnace"));
    }

    public static JsonObject createEnderChestBoatRecipe(ResourceLocation output, ResourceLocation boat) {
        return createBoatRecipe("ender_chest_boat", output, boat, new ResourceLocation("ender_chest"));
    }

    public static JsonObject createBoatRecipe(String group, ResourceLocation output, ResourceLocation boat, ResourceLocation specialItem) {
        return createShapelessRecipeJson(group, output, boat, specialItem);
    }

    public static JsonObject createShapelessRecipeJson(String group, ResourceLocation output, ResourceLocation... items) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray ingredients = new JsonArray();
        for (ResourceLocation item : items) {
            JsonObject itemObject = new JsonObject();
            itemObject.addProperty("item", item.toString());
            ingredients.add(itemObject);
        }

        json.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        result.addProperty("count", 1);
        json.add("result", result);

        return json;
    }

}
