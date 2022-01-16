package com.xh.plugins;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.HabboPlugin;
import com.xh.plugins.events.EmulatorLoad;
import com.xh.plugins.events.UserTalking;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class EmulatorEngine extends HabboPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger(EmulatorEngine.class);

    // Printing colorize
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public void onEnable() throws Exception {
        Emulator.getPluginManager().registerEvents(this,new EmulatorLoad());
        Emulator.getPluginManager().registerEvents(this,new UserTalking());
        LOGGER.info(ANSI_GREEN + "[SM] Special Message plugin has activated" + ANSI_RESET);
    }

    @Override
    public void onDisable() throws Exception {
        LOGGER.error(ANSI_RED + "[SM] Private Message plugin turned off" + ANSI_RESET);
    }

    @Override
    public boolean hasPermission(Habbo habbo, String s) {
        return false;
    }
}
