package com.legacy.aether.common.items.weapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.legacy.aether.common.entities.projectile.EntityHammerProjectile;
import com.legacy.aether.common.player.PlayerAether;
import com.legacy.aether.common.registry.creative_tabs.AetherCreativeTabs;

public class ItemNotchHammer extends ItemSword
{

	public EntityHammerProjectile hammerProjectile;

	public ItemNotchHammer()
	{
		super(ToolMaterial.IRON);
        this.setCreativeTab(AetherCreativeTabs.weapons);
	}

	@Override
	public float getDamageVsEntity()
	{
		return 1.5F;
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer, EnumHand hand)
	{
		if (entityplayer.capabilities.isCreativeMode)
		{
			world.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F), false);

			this.hammerProjectile = new EntityHammerProjectile(world, entityplayer);

			if (!world.isRemote)
			{
				world.spawnEntityInWorld(this.hammerProjectile);
			}
		}
		
		else if (PlayerAether.get(entityplayer).setGeneralCooldown(200, itemstack.getDisplayName()))
		{
			itemstack.damageItem(1, entityplayer);

			world.playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F), false);

			this.hammerProjectile = new EntityHammerProjectile(world, entityplayer);

			if (!world.isRemote)
			{
				world.spawnEntityInWorld(this.hammerProjectile);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

}