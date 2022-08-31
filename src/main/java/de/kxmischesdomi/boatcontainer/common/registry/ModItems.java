package de.kxmischesdomi.boatcontainer.common.registry;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Function5;
import de.kxmischesdomi.boatcontainer.BoatContainer;
import de.kxmischesdomi.boatcontainer.common.compatability.CompatabilityHelper;
import de.kxmischesdomi.boatcontainer.common.compatability.IModCompatibility;
import de.kxmischesdomi.boatcontainer.common.entity.ChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.EnderChestBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.FurnaceBoatEntity;
import de.kxmischesdomi.boatcontainer.common.entity.OverriddenBoatEntity;
import de.kxmischesdomi.boatcontainer.common.item.CustomBoatItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat.Type;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static final Map<ResourceLocation, JsonElement> recipesToLoad = new HashMap<>();

	public static CustomBoatItem[] CHEST_BOAT = registerBoat("chest_boat", ModEntities.CHEST_BOAT, ChestBoatEntity::new, true);
	public static CustomBoatItem[] ENDER_CHEST_BOAT = registerBoat("ender_chest_boat", ModEntities.ENDER_CHEST_BOAT, EnderChestBoatEntity::new);
	public static CustomBoatItem[] FURNACE_BOAT = registerBoat("furnace_boat", ModEntities.FURNACE_BOAT, FurnaceBoatEntity::new);

	public static void init() {
		for (CustomBoatItem item : ENDER_CHEST_BOAT) {
			addRecipe(item, "ender_chest_boat", new ResourceLocation("ender_chest"));

		}
		for (CustomBoatItem item : FURNACE_BOAT) {
			addRecipe(item, "furnace_boat", new ResourceLocation("furnace"));
		}
	}

	private static void addRecipe(CustomBoatItem item, String name, ResourceLocation ingredient) {
		Type boatType = item.getType();
		ResourceLocation originLocation = new ResourceLocation(boatType.getName());
		ResourceLocation originBoatLocation = new ResourceLocation(boatType.getName() + "_boat");
		ResourceLocation boatLocation = new ResourceLocation(BoatContainer.MOD_ID, originLocation.getPath() + "_" + name);
		recipesToLoad.put(boatLocation, CompatabilityHelper.createBoatRecipe(name, boatLocation, originBoatLocation, ingredient));
	}

	public static CustomBoatItem[] registerBoat(String name, EntityType<? extends OverriddenBoatEntity> type, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator) {
		return registerBoat(name, type, instanceCreator, false);
	}

	public static CustomBoatItem[] registerBoat(String name, EntityType<? extends OverriddenBoatEntity> type, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator, boolean hideItem) {

		List<CustomBoatItem> list = new ArrayList<>();

		for (Type value : Type.values()) {
			try {
				Properties settings = new Properties().stacksTo(1);
				if (!hideItem) {
					settings.tab(CreativeModeTab.TAB_TRANSPORTATION);
				}

				ResourceLocation originBoatLocation = new ResourceLocation(value.getName() + "_" + name);

				CustomBoatItem item = register(originBoatLocation.getPath(), new CustomBoatItem(type, instanceCreator, value, settings));
				list.add(item);
				registerBoatDispenserBehavior(item, type, instanceCreator);
			} catch (Exception exception) {
				// Dont block mod loading for issues with other mods
				exception.printStackTrace();
			}
		}

		return list.toArray(new CustomBoatItem[0]);
	}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new ResourceLocation(BoatContainer.MOD_ID, name), item);
		return item;
	}

	public static void registerBoatDispenserBehavior(ItemLike item, EntityType<? extends OverriddenBoatEntity> entityType, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator) {
		DispenserBlock.registerBehavior(item, new CustomBoatDispenserBehavior(entityType, instanceCreator));
	}

	public static class CustomBoatDispenserBehavior extends DefaultDispenseItemBehavior {

		private static final DispenseItemBehavior FALLBACK_BEHAVIOR = new DefaultDispenseItemBehavior();
		private static final float OFFSET_MULTIPLIER = 1.125F;

		private final EntityType<? extends OverriddenBoatEntity> entityType;
		private final Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator;

		public CustomBoatDispenserBehavior(EntityType<? extends OverriddenBoatEntity> entityType, Function5<EntityType<? extends OverriddenBoatEntity>, Level, Double, Double, Double, ? extends OverriddenBoatEntity> instanceCreator) {
			this.entityType = entityType;
			this.instanceCreator = instanceCreator;
		}

		@Override
		public ItemStack execute(BlockSource pointer, ItemStack stack) {
			Direction facing = pointer.getBlockState().getValue(DispenserBlock.FACING);

			double x = pointer.x() + facing.getStepX() * OFFSET_MULTIPLIER;
			double y = pointer.y() + facing.getStepY() * OFFSET_MULTIPLIER;
			double z = pointer.z() + facing.getStepZ() * OFFSET_MULTIPLIER;

			Level world = pointer.getLevel();
			BlockPos pos = pointer.getPos().relative(facing);

			if (world.getFluidState(pos).is(FluidTags.WATER)) {
				y += 1;
			} else if (!world.getBlockState(pos).isAir() || !world.getFluidState(pos.below()).is(FluidTags.WATER)) {
				return FALLBACK_BEHAVIOR.dispense(pointer, stack);
			}

			OverriddenBoatEntity boatEntity = instanceCreator.apply(entityType, world, x, y, z);

			boatEntity.setYRot(facing.toYRot());
			world.addFreshEntity(boatEntity);

			stack.shrink(1);
			return stack;
		}
	}

}
