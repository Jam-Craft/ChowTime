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

package net.jamcraft.chowtime.core.client.gui.foodbook;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import cpw.mods.fml.common.registry.GameData;
import net.jamcraft.chowtime.core.client.Textures;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by James Hollowell on 5/31/2014.
 */
public class GuiFoodBook extends GuiScreen
{
    private static final int BOOK_BTN_NEXT = 0;
    private static final int BOOK_BTN_PREV = 1;

    public static final int WIDTH = 175;
    public static final int HEIGHT = 228;

    private GuiButton next;
    private GuiButton prev;

    private int pageIndex = 0;

    public List<ItemFood> foods = new ArrayList<ItemFood>();
    public List<BookPage> pages = new ArrayList<BookPage>();

    public int bookXStart = 0;

    public GuiFoodBook()
    {
        super();
        if (foods.size() == 0)
        {
            findFoods();
        }
    }

    @SuppressWarnings("rawtypes")
    private void findFoods()
    {
        Iterator iter = GameData.getItemRegistry().iterator();
        while (iter.hasNext())
        {
            Object item = iter.next();
            if (item instanceof ItemFood)
            {
                if (item instanceof ItemFishFood) continue; //for now... ;)
                foods.add((ItemFood) item);
            }
        }
        foods = Ordering.from(String.CASE_INSENSITIVE_ORDER).onResultOf(new Function<ItemFood, String>()
        {
            @Override
            public String apply(ItemFood input)
            {
                return StatCollector.translateToLocal(input.getUnlocalizedName() + ".name");
            }
        }).immutableSortedCopy(foods);
    }

    private void makePages()
    {
        pages.clear();
        int foodCurrent = 0;
        do
        {
            BookPage newBook = new BookPage(this, foodCurrent);
            foodCurrent = newBook.calculateEndIndex() + 1;
            pages.add(newBook);
        }
        while (foodCurrent < foods.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        makePages();

        bookXStart = (width - WIDTH) / 2;

        buttonList.add(next = new GuiButtonPageChange(BOOK_BTN_NEXT, bookXStart + WIDTH - 26, 210, false));
        buttonList.add(prev = new GuiButtonPageChange(BOOK_BTN_PREV, bookXStart + 10, 210, true));
        updateButtonState();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case BOOK_BTN_NEXT:
                pageIndex++;
                break;
            case BOOK_BTN_PREV:
                --pageIndex;
                break;
        }
        updateButtonState();
    }

    private void updateButtonState()
    {
        next.visible = pageIndex < pages.size();
        prev.visible = pageIndex > 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials)
    {
        drawBackground();
        drawForeground();

        super.drawScreen(mouseX, mouseY, renderPartials);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key)
    {
        char lowerCase = Character.toLowerCase(c);
        if (key == Keyboard.KEY_ESCAPE)
        {
            mc.displayGuiScreen(null);
        }
        else if (key==Keyboard.KEY_LEFT && pageIndex>0)
        {
            pageIndex--;
            updateButtonState();
        }
        else if(key==Keyboard.KEY_RIGHT && pageIndex<pages.size())
        {
            pageIndex++;
            updateButtonState();
        }
        else if (Character.getType(lowerCase) == Character.LOWERCASE_LETTER)
        {
            for (int i = 0, len = pages.size(); i < len; ++i)
            {
                BookPage page = pages.get(i);
                if (page.containsCharacter(lowerCase))
                {
                    //Go to the next page w/ that char if we have it...
                    if (pageIndex == i + 1)
                    {
                        if (i + 1 < pages.size() && pages.get(i + 1).containsCharacter(lowerCase))
                        {
                            pageIndex = i + 2;
                        }
                    }
                    else
                    {
                        pageIndex = i + 1;
                    }
                    updateButtonState();
                    break;
                }
            }
        }
    }

    protected void drawBackground()
    {
        mc.renderEngine.bindTexture(Textures.Gui_FoodBook);
        drawTexturedModalRect(bookXStart, 5, 0, 0, WIDTH, HEIGHT);
    }

    protected void drawForeground()
    {
        if (pageIndex == 0)
            drawStartScreen();
        else
            drawFoodPage();
        drawPages();
    }

    protected void drawStartScreen()
    {
        fontRendererObj.drawString(StatCollector.translateToLocal("gui.FoodBook.Title"), bookXStart + 45, 20, 0x000000);
        fontRendererObj.drawSplitString(StatCollector.translateToLocal("gui.FoodBook.MainDesc"), bookXStart + 20, 60, WIDTH-40, 0x000000);
    }

    protected void drawFoodPage()
    {
        BookPage pg = pages.get(pageIndex - 1);
        pg.RenderPage(itemRender);
    }

    protected void drawPages()
    {
        fontRendererObj.drawString((pageIndex + 1) + "/" + (pages.size()+1), bookXStart + 82, 215, 0x000000);
    }
}
