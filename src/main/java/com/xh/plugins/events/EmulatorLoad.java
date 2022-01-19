package com.xh.plugins.events;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.CommandHandler;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;
import com.xh.plugins.cmd.CustomizablePrefixCommand;
import com.xh.plugins.cmd.IgnoreSpecialMessage;
import com.xh.plugins.utils.Extras;

public class EmulatorLoad implements EventListener {

    @EventHandler
    public static void onEmu(EmulatorLoadedEvent e){
        Extras.load();

        CommandHandler.addCommand(new CustomizablePrefixCommand("cmd_editPrefixSM",Emulator.getTexts().getValue("commands.keys.cmd_editPrefixSM").split(";")));
        CommandHandler.addCommand(new IgnoreSpecialMessage("cmd_ignoreSM",Emulator.getTexts().getValue("commands.keys.cmd_ignoreSM").split(";")));
    }

}
