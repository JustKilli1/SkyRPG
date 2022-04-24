package net.marscraft.skyrpg.shared.inventory.invfunctions;

import net.marscraft.skyrpg.shared.inventory.IGuiInventory;
import net.marscraft.skyrpg.shared.logmanager.ILogManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InvFunctionGoBack {

    private final ILogManager logger;
    private List<IGuiInventory> previousGuiInv;

    public InvFunctionGoBack(ILogManager logger) {
        this.logger = logger;
        previousGuiInv = new ArrayList<>();
    }

    public @Nullable IGuiInventory getPreviousInventory() {
        if(previousGuiInv.size() == 0) return null;
        int lastIndex = previousGuiInv.size() - 1;
        IGuiInventory previousInventory = previousGuiInv.get(lastIndex);
        previousGuiInv.remove(lastIndex);
        return previousInventory;
    }

    public void addGuiInventory(IGuiInventory inv) {
        previousGuiInv.add(inv);
    }


}
