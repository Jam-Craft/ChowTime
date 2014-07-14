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

package net.jamcraft.chowtime.core.registrars;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James Hollowell on 5/23/2014.
 */
public class HarvestLevelRegistry
{
    private static Map<Block, Integer> blockReg = new HashMap<Block, Integer>();
    private static Map<Item, Block> seedReg = new HashMap<Item, Block>();

    /**
     * Registers a crop block and its seed item with the harvesting level
     * required for it.
     * @param crop The crop block
     * @param seed The seed item
     * @param level The level required to harvest/plant this crop
     */
    public static void AddBlockHavestXP(Block crop, Item seed, int level)
    {
        blockReg.put(crop, Integer.valueOf(level));
        seedReg.put(seed, crop);
    }

    public static Block[] GetCropsForLevel(int level)
    {
        List<Block> blocks = new ArrayList<Block>();
        for (Block b : blockReg.keySet())
        {
            if (level >= blockReg.get(b)) blocks.add(b);
        }
        return blocks.toArray(new Block[] { });
    }

    /**
     *
     * @param block The block to check
     * @param level The level to check
     * @return Returns whether a specified block's harvest level is less than
     * or equal to the level given
     */
    public static boolean IsCropAtLevel(Block block, int level)
    {
        if (!blockReg.keySet().contains(block)) return false;
        for (Block b : blockReg.keySet())
        {
            if (b.equals(block) && blockReg.get(b) >= level) return true;
        }
        return false;
    }

    /**
     *
     * @param seed The seed to check
     * @param level The level to check
     * @return Returns whether a specified item's harvest level is less than
     * or equal to the level given
     */
    public static boolean IsSeedAtLevel(Item seed, int level)
    {
        if (!seedReg.keySet().contains(seed)) return false;
        return IsCropAtLevel(seedReg.get(seed), level);
    }

    public static boolean IsRegistered(Block block)
    {
        return blockReg.containsKey(block);
    }

    public static boolean IsRegistered(Item item)
    {
        return seedReg.containsKey(item);
    }

    public static int LevelForSeed(Item item)
    {
        return blockReg.get(seedReg.get(item));
    }
}
