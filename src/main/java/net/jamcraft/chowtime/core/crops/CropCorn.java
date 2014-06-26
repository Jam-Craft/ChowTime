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

package net.jamcraft.chowtime.core.crops;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.jamcraft.chowtime.core.CTInits;
import net.jamcraft.chowtime.core.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static net.minecraftforge.common.EnumPlantType.Crop;

/**
 * Created by DarkKnight on 5/18/14.
 */
public class CropCorn extends BlockCrops implements ICrop
{
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public CropCorn()
    {
        super();
        float f = 0.5F;
        this.setTickRandomly(true);
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.disableStats();
        //        this.setBlockTextureName(ModConstants.MODID + ":barley");
    }

    public int getRenderType()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta > 7)
        {
            meta = 7;
        }

        return this.icons[meta];
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int p_149650_3_)
    {
        return meta == 7 ? CTInits.Corn : CTInits.CornSeeds;
    }

    @Override
    protected Item func_149866_i()
    {
        return CTInits.CornSeeds;
    }

    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);

        if (world.getBlockLightValue(x, y + 1, z) >= 9)
        {
            int l = world.getBlockMetadata(x, y, z);

            if (l < 7)
            {
                float f = this.growPlant(world, x, y, z);

                if (rand.nextInt((int) (25.0F / f) + 1) == 0)
                {
                    ++l;
                    world.setBlockMetadataWithNotify(x, y, z, l, 2);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.icons = new IIcon[8];

        for (int i = 0; i < this.icons.length; ++i)
        {
            this.icons[i] = icon.registerIcon(ModConstants.MODID + ":" + "corn_stage_" + i);
        }
    }

    private float growPlant(World world, int x, int y, int z)
    {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l)
        {
            for (int i1 = z - 1; i1 <= z + 1; ++i1)
            {
                float f1 = 0.0F;

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, ForgeDirection.UP, this))
                {
                    f1 = 1.0F;

                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1))
                    {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1)
        {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return Crop;
    }
}
