package net.jamcraft.chowtime.core.blocks;

import java.util.Random;

import net.jamcraft.chowtime.core.CTInits;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CTFarmland extends BlockFarmland
{
    @SideOnly(Side.CLIENT)
    private IIcon wet;
    @SideOnly(Side.CLIENT)
    private IIcon dry;

    public CTFarmland()
    {
        super();
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        setBlockName("farmland");
        this.setLightOpacity(255);
        this.opaque = false;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double) (x), (double) (y), (double) (z), (double) (x + 1), (double) (y + 1), (double) (z + 1));
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? (meta > 0 ? this.wet : this.dry) : Blocks.dirt.getBlockTextureFromSide(side);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!this.isWaterNearby(world, x, y, z) && !world.canLightningStrikeAt(x, y + 1, z))
        {
            int l = world.getBlockMetadata(x, y, z);

            if (l > 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, l - 1, 2);
            }
            else if (!this.canSustainPlant(world, x, y, z))
            {
                world.setBlock(x, y, z, Blocks.dirt);
            }
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, 7, 2);
        }
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float distance)
    {
        if (!world.isRemote && world.rand.nextFloat() < distance - 0.5F)
        {
            if (!(entity instanceof EntityPlayer) && !world.getGameRules().getGameRuleBooleanValue("mobGriefing") || (entity instanceof EntityPlayer && ((EntityPlayer) entity).inventory.armorInventory[0] != null && ((EntityPlayer) entity).inventory.armorInventory[0].getItem() == CTInits.FarmerBoots))
            {
                return;
            }

            world.setBlock(x, y, z, Blocks.dirt);
        }
    }

    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);

        if (plantType == EnumPlantType.Crop) return true;
        if (plantable instanceof BlockCrops) return true;

        return true;
    }

    public boolean isFertile(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) > 0;
    }

    private boolean canSustainPlant(World world, int x, int y, int z)
    {
        byte b0 = 0;

        for (int l = x - b0; l <= x + b0; ++l)
        {
            for (int i1 = z - b0; i1 <= z + b0; ++i1)
            {
                Block block = world.getBlock(l, y + 1, i1);

                if (block instanceof IPlantable && canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) block))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWaterNearby(World world, int x, int y, int z)
    {
        for (int l = x - 4; l <= x + 4; ++l)
        {
            for (int i1 = y; i1 <= y + 1; ++i1)
            {
                for (int j1 = z - 4; j1 <= z + 4; ++j1)
                {
                    if (world.getBlock(l, i1, j1).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        Material material = world.getBlock(x, y + 1, z).getMaterial();

        if (material.isSolid())
        {
            world.setBlock(x, y, z, Blocks.dirt);
        }
    }

    public Item getItemDropped(int meta, Random rand, int fortune)
    {
        return Blocks.dirt.getItemDropped(0, rand, fortune);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.wet = icon.registerIcon("farmland_wet");
        this.dry = icon.registerIcon("farmland_dry");
    }
}