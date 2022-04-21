package net.marscraft.skyrpg.module.regions.region;

import org.bukkit.Location;

public class Bound {

    private Location loc1, loc2;

    public Bound() {}

    public Bound(Location loc1, Location loc2) {
        loc1.setY(0);
        loc2.setY(256);
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    /**
     * Checks if given Location is inside Boundary
     * @param location Location that gets checked
     * @return true if given location is inside Boundary
     * */
    public boolean isWithinBounds(Location location) {
        int loc1X = this.loc1.getBlockX(), loc1Y = this.loc1.getBlockY(), loc1Z = this.loc1.getBlockZ();
        int loc2X = this.loc2.getBlockX() + 1, loc2Y = this.loc2.getBlockY() + 1, loc2Z = this.loc2.getBlockZ() + 1;
        double x = location.getX(), y = location.getY(), z = location.getZ();
        return (x >= loc1X && x <= loc2X && y >= loc1Y && y <= loc2Y && z >= loc1Z && z <= loc2Z);
    }

    /**
     * Secures that loc1 is always the Location with lower cords
     * */
    public void assignCorrectBounds() {
        int loc1X = this.loc1.getBlockX(), loc1Y = this.loc1.getBlockY(), loc1Z = this.loc1.getBlockZ();
        int loc2X = this.loc2.getBlockX(), loc2Y = this.loc2.getBlockY(), loc2Z = this.loc2.getBlockZ();
        if (loc1X > loc2X) {
            int tempX = loc1X;
            loc1X = loc2X;
            loc2X = tempX;
        }
        if (loc1Y > loc2Y) {
            int tempY = loc1Y;
            loc1Y = loc2Y;
            loc2Y = tempY;
        }
        if (loc1Z > loc2Z) {
            int tempZ = loc1Z;
            loc1Z = loc2Z;
            loc2Z = tempZ;
        }
        this.loc1 = new Location(this.loc1.getWorld(), loc1X, loc1Y, loc1Z);
        this.loc2 = new Location(this.loc2.getWorld(), loc2X, loc2Y, loc2Z);
    }

    public boolean setupComplete() { return (loc1 != null && loc2 != null); }

    public Location getLoc1() { return loc1; }
    public void setLoc1(Location loc1) {
        loc1.setY(0);
        this.loc1 = loc1;
    }

    public Location getLoc2() { return loc2; }
    public void setLoc2(Location loc2) {
        loc2.setY(256);
        this.loc2 = loc2;
    }

    public boolean isLoc1Set() { return loc1 != null; }
}
