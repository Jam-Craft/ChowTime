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

package net.jamcraft.chowtime.dyn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by James Hollowell on 6/29/2014.
 */
public class ReloadableClassLoader extends URLClassLoader
{
    public ReloadableClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }

    public ReloadableClassLoader(URL[] urls)
    {
        super(urls);
    }

    public Class<?> reloadClass(String name) throws ClassNotFoundException
    {
        //This will throw an exception if the class is not already loaded
//        Class.forName(name);
        try
        {
            InputStream is = this.getResourceAsStream(name.replace('.','/')+".class");
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int data = is.read();
            while (data != -1)
            {
                buf.write(data);
                data = is.read();
            }

            is.close();

            byte[] classData = buf.toByteArray();

            return defineClass(name, classData, 0, classData.length);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new ClassNotFoundException(name + " could not be located");
        }
    }
}
