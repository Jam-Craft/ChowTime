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

package net.jamcraft.chowtime.compat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.jamcraft.chowtime.core.client.gui.GuiICMaker;
import net.jamcraft.chowtime.core.recipies.IceCreamRecipies;
import net.jamcraft.chowtime.core.recipies.Recipe2_1;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 5/25/2014.
 */
public class IceCreamMakerHandler extends TemplateRecipeHandler
{
    class CachedICMakerRecipe extends CachedRecipe
    {
        private ItemStack input1;
        private ItemStack input2;
        private ItemStack output;
        private int temp;

        public CachedICMakerRecipe(Recipe2_1 r)
        {
            input1 = r.getInput1();
            input2 = r.getInput2();
            output = r.getOutput();
            temp = r.getTemp();
        }

        @Override
        public PositionedStack getResult()
        {
            return new PositionedStack(output, 110, 23);
        }

        @Override
        public List<PositionedStack> getIngredients()
        {
            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
            stacks.add(new PositionedStack(input1, 50, 23));
            stacks.add(new PositionedStack(input2, 25, 23));
            return stacks;
        }

        public int getTemp()
        {
            return temp;
        }
    }

    @Override public String getGuiTexture()
    {
        return "chowtime:textures/gui/guiIceCreamMaker.png";
    }

    @Override public String getRecipeName()
    {
        return StatCollector.translateToLocal("container.IceCreamMaker");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("item"))
            loadCraftingRecipes((ItemStack) results[0]);
        else if (outputId.equals("allIC"))
        {
            for (Recipe2_1 r : IceCreamRecipies.GetAllRecipes())
            {
                arecipes.add(new CachedICMakerRecipe(r));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (Recipe2_1 r : IceCreamRecipies.GetAllRecipes())
        {
            if (r.getOutput().isItemEqual(result))
                arecipes.add(new CachedICMakerRecipe(r));
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients)
    {
        if (ingredients.length == 0) return;
        if ("item".equals(inputId))
        {
            for (Recipe2_1 r : IceCreamRecipies.GetRecipesFromStack((ItemStack) ingredients[0]))
                arecipes.add(new CachedICMakerRecipe(r));
        }
    }

    @Override
    public void drawExtras(int recipe)
    {
        CachedICMakerRecipe crecipe = (CachedICMakerRecipe) arecipes.get(recipe);

        //render progress bar
        //        drawProgressBar(79, 34, 176, 14, 20, 14, 24, 17);

        //render temperature
        GL11.glPushMatrix();
        GL11.glScaled(0.51F, 0.51F, 0.51F);
        double tempD = ((double) crecipe.getTemp()) / 1000;
        String temp = Double.toString(tempD) + "\u00B0C";
        GuiDraw.drawString(temp,9,55,4210752, false);
        GL11.glPopMatrix();

//        Tessellator tesser=Tessellator.instance;
//        tesser.startDrawing();

        //render blank card
        //        changeTexture("minestuck:textures/items/CardBlank.png");
        //        drawTexturedModalRect(21, 39, 0, 0, 16, 16,16,16);

        //render combo mode
        //        GuiDraw.drawString(crecipe.mode ? "&&" : "||", 22, 18, 0);
    }

    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6, int w, int h)
    {
        float f = (float) 1 / w;
        float f1 = (float) 1 / h;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6), 0.0F, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), 0.0F, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0), 0.0F, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), 0.0F, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.draw();
    }

    @Override
    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction)
    {
        if (direction > 3)
        {
            completion = 1 - completion;
            direction %= 4;
        }
        int var = (int) (completion * (direction % 2 == 0 ? w : h));

        switch (direction)
        {
            case 0://right
                this.drawTexturedModalRect(x, y, tx, ty, var, h, w, h);
                break;
            case 1://down
                this.drawTexturedModalRect(x, y, tx, ty, w, var, w, h);
                break;
            case 2://left
                this.drawTexturedModalRect(x + w - var, y, tx + w - var, ty, var, h, w, h);
                break;
            case 3://up
                this.drawTexturedModalRect(x, y + h - var, tx, ty + h - var, w, var, w, h);
                break;
        }
    }

    @Override
    public void loadTransferRects()
    {
        RecipeTransferRect rect = new RecipeTransferRect(new Rectangle(73, 25, 24, 17), "allIC");
        transferRects.add(rect);
        List<Class<? extends GuiContainer>> guis = new ArrayList<Class<? extends GuiContainer>>();
        guis.add(GuiICMaker.class);
        RecipeTransferRectHandler.registerRectsToGuis(guis, transferRects);
    }
}
