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

package net.jamcraft.chowtime.core.util;

/**
 * Created by James Hollowell on 5/15/2014.
 */
public class Version implements Comparable<Version>
{
    private int Major = 0;
    private int Minor = 0;
    private int Build = 0;
    private int Revision = 0;

    public int getMajor()
    {
        return Major;
    }

    public int getMinor()
    {
        return Minor;
    }

    public int getBuild()
    {
        return Build;
    }

    public int getRevision()
    {
        return Revision;
    }

    public Version(int major, int minor, int build, int revision)
    {
        Major = major;
        Minor = minor;
        Build = build;
        Revision = revision;
    }

    @Override
    public String toString()
    {
        return Major + "." + Minor + "." + Build + (Revision != 0 ? "-rev" + Revision : "");
    }

    public void readFromString(String s)
    {
        String pre=(s.contains("-rev")?s.split("-rev")[0]:s);
        Major = Integer.parseInt(pre.split("\\.")[0]);
        Minor = Integer.parseInt(pre.split("\\.")[1]);
        Build = Integer.parseInt(pre.split("\\.")[2]);
        if (s.contains("-rev"))
        {
            Revision = Integer.parseInt(s.split("-rev")[1]);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Version)) return false;
        Version other = (Version) o;
        return this.compareTo(other)==0;
    }

    @Override
    public int compareTo(Version o)
    {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this == o) return EQUAL;

        if (this.Major < o.Major) return BEFORE;
        if (this.Major > o.Major) return AFTER;

        if (this.Minor < o.Minor) return BEFORE;
        if (this.Minor > o.Minor) return AFTER;

        if (this.Build < o.Build) return BEFORE;
        if (this.Build > o.Build) return AFTER;

        if (this.Revision < o.Revision) return BEFORE;
        if (this.Revision > o.Revision) return AFTER;

        return EQUAL;
    }
}
