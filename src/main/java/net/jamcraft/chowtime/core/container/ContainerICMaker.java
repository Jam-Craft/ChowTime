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

package net.jamcraft.chowtime.core.container;

import net.jamcraft.chowtime.core.container.slot.SlotICMaker;
import net.jamcraft.chowtime.core.container.slot.SlotIceFuel;
import net.jamcraft.chowtime.core.container.slot.SlotOutput;
import net.jamcraft.chowtime.core.recipies.IceCreamRecipies;
import net.jamcraft.chowtime.core.tileentities.TEFermenter;
import net.jamcraft.chowtime.core.tileentities.TEIceCreamMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by James Hollowell on 5/16/2014.
 */
public class ContainerICMaker extends Container
{

    public ContainerICMaker(InventoryPlayer playerInv, TEIceCreamMaker te)
    {
        // Add input 1
        this.addSlotToContainer(new SlotICMaker(te, 0, 54, 34));

        this.addSlotToContainer(new SlotICMaker(te, 1, 32, 34));

        this.addSlotToContainer(new SlotOutput(te, 2, 116, 34));

        this.addSlotToContainer(new SlotIceFuel(te, 3, 8, 62));

        this.addSlotToContainer(new SlotOutput(te, 4, 54, 62));

        // Add the player's inventory slots to the container
        for (int inventoryRowIndex = 0; inventoryRowIndex < 3; ++inventoryRowIndex)
        {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < 9; ++inventoryColumnIndex)
            {
                this.addSlotToContainer(new Slot(playerInv, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 84 + inventoryRowIndex * 18));
            }
        }

        // Add the player's action bar slots to the container
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            this.addSlotToContainer(new Slot(playerInv, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 142));
        }
    }

    @Override public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex)
    {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {

            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            /**
             * If we are shift-clicking an item out of the IceCreamMaker's
             * container, attempt to put it in the first available slot in the
             * player's inventory
             */
            if (slotIndex < TEIceCreamMaker.INV_SIZE)
            {
                if (!this.mergeItemStack(slotItemStack, TEIceCreamMaker.INV_SIZE, inventorySlots.size(), false))
                {
                    return null;
                }
            }
            else
            {
                /**
                 * If the stack being shift-clicked into the IceCreamMakers's
                 * container is a fuel, try to put it in the fuel slot.
                 */
                if (TEIceCreamMaker.isIceFuel(slotItemStack))
                {
                    if (!this.mergeItemStack(slotItemStack, TEIceCreamMaker.FUEL_LOC, TEIceCreamMaker.FUEL_LOC, true))
                    {
                        return null;
                    }
                }

                if (IceCreamRecipies.GetRecipesFromStack(slotItemStack) != null)
                {
                    if (!this.mergeItemStack(slotItemStack, 0, 2, false))
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }

            if (slotItemStack.stackSize <= 0)
            {
                slot.putStack((ItemStack) null);
            }

            slot.onSlotChanged();
        }

        return itemStack;
    }
}
