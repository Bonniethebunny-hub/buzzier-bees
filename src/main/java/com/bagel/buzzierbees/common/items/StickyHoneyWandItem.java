package com.bagel.buzzierbees.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StickyHoneyWandItem extends Item {
	public StickyHoneyWandItem(Properties properties) {
		super(properties);
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if (blockstate.getBlock() instanceof BeehiveBlock && (context.getFace() == blockstate.get(BeehiveBlock.field_226872_b_)) && blockstate.get(BeehiveBlock.field_226873_c_) == 5) {
			PlayerEntity playerentity = context.getPlayer();
			BlockState blockstate2 = null;
			world.playSound((PlayerEntity) null, blockpos, SoundEvents.field_226135_eP_, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			blockstate2 = blockstate.with(BeehiveBlock.field_226873_c_, Integer.valueOf(0));
			if (blockstate2 != null) {
				if (!world.isRemote) {
					world.setBlockState(blockpos, blockstate2, 11);
					if (playerentity != null) {
						context.getPlayer().setHeldItem(context.getPlayer().getActiveHand(), new ItemStack(ModItems.HONEY_WAND));
						// replace item here
						// context.getItem().damageItem(1, playerentity, (p_220040_1_) -> {p_220040_1_.sendBreakAnimation(context.getHand());});
					}
				}
			}
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		CompoundNBT nbt = stack.getOrCreateTag();
			if (entityLiving instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
				CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
				serverplayerentity.addStat(Stats.ITEM_USED.get(this));
				}
			if (!worldIn.isRemote) {
				entityLiving.removePotionEffect(Effects.POISON);
			}
		return new ItemStack(ModItems.HONEY_WAND);
	}
	
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
			target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2, true, true));
			//replace item?
			//stack.damageItem(1, attacker, (p_220048_0_) -> { p_220048_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);});
			return true;
	}	
}
