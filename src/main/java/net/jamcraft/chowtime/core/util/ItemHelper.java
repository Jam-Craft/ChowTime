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

package net.jamcraft.chowtime.core.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by James Hollowell on 5/18/2014.
 */
public class ItemHelper
{
    /**Bucket intellegent decrease of stack size
     *
     * @param itemStack Stack to decrease.
     * @param world World object in case of spawning items on ground
     * @param x Spawn center x
     * @param y Spawn center y
     * @param z Spawn center z
     * @return The resultant itemstack
     */
    public static ItemStack decreaseStack(ItemStack itemStack, World world, int x, int y, int z)
    {
        ItemStack stack=itemStack.copy();
        stack.stackSize--;
        if(stack.getItem() instanceof ItemBucket || stack.getItem() instanceof ItemBucketMilk)
        {
            if(stack.stackSize<=0)
            {
                stack=new ItemStack(Items.bucket);
            }
            else
            {
                ItemHelper.spawnItemStackInWorld(new ItemStack(Items.bucket),world,x,y,z);
            }

        }
        else
        {
            if (stack.stackSize <= 0)
            {
                stack = null;
            }
        }
        return stack;
    }

    public static void spawnItemStackInWorld(ItemStack stack, World world, int x, int y, int z)
    {
        if(!world.isRemote)
        {
            Random rand = new Random();
            final float f = rand.nextFloat() * 0.8F + 0.1F;
            final float f1 = rand.nextFloat() * 0.8F + 0.1F;
            final float f2 = rand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), stack);
            entityitem.motionX = (double) ((float) rand.nextGaussian() * .05F);
            entityitem.motionY = (double) ((float) rand.nextGaussian() * 0.25F);
            entityitem.motionZ = (double) ((float) rand.nextGaussian() * .05F);
            world.spawnEntityInWorld(entityitem);
        }
    }
}
