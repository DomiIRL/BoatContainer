package de.kxmischesdomi.boatcontainer.mixin;

import com.google.gson.JsonElement;
import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.compatability.IModCompatibility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        for (IModCompatibility compatibility : BoatContainer.getLoadedCompatibilities()) {
            compatibility.loadCompatibilityRecipes(map);
        }
    }

}