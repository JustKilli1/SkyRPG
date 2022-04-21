package net.marscraft.skyrpg.module.regions.region;

import net.marscraft.skyrpg.shared.logmanager.ILogManager;

public class Region {

    private ILogManager logger;
    private int id;
    private String name;
    private Bound bound;

    public Region(ILogManager logger, String name, Bound bound) {
        this.logger = logger;
        this.name = name;
        this.bound = bound;
    }

    public Region(ILogManager logger, int id, String name, Bound bound) {
        this.logger = logger;
        this.id = id;
        this.name = name;
        this.bound = bound;
    }

    public boolean setupComplete() {
        if(id == 0) return false;
        if(name == null || name.length() == 0) return false;
        return bound.setupComplete();
    }

    public Bound getBound() {
        return bound;
    }
    public void setBound(Bound bound) { this.bound = bound; }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }
}
