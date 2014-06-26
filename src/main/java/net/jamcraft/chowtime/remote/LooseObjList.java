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

package net.jamcraft.chowtime.remote;

import com.google.gson.JsonIOException;
import net.jamcraft.chowtime.core.util.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James Hollowell on 5/15/2014.
 */
public class LooseObjList
{
    protected List<DynDescription> descriptions = new ArrayList<DynDescription>();

    private boolean isLoaded = false;

    public List<DynDescription> getObjects()
    {
        return descriptions;
    }

    public void writeToFile(File out) throws IOException, JsonIOException
    {
        /* FILE DESCRIPTION:
        *********************************
        classes
        {
        net.jamcraft.chowtime. 0.0.1
        ...
        }
        resources
        {
        //based in DYN_LOC/assets/chowtime
        textures/items/Temp.png 0.0.1
        lang/en_US.lang 0.0.1
        }
        ***********************************
         */

        //Sort into two seperate lists...
        List<DynClassDescription> classes = new ArrayList<DynClassDescription>();
        List<DynResourceDescription> resources = new ArrayList<DynResourceDescription>();

        for (DynDescription desc : descriptions)
        {
            if (desc instanceof DynClassDescription)
                classes.add((DynClassDescription) desc);
            if (desc instanceof DynResourceDescription)
                resources.add((DynResourceDescription) desc);
        }

        if (!out.exists()) out.createNewFile();
        FileWriter fw = new FileWriter(out);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("classes");
        bw.newLine();
        bw.write("{");
        bw.newLine();
        for (DynClassDescription d : classes)
        {
            String s = d.classname + " " + d.version.toString();
            bw.write(s);
            bw.newLine();
        }
        bw.write("}");
        bw.newLine();

        bw.write("resources");
        bw.newLine();
        bw.write("{");
        bw.newLine();
        for (DynResourceDescription d : resources)
        {
            String s = d.path + " " + d.version.toString();
            bw.write(s);
            bw.newLine();
        }
        bw.write("}");
        bw.newLine();

        bw.close();
        isLoaded = true;
    }

    @SuppressWarnings("resource")
    public void readFromFile(File in)
    {
        try
        {
            if (!in.exists()) return;
            FileReader fr = new FileReader(in);
            BufferedReader br = new BufferedReader(fr);

            String l = br.readLine();
            if (!"classes".equals(l)) return;
            l = br.readLine();
            if (!"{".equals(l)) return;
            while (br.ready())
            {
                String line = br.readLine();
                if (line == null) return;
                if (line.equals("}")) break;
                DynClassDescription desc = new DynClassDescription();
                desc.classname = line.split(" ")[0];
                desc.version = new Version(0, 0, 0, 0);
                desc.version.readFromString(line.split(" ")[1]);
                descriptions.add(desc);
            }

            l = br.readLine();
            if (!"resources".equals(l)) return;
            l = br.readLine();
            if (!"{".equals(l)) return;
            while (br.ready())
            {
                String line = br.readLine();
                if (line.equals("}")) break;
                DynResourceDescription desc = new DynResourceDescription();
                desc.path = line.split(" ")[0];
                desc.version = new Version(0, 0, 0, 0);
                desc.version.readFromString(line.split(" ")[1]);
                descriptions.add(desc);
            }

            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof LooseObjList)) return false;

        LooseObjList other = (LooseObjList) o;

        //If they aren't the same size, they obviously aren't the same...
        if (this.getObjects().size() != other.getObjects().size()) return false;

        for (int i = 0; i < descriptions.size(); i++)
        {
            if (!descriptions.contains(other.getObjects().get(i))) return false;
        }

        return true;
    }

    public List<DynDescription> difference(LooseObjList other)
    {
        List<DynDescription> diff = new ArrayList<DynDescription>();
        for (DynDescription desc : descriptions)
        {
            if (!other.getObjects().contains(desc)) diff.add(desc);
        }
        return diff;
    }

    public void add(DynDescription desc)
    {
        descriptions.add(desc);
    }

    public boolean isLoaded()
    {
        return isLoaded;
    }
}
