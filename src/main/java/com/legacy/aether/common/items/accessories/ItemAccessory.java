package com.legacy.aether.common.items.accessories;

import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicates;
import com.legacy.aether.common.Aether;
import com.legacy.aether.common.containers.util.AccessoryType;
import com.legacy.aether.common.player.PlayerAether;
import com.legacy.aether.common.registry.creative_tabs.AetherCreativeTabs;

public class ItemAccessory extends Item
{

	public static final String ROOT = Aether.modAddress() + "textures/slots/slot_";

	protected final AccessoryType accessoryType;

	public ResourceLocation texture;

	private int colorHex = 0xdddddd;

    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem()
    {
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            ItemStack itemstack = ItemAccessory.dispenseAccessory(source, stack);
            return itemstack != null ? itemstack : super.dispenseStack(source, stack);
        }
    };

	public ItemAccessory(AccessoryType type)
	{
		this.accessoryType = type;
		this.texture = new ResourceLocation("aether_legacy", "textures/armor/accessory_base.png");
		this.setMaxStackSize(1);
		this.setCreativeTab(AetherCreativeTabs.accessories);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
	}

    public static ItemStack dispenseAccessory(IBlockSource blockSource, ItemStack stack)
    {
        BlockPos blockpos = blockSource.getBlockPos().offset((EnumFacing)blockSource.getBlockState().getValue(BlockDispenser.FACING));
        List<EntityLivingBase> list = blockSource.getWorld().<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockpos), Predicates.<EntityLivingBase>and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(stack)));

        if (list.isEmpty())
        {
            return null;
        }
        else
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(0);
            
            if (entitylivingbase instanceof EntityPlayer)
            {
            	ItemStack itemstack = stack.copy();
            	itemstack.stackSize = 1;
            	
            	PlayerAether playerAether = PlayerAether.get((EntityPlayer) entitylivingbase);

            	if (!playerAether.accessories.setInventoryAccessory(itemstack))
            	{
            		BehaviorDefaultDispenseItem.doDispense(blockSource.getWorld(), itemstack, 6, (EnumFacing)blockSource.getBlockState().getValue(BlockDispenser.FACING), BlockDispenser.getDispensePosition(blockSource));
            	}

            	--stack.stackSize;
            	return stack;
            }
        }

		return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player, EnumHand hand)
    {
        if (itemStack != null)
        {
        	PlayerAether.get(player).accessories.setInventoryAccessory(itemStack.copy());
        	--itemStack.stackSize;

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
    }

	public AccessoryType getType()
	{
		return this.accessoryType;
	}

    public Item setColor(int color)
    {
    	this.colorHex = color;
    	return this;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int meta)
    {
    	return this.colorHex;
    }

    public ItemAccessory setTexture(String location)
    {
    	texture = new ResourceLocation("aether_legacy", "textures/armor/accessory_" + location + ".png");
    	return this;
    }

}