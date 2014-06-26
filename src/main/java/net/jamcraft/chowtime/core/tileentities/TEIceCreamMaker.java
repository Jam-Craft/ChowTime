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

package net.jamcraft.chowtime.core.tileentities;

import net.jamcraft.chowtime.core.recipies.IceCreamRecipies;
import net.jamcraft.chowtime.core.recipies.Recipe2_1;
import net.jamcraft.chowtime.core.util.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by James Hollowell on 5/16/2014.
 */

//TODO: Work on ISided-ness
public class TEIceCreamMaker extends TileEntity implements ISidedInventory
{
    public static final int IN1_LOC = 0;
    public static final int IN2_LOC = 1;
    public static final int OUT_LOC = 2;
    public static final int FUEL_LOC = 3;
    public static final int BUCKET_OUT_LOC = 4;
    //    public static final int FREEZING_TEMP = -21000;
    public static final int ROOM_TEMP = 20000;
    public static final int MIN_TEMP = -50000;

    public static final int INV_SIZE = 5;
    private ItemStack[] inventory = new ItemStack[INV_SIZE];
    private int ticksLeft = 0;
    private int maxTicks = 0;
    private int temp = ROOM_TEMP; //room temperature (1000=1 Degree Celcius)
    private int freezeTemp = MIN_TEMP;

    public TEIceCreamMaker()
    {
    }

    @Override
    public int getSizeInventory()
    {
        return INV_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        if (var1 > INV_SIZE) return null;
        return inventory[var1];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        inventory[slot] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return "container.IceCreamMaker";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot == IN1_LOC || slot == IN2_LOC)
            return IceCreamRecipies.GetRecipesFromStack(stack) != null;
        if (slot == FUEL_LOC) return TEIceCreamMaker.isIceFuel(stack);
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if (ForgeDirection.UP.ordinal() == side)
            return new int[] { IN1_LOC, IN2_LOC };
        if (side == ForgeDirection.DOWN.ordinal())
            return new int[] { OUT_LOC, BUCKET_OUT_LOC };
        return new int[] { FUEL_LOC };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        if (slot == OUT_LOC || slot == BUCKET_OUT_LOC || side == ForgeDirection.DOWN.ordinal())
            return false;
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side)
    {
        return (slot == OUT_LOC || slot == BUCKET_OUT_LOC) && side == ForgeDirection.DOWN.ordinal();
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);

        tags.setInteger("timeleft", ticksLeft);
        tags.setInteger("maxTime", maxTicks);
        tags.setInteger("temp", temp);
        tags.setInteger("freezingTemp", freezeTemp);

        if (inventory[0] != null)
        {
            NBTTagCompound sl1 = new NBTTagCompound();
            inventory[0].writeToNBT(sl1);
            tags.setTag("slot1", sl1);
        }

        if (inventory[1] != null)
        {
            NBTTagCompound sl2 = new NBTTagCompound();
            inventory[1].writeToNBT(sl2);
            tags.setTag("slot2", sl2);
        }

        if (inventory[2] != null)
        {
            NBTTagCompound sl3 = new NBTTagCompound();
            inventory[2].writeToNBT(sl3);
            tags.setTag("slot3", sl3);
        }

        if (inventory[3] != null)
        {
            NBTTagCompound sl4 = new NBTTagCompound();
            inventory[3].writeToNBT(sl4);
            tags.setTag("slot4", sl4);
        }

        if (inventory[4] != null)
        {
            NBTTagCompound sl5 = new NBTTagCompound();
            inventory[4].writeToNBT(sl5);
            tags.setTag("slot5", sl5);
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);

        ticksLeft = tags.getInteger("timeleft");
        maxTicks = tags.getInteger("maxTime");
        temp = tags.getInteger("temp");
        freezeTemp = tags.getInteger("freezingTemp");

        if (tags.hasKey("slot1"))
        {
            inventory[0] = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("slot1"));
        }
        if (tags.hasKey("slot2"))
        {
            inventory[1] = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("slot2"));
        }
        if (tags.hasKey("slot3"))
        {
            inventory[2] = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("slot3"));
        }
        if (tags.hasKey("slot4"))
        {
            inventory[3] = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("slot4"));
        }
        if (tags.hasKey("slot5"))
        {
            inventory[4] = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("slot5"));
        }
    }

    @Override
    public void updateEntity()
    {
        //Something in input and nothing currently processing
        if (inventory[0] != null && inventory[1] != null && ticksLeft == 0)
        {
            Recipe2_1 r = IceCreamRecipies.GetRecipeFromStack(inventory[0], inventory[1]);
            if (r != null)
            {
                maxTicks = r.getTime();
                freezeTemp = r.getTemp();
            }
        }
        //Actual processing
        if (temp < freezeTemp)
        {
            if (ticksLeft < maxTicks && IceCreamRecipies.GetRecipeFromStack(inventory[0], inventory[1]) != null)
            {
                ItemStack output = IceCreamRecipies.GetRecipeFromStack(inventory[0], inventory[1]).getOutput();
                if (inventory[2] == null ||
                        (output.getItem().equals(inventory[2].getItem()) && output.getMaxStackSize() > inventory[2].stackSize) &&
                                (inventory[3] == null || inventory[3].getMaxStackSize() > inventory[3].stackSize))
                {
                    ticksLeft++;
                    temp += 3;
                }
                else
                {
                    ticksLeft = 0;
                }
            }
            if (IceCreamRecipies.GetRecipeFromStack(inventory[0], inventory[1]) == null && ticksLeft > 0)
            {
                ticksLeft = 0;
            }
            if (ticksLeft == maxTicks && maxTicks != 0)
            {
                ticksLeft = 0;
                make();
            }
        }

        if (inventory[3] != null && this.worldObj.getTotalWorldTime() % 3 == 0)
        {
            if (temp - iceFuelValue(inventory[3]) > MIN_TEMP)
            {
                if (isIceFuel(inventory[3]))
                {
                    temp -= iceFuelValue(inventory[3]);

                    inventory[3].stackSize--;
                    if (inventory[3].stackSize <= 0)
                    {
                        inventory[3] = null;
                    }
                }
            }
        }

        if (temp < ROOM_TEMP && this.worldObj.getTotalWorldTime() % 10 == 0)
        {
            temp++;
        }
    }

    private void make()
    {
        if (inventory[0] == null || inventory[1] == null) return;
        ItemStack res = IceCreamRecipies.GetRecipeFromStack(inventory[0], inventory[1]).getOutput();
        if (inventory[2] == null)
            inventory[2] = res.copy();
        else
            inventory[2].stackSize += res.stackSize;

        inventory[0].stackSize--;
        if (inventory[0].getItem() instanceof ItemBucketMilk || inventory[0].getItem() instanceof ItemBucket)
        {
            if (inventory[BUCKET_OUT_LOC] == null)
            {
                inventory[BUCKET_OUT_LOC] = new ItemStack(Items.bucket);
            }
            else
            {
                inventory[BUCKET_OUT_LOC].stackSize++;
            }
        }
        if (inventory[0].stackSize <= 0) inventory[0] = null;

        inventory[1].stackSize--;
        if (inventory[1].getItem() instanceof ItemBucketMilk || inventory[1].getItem() instanceof ItemBucket)
        {
            if (inventory[BUCKET_OUT_LOC] == null)
            {
                inventory[BUCKET_OUT_LOC] = new ItemStack(Items.bucket);
            }
            else
            {
                inventory[BUCKET_OUT_LOC].stackSize++;
            }
        }
        if (inventory[1].stackSize <= 0) inventory[1] = null;
    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getScaledProgress(int scale)
    {
        if (maxTicks == 0) return 0;
        return ticksLeft * scale / maxTicks;
    }

    public int getScaledTemp(int scale)
    {
        return (ROOM_TEMP - temp) * scale / (ROOM_TEMP - MIN_TEMP);
    }

    public int getTemp()
    {
        return temp;
    }

    public int getFreezeTemp()
    {
        return freezeTemp;
    }

    public int getScaledFreezeTemp(int scale)
    {
        return (ROOM_TEMP - freezeTemp) * scale / (ROOM_TEMP - MIN_TEMP);
    }

    public static boolean isIceFuel(ItemStack stack)
    {
        return iceFuelValue(stack) != 0;
    }

    public static int iceFuelValue(ItemStack stack)
    {
        if (stack == null) return 0;
        Item i = stack.getItem();
        if (i.equals(Items.snowball))
        {
            return 100;
        }
        if (i.equals(Item.getItemFromBlock(Blocks.snow)))
        {
            return 400;
        }
        if (i.equals(Item.getItemFromBlock(Blocks.ice)))
        {
            return 300;
        }
        if (i.equals(Item.getItemFromBlock(Blocks.packed_ice)))
        {
            return 1000;
        }
        return 0;
    }

    public void dropContents()
    {
        for (ItemStack stack : inventory)
        {
            ItemHelper.spawnItemStackInWorld(stack, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
    }
}
