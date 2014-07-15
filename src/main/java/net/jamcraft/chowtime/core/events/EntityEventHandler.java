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

package net.jamcraft.chowtime.core.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.jamcraft.chowtime.core.config.Config;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPClient;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPServer;
import net.jamcraft.chowtime.core.registrars.HarvestLevelRegistry;
import net.jamcraft.chowtime.remote.RemoteMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by DarkKnight on 5/18/14.
 */
public class EntityEventHandler
{
    private static boolean HasBeenNotified = false;

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;

            //            //TODO: Remove in next update
            //            ChowTime.harvestXP = ChowTime.saveData.getInteger("harvestXP" + (player).getCommandSenderName());
            //            ChowTime.harvestLVL = ChowTime.saveData.getInteger("harvestLVL" + (player).getCommandSenderName());
            //
            //            //TODO: Remove in next update
            //            //Backwards compat
            //            if (!event.world.isRemote)
            //            {
            //                //Only do it if the new system is @ 0.
            //                if(HarvestXPServer.INSTANCE.GetXPForUser(player.getCommandSenderName())==0)
            //                {
            //                    HarvestXPServer.INSTANCE.SetXPForUser(player, ChowTime.harvestXP);
            //                }
            //            }
            //            if (event.world.isRemote)
            //            {
            //                HarvestXPClient.INSTANCE.xp=ChowTime.harvestXP;
            //            }

            if (event.world.isRemote && !HasBeenNotified)
            {
                if (HarvestXPClient.INSTANCE.xp == 0)
                    player.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.welcomeMessage")));
                if (RemoteMain.hasUpdated)
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("string.updated")));
                    RemoteMain.hasUpdated = false;
                }
                if (Config.forceLocal)
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("string.warnlocal")));
                }
                if (Config.useDev)
                {
                    player.addChatComponentMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("string.usedev")));
                }
                RemoteMain.player = player;
                HasBeenNotified = true;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        //        //TODO: Remove on update
        //        if (!event.world.isRemote)
        //        {
        //            new File(ChowTime.dir + File.separator + "ChowTime").mkdirs();
        //            ChowTime.harvestingLVL = new File(ChowTime.dir + File.separator + "ChowTime", "CT" + event.world.getWorldInfo().getWorldName() + ".cfg");
        //            try
        //            {
        //                if (!ChowTime.harvestingLVL.exists())
        //                    ChowTime.harvestingLVL.createNewFile();
        //            }
        //            catch (IOException e)
        //            {
        //                e.printStackTrace();
        //            }
        //        }
        //
        //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        //        {
        ////            HarvestXPServer.INSTANCE.Load();
        //
        //            //TODO: This code will be removed in the next release, it is simply to permit compat w/ old save format
        //            try
        //            {
        //                if (ChowTime.harvestingLVL.exists())
        //                    ChowTime.saveData = CompressedStreamTools.readCompressed(new FileInputStream(ChowTime.harvestingLVL));
        //            }
        //            catch (EOFException e)
        //            {
        //                e.printStackTrace();
        //            }
        //            catch (IOException e)
        //            {
        //                e.printStackTrace();
        //            }
        //        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            HarvestXPServer.INSTANCE.Save();

            //Don't save to the old save format, only load from it.

            //            try
            //            {
            //                if (ChowTime.harvestingLVL.exists())
            //                    CompressedStreamTools.writeCompressed(ChowTime.saveData, new FileOutputStream(ChowTime.harvestingLVL));
            //                int i = event.world.playerEntities.size();
            //                for (int j = 0; j < i; j++)
            //                {
            //                    ChowTime.saveData.setInteger("harvestXP" + ((EntityPlayer) event.world.playerEntities.get(j)).getCommandSenderName(), ChowTime.harvestXP);
            //                    ChowTime.saveData.setInteger("harvestLVL" + ((EntityPlayer) event.world.playerEntities.get(j)).getCommandSenderName(), ChowTime.harvestLVL);
            //                }
            //            }
            //            catch (EOFException e)
            //            {
            //                e.printStackTrace();
            //            }
            //            catch (IOException e)
            //            {
            //                e.printStackTrace();
            //            }
        }
    }

    //    @SubscribeEvent
    //    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    //            if (event.entityLiving instanceof EntityPlayer)
    //            {
    //            }
    //        }
    //    }

    //    @SubscribeEvent
    //    public void onItemUseStart(PlayerUseItemEvent.Start event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    //
    //        }
    //    }

    //    @SubscribeEvent
    //    public void onItemUseTick(PlayerUseItemEvent.Tick event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    //
    //        }
    //    }

    //    @SubscribeEvent
    //    public void onItemUseStopBeforeFinish(PlayerUseItemEvent.Stop event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    //
    //        }
    //    }

    //    @SubscribeEvent
    //    public void onItemUseFinish(PlayerUseItemEvent.Finish event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    // if (event.item.getItem() instanceof ItemAppleGold ||
    // event.item.getItem() instanceof ItemFood || event.item.getItem()
    // instanceof ItemSoup || event.item.getItem() instanceof
    // ItemFishFood) event.entityPlayer.addChatMessage(new
    // ChatComponentTranslation("Munch munch munch"));
    //        }
    //    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            String user = event.entityPlayer.getCommandSenderName();
            int harvestXP = HarvestXPServer.INSTANCE.GetXPForUser(user);
            if (!(event.entityPlayer instanceof FakePlayer))
            {
                boolean canHarvest = true;
                Block block2 = event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z);
                if (harvestXP < 20)
                {
                    if (HarvestLevelRegistry.IsCropAtLevel(event.block, 2) || HarvestLevelRegistry.IsCropAtLevel(block2, 2))
                        //                    if ((event.block instanceof CropTomato || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropTomato) || (event.block instanceof CropCranberry || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropCranberry) || (event.block instanceof CropRaspberry || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropRaspberry))
                        canHarvest = false;
                }
                if (harvestXP < 100)
                {
                    if (HarvestLevelRegistry.IsCropAtLevel(event.block, 3) || HarvestLevelRegistry.IsCropAtLevel(block2, 3) || HarvestLevelRegistry.IsCropAtLevel(event.block, 4) || HarvestLevelRegistry.IsCropAtLevel(block2, 4))
                        //                    if ((event.block instanceof CropCorn || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropCorn) || (event.block instanceof CropGrape || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropGrape))
                        canHarvest = false;
                }
                if (harvestXP < 300)
                {
                    if (HarvestLevelRegistry.IsCropAtLevel(event.block, 5) || HarvestLevelRegistry.IsCropAtLevel(block2, 5))
                        //                    if ((event.block instanceof CropStrawberry || event.entityPlayer.worldObj.getBlock(event.x, event.y + 1, event.z) instanceof CropStrawberry))
                        canHarvest = false;
                }
                if (!canHarvest)
                {
                    event.entityPlayer.worldObj.markBlockForUpdate(event.x, event.y, event.z);
                    event.setCanceled(true);
                    return;
                }
            }
            if (event.metadata == 7 && !(event.entityPlayer instanceof FakePlayer))
            {
                int hx1 = harvestXP;
                if (harvestXP >= 300 && HarvestLevelRegistry.IsCropAtLevel(event.block, 5))
                    harvestXP += 10;
                else if (harvestXP >= 100 && HarvestLevelRegistry.IsCropAtLevel(event.block, 4))
                    harvestXP += 5;
                else if (harvestXP >= 100 && HarvestLevelRegistry.IsCropAtLevel(event.block, 3))
                    harvestXP += 4;
                else if (harvestXP >= 20 && HarvestLevelRegistry.IsCropAtLevel(event.block, 2))
                    harvestXP += 2;
                else if (harvestXP >= 0 && HarvestLevelRegistry.IsCropAtLevel(event.block, 1))
                    harvestXP += 1;
                
                if (hx1 != harvestXP)
                    HarvestXPServer.INSTANCE.SetXPForUser(event.entityPlayer, harvestXP);
            }
        }
    }

    //    @SubscribeEvent
    //    public void harvestCheck(PlayerEvent.HarvestCheck event)
    //    {
    //        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
    //        {
    //        }
    //    }

    @SubscribeEvent
    public void blockInteraction(PlayerInteractEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            if (event.action == Action.LEFT_CLICK_BLOCK && !(event.entityPlayer instanceof FakePlayer))
            {
                if (HarvestLevelRegistry.IsRegistered(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z)))
                //                if (event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropStrawberry || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropBlueberry || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropCranberry || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropRaspberry || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropTomato || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropCorn || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropGrape || event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof CropBarley)
                {
                    // This needs to change slightly... if you go from 99 to
                    // 101, you never get the message...
                    int harvestXP = HarvestXPServer.INSTANCE.GetXPForUser(event.entityPlayer.getCommandSenderName());
                    if (harvestXP == 19)
                        event.entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.HXPGain20")));
                    if (harvestXP == 99)
                        event.entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.HXPGain100")));
                    if (harvestXP == 299)
                        event.entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.HXPGain300")));
                }
            }
            if (event.action == Action.RIGHT_CLICK_BLOCK && !(event.entityPlayer instanceof FakePlayer) && event.entityPlayer.getHeldItem() != null && event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) instanceof BlockFarmland)
            {
                int harvestXP = HarvestXPServer.INSTANCE.GetXPForUser(event.entityPlayer.getCommandSenderName());
                boolean canPlant = true;
                if (harvestXP < 20)
                {
                    if (HarvestLevelRegistry.IsSeedAtLevel(event.entityPlayer.getHeldItem().getItem(), 2))
                        //                    if (event.entityPlayer.getHeldItem().getItem() instanceof SeedCranberry || event.entityPlayer.getHeldItem().getItem() instanceof SeedTomato || event.entityPlayer.getHeldItem().getItem() instanceof SeedRaspberry)
                        canPlant = false;
                }
                if (harvestXP < 100)
                {
                    if (HarvestLevelRegistry.IsSeedAtLevel(event.entityPlayer.getHeldItem().getItem(), 3) || HarvestLevelRegistry.IsSeedAtLevel(event.entityPlayer.getHeldItem().getItem(), 4))
                        //                    if (event.entityPlayer.getHeldItem().getItem() instanceof SeedCorn || event.entityPlayer.getHeldItem().getItem() instanceof SeedGrape)
                        canPlant = false;
                }
                if (harvestXP < 300)
                {
                    if (HarvestLevelRegistry.IsSeedAtLevel(event.entityPlayer.getHeldItem().getItem(), 5))
                        //                    if (event.entityPlayer.getHeldItem().getItem() instanceof SeedStrawberry)
                        canPlant = false;
                }
                if (!canPlant)
                {
                    event.setCanceled(true);
                    event.entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ctprefix").appendSibling(new ChatComponentTranslation("chat.notExperienced")));
                    event.entityPlayer.addChatMessage(new ChatComponentTranslation("chat.gainExperience"));
                }
            }
        }
    }

    //    @SubscribeEvent
    //    public void useHoe(UseHoeEvent event)
    //    {
    //        Block block = event.world.getBlock(event.x, event.y, event.z);
    //        if ((block instanceof BlockDirt || block instanceof BlockGrass) && event.world.getBlock(event.x, event.y + 1, event.z) instanceof BlockAir)
    //        {
    //            event.world.playSoundEffect((double)((float)event.x + 0.5F), (double)((float)event.y + 0.5F), (double)((float)event.z + 0.5F), Blocks.farmland.stepSound.getStepResourcePath(), (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.farmland.stepSound.getPitch() * 0.8F);
    //            event.current.damageItem(1, event.entityPlayer);
    //            event.world.setBlock(event.x, event.y, event.z, CTInits.CTFarmland);
    //            event.current.useItemRightClick(event.world, event.entityPlayer);
    //        }
    //    }
}
