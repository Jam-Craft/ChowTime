package net.jamcraft.chowtime.dyn;

/**
 * Created by James Hollowell on 5/14/2014.
 */
public class DynMain
{
    public static void init()
    {
        try
        {
            DynTextures.addDynTP();

            DynItems.loadList();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}