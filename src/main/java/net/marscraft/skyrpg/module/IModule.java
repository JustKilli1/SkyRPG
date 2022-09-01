package net.marscraft.skyrpg.module;

import net.marscraft.skyrpg.shared.events.EventStorage;

public interface IModule {

    void onModuleEnable();
    void onModuleDisable();

    /**
     * Module Reload logic
     * */
    void reloadModule();

    /**
     * Handles ListenerCalls. Event gets Stored in Object EventStorage storage
     * @param storage Object with calling Event
     * */
    void onListenerCall(EventStorage storage);

    ModuleState getModuleState();
    void updateModuleState(ModuleState moduleState);

    ModuleMode getModuleMode();
    void updateModuleMode(ModuleMode moduleMode);

    String getModuleName();
    String getModuleDescription();

}
