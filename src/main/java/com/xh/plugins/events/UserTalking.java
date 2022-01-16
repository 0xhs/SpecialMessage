package com.xh.plugins.events;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.events.users.UserTalkEvent;
import gnu.trove.map.hash.THashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserTalking implements EventListener {

    @EventHandler
    public static void onUserTalkEvent(UserTalkEvent chatEvent){
        if(chatEvent.chatMessage.getMessage().startsWith("@")){
            Habbo client   = chatEvent.chatMessage.getHabbo();
            String message = chatEvent.chatMessage.getMessage();
            String[] parts = message.split(" ");
            String targetUser = parts[0].substring(1);
            Habbo target = Emulator.getGameEnvironment().getHabboManager().getHabbo(targetUser);
            if(Integer.parseInt(Emulator.getConfig().getValue("specialMessage.creditsOption")) == 1){
                if(client.getHabboInfo().getCredits() >= Integer.parseInt(Emulator.getConfig().getValue("specialMessage.creditsValue"))){
                    if(findUser(target)){
                        processOK(client,target,chatEvent);
                    } else {
                        client.whisper(Emulator.getTexts().getValue("commands.SM.notFoundUser").replace("%user%","@" + client.getHabboInfo().getUsername()));
                        chatEvent.setCancelled(true);
                    }
                } else {
                    client.whisper(Emulator.getTexts().getValue("commands.SM.lowCredits").replace("%credits%",Emulator.getConfig().getValue("specialMessage.creditsValue")));
                    chatEvent.setCancelled(true);
                }
            } else {
                if(findUser(target)){
                    processOK(client,target,chatEvent);
                } else {
                    client.whisper(Emulator.getTexts().getValue("commands.SM.notFoundUser").replace("%user%","@" + client.getHabboInfo().getUsername()));
                    chatEvent.setCancelled(true);
                }
            }
        }
    }

    private static boolean findUser(Habbo target){
        return target != null && target.isOnline();
    }

    private static void processOK(Habbo client, Habbo target, UserTalkEvent event) {
        if (client.getHabboInfo().getUsername().equals(target.getHabboInfo().getUsername())) {
            client.whisper(Emulator.getTexts().getValue("commands.SM.notSelfSM"));
            event.setCancelled(true);
        } else {
                if(didntIgnoreTargetMessage(target)){
                    THashMap<String, String> specialM = new THashMap();
                    specialM.put("display", "BUBBLE");
                    specialM.put("image", Emulator.getTexts().getValue("commands.SM.look").replace("%look%", client.getHabboInfo().getLook()));
                    Room room = client.getHabboInfo().getCurrentRoom();
                    if (room != null) {
                        specialM.put("linkUrl", "event:navigator/goto/" + client.getHabboInfo().getCurrentRoom().getId());
                    }

                    specialM.put("message", Emulator.getTexts().getValue("commands.SM.message").replace("%message%", event.chatMessage.getMessage()).replace("%client%", client.getHabboInfo().getUsername()));
                    target.getClient().sendResponse(new BubbleAlertComposer("special.message", specialM));
                    client.giveCredits(-Integer.parseInt(Emulator.getConfig().getValue("specialMessage.creditsValue")));
                    event.setCancelled(true);
                } else {
                    client.whisper(Emulator.getTexts().getValue("commands.cmd_ignoreSM.targetIgnoredSM").replace("%target%",target.getHabboInfo().getUsername()));
                    event.setCancelled(true);
                }
        }
    }

    public static boolean didntIgnoreTargetMessage(Habbo target){
        try(Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement getIgnoreData = connection.prepareStatement("SELECT ignore_specialMessage FROM users_settings WHERE user_id = ?")){
            getIgnoreData.setInt(1,target.getHabboInfo().getId());
            ResultSet rs = getIgnoreData.executeQuery();
            while(rs.next()){
                byte ignoreData = rs.getByte("ignore_specialMessage");
                if(ignoreData == 0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
