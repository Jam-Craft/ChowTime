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

package net.jamcraft.chowtime.core.mobs.SeedMob;

import net.jamcraft.chowtime.core.CTInits;
import net.jamcraft.chowtime.core.registrars.SeedRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * Created by Kayla Marie on 5/16/14.
 */
public class EntitySeedMob extends EntityAnimal
{

    private int inLove;
    private int cooldown = 0;
    private EntityPlayer field_146084_br;

    public EntitySeedMob(World par1World)
    {
        super(par1World);

        this.setHealth(20.0F); //This is in half-hearts
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setSpeed(0.222);
        this.setSize(0.5F, 0.8F);
        this.isImmuneToFire = false;
        float var2 = 0.27F;

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.33F));
        this.tasks.addTask(2, new EntityAIMate(this, var2));
        this.tasks.addTask(3, new EntityAITempt(this, 0.3F, CTInits.Strawberry, false));
        //this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0.28F));
        this.tasks.addTask(5, new EntityAIWander(this, 0.25F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 5.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }


    public boolean isAIEnabled()
    {
        return true;
    }

    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItem() == CTInits.Strawberry;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.getGrowingAge() != 0)
        {
            this.inLove = 0;
        }

        if (this.inLove > 0)
        {
            --this.inLove;
            String s = "heart";

            if (this.inLove % 10 == 0)
            {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(s, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
        if (this.cooldown > 0)
        {
            this.cooldown--;
            if (cooldown % 2 == 0)
            {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;

                this.worldObj.spawnParticle("smoke", this.posX + rand.nextFloat(), this.posY + 0.5D + rand.nextFloat(), this.posZ + rand.nextFloat(), d0, d1, d2);
            }
        }
    }

    @SuppressWarnings("unused")
    private void procreate(EntitySeedMob par1EntityAnimal)
    {
        EntityAgeable entityageable = this.createChild(par1EntityAnimal);

        if (entityageable != null)
        {
            if (this.field_146084_br == null && par1EntityAnimal.func_146083_cb() != null)
            {
                this.field_146084_br = par1EntityAnimal.func_146083_cb();
            }

            if (this.field_146084_br != null)
            {
                this.field_146084_br.triggerAchievement(StatList.field_151186_x);

            }

            this.setGrowingAge(6000);
            par1EntityAnimal.setGrowingAge(6000);
            this.inLove = 0;
            par1EntityAnimal.inLove = 0;
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);

            for (int i = 0; i < 7; ++i)
            {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("heart", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }

            this.worldObj.spawnEntityInWorld(entityageable);
        }
    }

    protected boolean canDespawn()
    {
        return false;
    }

    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }

    protected String getLivingSound()
    {
        return "chowtime:mob.glog.say";
    }

    protected String getHurtSound()
    {
        return "chowtime:mob.glog.hurt";
    }

    protected String getDeathSound()
    {
        return "chowtime:mob.glog.grunt";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.worldObj.playSoundAtEntity(this, "chowtime:mob.glog.step", 0.1F, 1.0F);
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        Random random = new Random();
        int n = random.nextInt(SeedRegistry.getSeeds().length);

        if (itemstack != null && itemstack.getItem() == Items.wheat_seeds && !par1EntityPlayer.capabilities.isCreativeMode)
        {
            if (cooldown > 0)
            {
                for (int i = 0; i < 3; i++)
                {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle("largeexplode", this.posX + this.rand.nextFloat(), this.posY + 0.5D + (double) this.rand.nextFloat(), this.posZ + this.rand.nextFloat(), d0, d1, d2);
                }
                this.worldObj.playSoundAtEntity(this, "chowtime:mob.glog.grunt", 0.9F, 1.0F);
                return false;
            }
            else
            {
                if (!worldObj.isRemote)
                {
                    par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(SeedRegistry.getSeeds()[n], 1, 0));
                    par1EntityPlayer.inventory.consumeInventoryItem(Items.wheat_seeds);
                    par1EntityPlayer.inventoryContainer.detectAndSendChanges();
                }
                this.cooldown = 600;//30 sec.
                return true;
            }
        }
        else
        {
            return super.interact(par1EntityPlayer);
        }

    }

    @Override
    protected Item getDropItem()
    {
        return null;
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.worldObj.getBlock(i, j - 1, k) == Blocks.grass || this.worldObj.getBlock(i, j - 1, k) == Blocks.sand) && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return new EntitySeedMob(this.worldObj);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tags)
    {
        inLove=tags.getInteger("inlove");
        cooldown=tags.getInteger("cooldown");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tags)
    {
        tags.setInteger("inlove",inLove);
        tags.setInteger("cooldown",cooldown);
    }
    //
    //    @Override
    //    public String getOwnerName() {
    //        return null;
    //    }
}
