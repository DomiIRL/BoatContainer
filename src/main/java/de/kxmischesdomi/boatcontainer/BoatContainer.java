package de.kxmischesdomi.boatcontainer;

import de.kxmischesdomi.boatcontainer.common.compatability.IModCompatibility;
import de.kxmischesdomi.boatcontainer.common.compatability.TwigsCompatibility;
import de.kxmischesdomi.boatcontainer.common.registry.ModEntities;
import de.kxmischesdomi.boatcontainer.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.LinkedList;
import java.util.List;

public class BoatContainer implements ModInitializer {

	public static final String MOD_ID = "boatcontainer";

	private static final List<IModCompatibility> loadedCompatibilities = new LinkedList<>();

	@Override
	public void onInitialize() {
		ModItems.init();
		ModEntities.init();
		initCompatibilities();
	}

	private void initCompatibilities() {
		initCompatibility(new TwigsCompatibility());
	}

	private void initCompatibility(IModCompatibility compatibility) {
		if (FabricLoader.getInstance().isModLoaded(compatibility.getModId())) {
			compatibility.loadCompatibility();
			loadedCompatibilities.add(compatibility);
		}
	}

	public static List<IModCompatibility> getLoadedCompatibilities() {
		return loadedCompatibilities;
	}
}
