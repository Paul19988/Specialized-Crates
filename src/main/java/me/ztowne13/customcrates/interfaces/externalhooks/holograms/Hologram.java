package me.ztowne13.customcrates.interfaces.externalhooks.holograms;

import me.ztowne13.customcrates.SpecializedCrates;
import org.bukkit.Location;

public abstract class Hologram {
    SpecializedCrates customCrates;

    String name;
    Location location;

    public Hologram(SpecializedCrates cc, String name, Location location) {
        this.customCrates = cc;
        this.name = name;
        this.location = location;
    }

    public abstract void addLine(String line);

    public abstract void setLine(int i, String line);

    public abstract void update();

    public SpecializedCrates getCustomCrates() {
        return customCrates;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
