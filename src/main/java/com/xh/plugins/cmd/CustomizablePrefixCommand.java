package com.xh.plugins.cmd;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.events.emulator.EmulatorConfigUpdatedEvent;

public class CustomizablePrefixCommand extends Command {
    public CustomizablePrefixCommand(String permission, String[] keys) {
        super(permission, keys);
    }

    @Override
    public boolean handle(GameClient gc, String[] s) {

        if(s.length == 2){
            Habbo c = gc.getHabbo();
            String old_prefix = Emulator.getConfig().getValue("specialMessage.customizablePrefix");
            String new_prefix = s[1];

            if(new_prefix.equals(old_prefix)){
                c.whisper(Emulator.getTexts().getValue("commands.cmd_editPrefixSM.notNew"));
            } else {
                c.whisper(Emulator.getTexts().getValue("commands.cmd_editPrefixSM.updatedPrefix").replace("%newPrefix%",new_prefix));
                try {
                    Emulator.getConfig().update("specialMessage.customizablePrefix", new_prefix);
                    Emulator.getConfig().saveToDatabase();
                    Emulator.getPluginManager().fireEvent(new EmulatorConfigUpdatedEvent());
                    Emulator.getConfig().reload();
                } catch (Exception e) {
                    c.whisper(Emulator.getTexts().getValue("commands.cmd_editPrefixSM.failUpdatePrefix").replace("%prefix%",old_prefix));
                }
            }


        }

        return true;
    }
}
