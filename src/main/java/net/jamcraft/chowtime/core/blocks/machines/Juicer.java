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

package net.jamcraft.chowtime.core.blocks.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.GuiIDS;
import net.jamcraft.chowtime.core.ModConstants;
import net.jamcraft.chowtime.core.tileentities.TEJuicer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Kayla Marie on 5/14/14.
 */
public class Juicer extends BlockContainer implements IMachineBlock
{
    @SideOnly(Side.CLIENT)
    protected IIcon top;
    @SideOnly(Side.CLIENT)
    protected IIcon side;

    public Juicer()
    {
        super(Material.iron);
        this.setCreativeTab(ChowTime.creativeTab);
        setBlockName("juicer");
        setBlockTextureName(ModConstants.MODID + ":juicer");
        setHarvestLevel("pickaxe",1);
        setHardness(5);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEJuicer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
        if (entityPlayer.isSneaking()) return false;
        else
        {
            if (!world.isRemote)
            {
                TEJuicer logic = (TEJuicer) world.getTileEntity(x, y, z);

                if (logic != null)
                {
                    entityPlayer.openGui(ChowTime.instance, GuiIDS.Juicer_Gui, world, x, y, z);
                }
            }

            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity te=world.getTileEntity(x,y,z);
        if(te instanceof TEJuicer)
        {
            ((TEJuicer) te).dropContents();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.side = par1IconRegister.registerIcon(ModConstants.MODID + ":" + "juicer_side");
        this.top = par1IconRegister.registerIcon(ModConstants.MODID + ":" + "juicer_top");
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int p_150163_1_)
    {
        return this.side;
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int p_150161_1_)
    {
        return this.top;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        int k = p_149691_2_ & 12;
        int l = p_149691_2_ & 3;
        return k == 0 && (p_149691_1_ == 1 || p_149691_1_ == 0) ? this.getTopIcon(l) : (k == 4 && (p_149691_1_ == 5 || p_149691_1_ == 4) ? this.getTopIcon(l) : (k == 8 && (p_149691_1_ == 2 || p_149691_1_ == 3) ? this.getTopIcon(l) : this.getSideIcon(l)));
    }
    public int getRenderType()
    {
        return 31;
    }
}
