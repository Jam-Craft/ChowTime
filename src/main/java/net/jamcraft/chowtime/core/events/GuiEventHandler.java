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

import net.jamcraft.chowtime.ChowTime;
import net.jamcraft.chowtime.core.Config;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPClient;
import net.jamcraft.chowtime.core.harvestxp.HarvestXPCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by James Hollowell on 6/10/2014.
 */
public class GuiEventHandler
{
    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent.Text event)
    {
        if(Config.shouldRenderXP)
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            FontRenderer fontrenderer = this.mc.fontRenderer;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j2;
            int k2;
            String s3 = "ChowTime";
            j2 = k - fontrenderer.getStringWidth(s3) - 5;
            k2 = l - 131;
            fontrenderer.drawString(s3, j2 + 1, k2, 0);
            fontrenderer.drawString(s3, j2 - 1, k2, 0);
            fontrenderer.drawString(s3, j2, k2 + 1, 0);
            fontrenderer.drawString(s3, j2, k2 - 1, 0);
            for (int i = 0; i <= 7; i++)
                fontrenderer.drawString("_", j2 + i * 6 - 2, k2 + 2, 0);
            fontrenderer.drawString(s3, j2, k2, 0x00c2e0);

            s3 = "XP: " + HarvestXPClient.INSTANCE.xp;
            j2 = k - fontrenderer.getStringWidth(s3) - 5;
            k2 += 12;
            fontrenderer.drawString(s3, j2 + 1, k2, 0);
            fontrenderer.drawString(s3, j2 - 1, k2, 0);
            fontrenderer.drawString(s3, j2, k2 + 1, 0);
            fontrenderer.drawString(s3, j2, k2 - 1, 0);
            fontrenderer.drawString(s3, j2, k2, 0x22c530);

            int xp = 0;
            if (HarvestXPClient.INSTANCE.xp < 20) xp = 20;
            if (HarvestXPClient.INSTANCE.xp >= 20 && HarvestXPClient.INSTANCE.xp < 100) xp = 100;
            if (HarvestXPClient.INSTANCE.xp >= 100 && HarvestXPClient.INSTANCE.xp < 300) xp = 300;
            if (HarvestXPClient.INSTANCE.xp < 300)
            {
                s3 = "Next farming upgrade:";
                j2 = k - fontrenderer.getStringWidth(s3) - 5;
                k2 += 12;
                fontrenderer.drawString(s3, j2 + 1, k2, 0);
                fontrenderer.drawString(s3, j2 - 1, k2, 0);
                fontrenderer.drawString(s3, j2, k2 + 1, 0);
                fontrenderer.drawString(s3, j2, k2 - 1, 0);
                fontrenderer.drawString(s3, j2, k2, 0xfeee00);

                s3 = "" + xp;
                j2 = k - fontrenderer.getStringWidth(s3) - 8 - fontrenderer.getStringWidth("XP");
                k2 += 12;
                fontrenderer.drawString(s3, j2 + 1, k2, 0);
                fontrenderer.drawString(s3, j2 - 1, k2, 0);
                fontrenderer.drawString(s3, j2, k2 + 1, 0);
                fontrenderer.drawString(s3, j2, k2 - 1, 0);
                fontrenderer.drawString(s3, j2, k2, 0xfe0000);

                s3 = " XP";
                j2 = k - fontrenderer.getStringWidth(s3) - 5;
                fontrenderer.drawString(s3, j2 + 1, k2, 0);
                fontrenderer.drawString(s3, j2 - 1, k2, 0);
                fontrenderer.drawString(s3, j2, k2 + 1, 0);
                fontrenderer.drawString(s3, j2, k2 - 1, 0);
                fontrenderer.drawString(s3, j2, k2, 0xfeee00);
            }
        }
    }
}
