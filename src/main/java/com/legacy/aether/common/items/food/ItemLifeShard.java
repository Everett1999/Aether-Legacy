package com.legacy.aether.common.items.food;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.legacy.aether.common.Aether;
import com.legacy.aether.common.player.PlayerAether;
import com.legacy.aether.common.registry.creative_tabs.AetherCreativeTabs;

public class ItemLifeShard extends Item
{

	public ItemLifeShard()
	{
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.misc);
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World worldIn, EntityPlayer player, EnumHand hand)
	{
		PlayerAether playerAether = PlayerAether.get(player);

		if (!worldIn.isRemote)
		{
			if (playerAether.getExtraHealth() < 20.0F)
			{
				playerAether.increaseMaxHP();

				itemstack.stackSize--;
				
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
			}
			else if (playerAether.getExtraHealth() >= 20.0F)
			{
				Aether.proxy.sendMessage(player, "You can only use a total of 10 life shards.");
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

}