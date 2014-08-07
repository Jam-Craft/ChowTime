/*
 * ChowTime - Dynamically updating food mod for Minecraft
 *     Copyright (C) 2014  Team JamCraft
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jamcraft.chowtime.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by James Hollowell on 6/20/2014.
 */
public class ItemFarmerBoots extends ItemArmor
{
    public ItemFarmerBoots()
    {
        super(ChowTime.FARMER_BOOTS, 0, 3);
    }

    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return ChowTime.creativeTab;
    }

    public String getUnlocalizedName()
    {
        return "item.farmerBoots";
    }

    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return "item.farmerBoots";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(ModConstants.MODID + ":farmer_boots");
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "chowtime:textures/armor/farmer_boots.png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        //Check 1 & 2 blocks beneath player (2 for those random times it fails... go figure...)
        Block block1 = world.getBlock((int) player.posX - 1, (int) player.posY - 1, (int) player.posZ);
        Block block2 = world.getBlock((int) player.posX - 1, (int) player.posY - 2, (int) player.posZ);
        if (block1 instanceof BlockFarmland || block2 instanceof BlockFarmland)
        {
            //Yes, this is exploitable... IDK what else to do...
            player.fallDistance = 0.0F;
        }
    }
}

