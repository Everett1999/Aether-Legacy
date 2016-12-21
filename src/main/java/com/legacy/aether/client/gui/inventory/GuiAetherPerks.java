package com.legacy.aether.client.gui.inventory;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiInventory;

import org.lwjgl.input.Keyboard;

import com.legacy.aether.server.entities.passive.mountable.EntityMoa;
import com.legacy.aether.server.entities.util.MoaColor;
import com.legacy.aether.server.networking.AetherNetworkingManager;
import com.legacy.aether.server.networking.packets.PacketPerkChanged;
import com.legacy.aether.server.player.PlayerAether;
import com.legacy.aether.server.player.perks.AetherRankings;
import com.legacy.aether.server.player.perks.util.DonatorMoaSkin;
import com.legacy.aether.server.player.perks.util.EnumAetherPerkType;

public class GuiAetherPerks extends GuiScreen 
{

	private boolean enableMoaEditor, enableHaloEditor;

	private PlayerAether player;

	private EntityMoa moa;

	private GuiButton defualtSkin, enableHalo, confirmPreference;

	private GuiTextField moaWingMarking, moaWing, moaBody, moaBodyMarking, moaEye, moaOutside;

	private DonatorMoaSkin moaSkin = new DonatorMoaSkin();

    protected int guiLeft;

    protected int guiTop;

	public GuiAetherPerks()
	{
		super();

		this.mc = Minecraft.getMinecraft();
    	this.player = PlayerAether.get(this.mc.thePlayer);
    	this.moaSkin = this.player.donatorMoaSkin;

		this.moa = this.player.thePlayer.getRidingEntity() instanceof EntityMoa ? (EntityMoa) this.player.thePlayer.getRidingEntity() : new EntityMoa(this.mc.theWorld, MoaColor.getColor(0));
	}

	@Override
    public void updateScreen()
    {
    	this.moaWingMarking.updateCursorCounter();
    	this.moaWing.updateCursorCounter();
    	this.moaBody.updateCursorCounter();
    	this.moaBodyMarking.updateCursorCounter();
    	this.moaEye.updateCursorCounter();
    	this.moaOutside.updateCursorCounter();

    	if (!this.moaWingMarking.getText().isEmpty())
    	{
    		this.moaSkin.setWingMarkingColor(Integer.decode("0x" + this.moaWingMarking.getText()));
    	}

    	if (!this.moaWing.getText().isEmpty())
    	{
    		this.moaSkin.setWingColor(Integer.decode("0x" + this.moaWing.getText()));
    	}

    	if (!this.moaBody.getText().isEmpty())
    	{
    		this.moaSkin.setBodyColor(Integer.decode("0x" + this.moaBody.getText()));
    	}

    	if (!this.moaBodyMarking.getText().isEmpty())
    	{
    		this.moaSkin.setMarkingColor(Integer.decode("0x" + this.moaBodyMarking.getText()));
    	}

    	if (!this.moaEye.getText().isEmpty())
    	{
    		this.moaSkin.setEyeColor(Integer.decode("0x" + this.moaEye.getText()));
    	}

    	if (!this.moaOutside.getText().isEmpty())
    	{
    		this.moaSkin.setOutsideColor(Integer.decode("0x" + this.moaOutside.getText()));
    	}

    	this.moaSkin.shouldUseDefualt(this.defualtSkin.displayString.contains("true"));

    	if (!this.enableHaloEditor || !AetherRankings.isRankedPlayer(this.player.thePlayer.getUniqueID()))
    	{
    		this.enableHalo.visible = false;
    	}

    	if (!this.enableMoaEditor)
    	{
    		this.defualtSkin.visible = false;
    		this.confirmPreference.visible = false;
    	}
    }

	@Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        this.guiLeft = (this.width - 176) / 2;
        this.guiTop = (this.height - 166) / 2;

    	this.buttonList.add(new GuiButton(1, 4, 17, 100, 20, "Moa Customizer"));
    	this.buttonList.add(new GuiButton(5, 110, 17, 100, 20, "Developer Perks"));

    	this.buttonList.add(this.enableHalo = new GuiButton(6, this.width / 2 - 50, this.height - 20, 100, 20, "Enable Halo: "));
    	this.buttonList.add(this.defualtSkin = new GuiButton(2,  this.width / 2 - 100, this.height - 20, 100, 20, "Default skin: " + this.player.donatorMoaSkin.shouldUseDefualt()));
    	this.buttonList.add(this.confirmPreference = new GuiButton(4,  this.width / 2, this.height - 20, 100, 20, "Confirm Skin"));

    	this.enableHalo.displayString = this.enableHalo.displayString + Boolean.toString(this.player.shouldRenderHalo ? false : true);

        this.moaEye = new GuiTextField(1, this.fontRendererObj, (this.width / 2) + 105, 70, 45, 20);
        this.moaEye.setMaxStringLength(6);
        this.moaEye.setText(Integer.toHexString(this.player.donatorMoaSkin.getEyeColor()));

        this.moaWingMarking = new GuiTextField(5, this.fontRendererObj, (this.width / 2) + 105, this.height - 40, 45, 20);
        this.moaWingMarking.setMaxStringLength(6);
        this.moaWingMarking.setText(Integer.toHexString(this.player.donatorMoaSkin.getWingMarkingColor()));

        this.moaWing = new GuiTextField(10, this.fontRendererObj, (this.width / 2) + 105, this.height / 2 + 15, 45, 20);
        this.moaWing.setMaxStringLength(6);
        this.moaWing.setText(Integer.toHexString(this.player.donatorMoaSkin.getWingColor()));

        this.moaBody = new GuiTextField(14, this.fontRendererObj, (this.width / 2) - 155, this.height / 2 + 15, 45, 20);
        this.moaBody.setMaxStringLength(6);
        this.moaBody.setText(Integer.toHexString(this.player.donatorMoaSkin.getBodyColor()));

        this.moaBodyMarking = new GuiTextField(18, this.fontRendererObj, (this.width / 2) - 155, this.height - 40, 45, 20);
        this.moaBodyMarking.setMaxStringLength(6);
        this.moaBodyMarking.setText(Integer.toHexString(this.player.donatorMoaSkin.getMarkingColor()));

        this.moaOutside = new GuiTextField(22, this.fontRendererObj, (this.width / 2) - 155, 70, 45, 20);
        this.moaOutside.setMaxStringLength(6);
        this.moaOutside.setText(Integer.toHexString(this.player.donatorMoaSkin.getOutsideColor()));
    }

	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	super.keyTyped(typedChar, keyCode);

    	if (this.enableMoaEditor)
    	{
    		if (keyCode > 1 && keyCode < 12 || keyCode == 14 || keyCode == 30 || keyCode == 48 || keyCode == 46 || keyCode == 32 || keyCode == 18 || keyCode == 33)
    		{
            	this.moaWingMarking.textboxKeyTyped(typedChar, keyCode);
            	this.moaWing.textboxKeyTyped(typedChar, keyCode);
            	this.moaBody.textboxKeyTyped(typedChar, keyCode);
            	this.moaBodyMarking.textboxKeyTyped(typedChar, keyCode);
            	this.moaEye.textboxKeyTyped(typedChar, keyCode);
            	this.moaOutside.textboxKeyTyped(typedChar, keyCode);
    		}
    	}
    }

	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.enableMoaEditor)
        {
            this.moaWingMarking.mouseClicked(mouseX, mouseY, mouseButton);
            this.moaWing.mouseClicked(mouseX, mouseY, mouseButton);
            this.moaBody.mouseClicked(mouseX, mouseY, mouseButton);
            this.moaBodyMarking.mouseClicked(mouseX, mouseY, mouseButton);
            this.moaEye.mouseClicked(mouseX, mouseY, mouseButton);
            this.moaOutside.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

	@Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
		AetherNetworkingManager.sendToServer(new PacketPerkChanged(this.player.thePlayer.getEntityId(), EnumAetherPerkType.Moa, this.moaSkin));
    }

	@Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
		if (button.id == 1)
		{
			boolean shouldEnable = this.enableMoaEditor ? false : true;

			this.enableMoaEditor = shouldEnable;
			this.enableHaloEditor = false;
			this.enableHalo.visible = false;
			this.confirmPreference.visible = true;
			this.defualtSkin.visible = true;
		}
		else if (button.id == 2)
		{
			boolean enableDefualt = this.player.donatorMoaSkin.shouldUseDefualt() ? false : true;

			this.defualtSkin.displayString = "Use Default: " + Boolean.toString(enableDefualt);
			this.player.donatorMoaSkin.shouldUseDefualt(enableDefualt);
		}
		else if (button.id == 4)
		{
			AetherNetworkingManager.sendToServer(new PacketPerkChanged(this.player.thePlayer.getEntityId(), EnumAetherPerkType.Moa, this.moaSkin));
		}
		else if (button.id == 5)
		{
			boolean shouldEnable = this.enableHaloEditor ? false : true;

			this.enableHaloEditor = shouldEnable;
			this.enableMoaEditor = false;
			this.enableHalo.visible = true;
		}
		else if (button.id == 6)
		{
			boolean enableHalo = this.player.shouldRenderHalo ? false : true;

			AetherNetworkingManager.sendToServer(new PacketPerkChanged(this.player.thePlayer.getEntityId(), EnumAetherPerkType.Halo, enableHalo));
			this.enableHalo.displayString = "Enable Halo: " + Boolean.toString(enableHalo);
		}
    }

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRendererObj, "Donator Options", 45, 4, 16777215);

        if (this.enableMoaEditor)
        {
        	GuiInventory.drawEntityOnScreen(this.width / 2, this.height / 2 + (this.height / 2) - 30, (this.height / 3) - 20, (float)(this.guiLeft + 51) - (float)mouseX, (float)(75 - 50) - (float)mouseY, this.moa);

        	/* Left Side*/

            this.drawString(this.fontRendererObj, "Leg/Beak Color", (this.width / 2) - 170, 55, 16777215);

            this.drawString(this.fontRendererObj, "Body Color", (this.width / 2) - 160, this.height / 2, 16777215);

            this.drawString(this.fontRendererObj, "Body Marking Color", (this.width / 2) - 180, this.height - 55, 16777215);

        	/* Right Side */

            this.drawString(this.fontRendererObj, "Eye Color", (this.width / 2) + 104, 55, 16777215);

            this.drawString(this.fontRendererObj, "Wing Color", (this.width / 2) + 102, this.height / 2, 16777215);

            this.drawString(this.fontRendererObj, "Wing Marking Color", (this.width / 2) + 82, this.height - 55, 16777215);

            this.moaWingMarking.drawTextBox();
            this.moaWing.drawTextBox();
            this.moaBody.drawTextBox();
            this.moaBodyMarking.drawTextBox();
            this.moaEye.drawTextBox();
            this.moaOutside.drawTextBox();
        }

        if (this.enableHaloEditor)
        {
        	GuiInventory.drawEntityOnScreen(this.width / 2, this.height / 2 + (this.height / 2) - 30, (this.height / 3) - 20, (float)(this.guiLeft + 51) - (float)mouseX, (float)(75 - 50) - (float)mouseY, this.player.thePlayer);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

	@Override
    public void drawWorldBackground(int tint)
    {
        this.drawGradientRect(0, 0, this.width, 50, -1072689136, -804253680);

        if (this.mc.theWorld != null)
        {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else
        {
            this.drawBackground(tint);
        }
    }

}