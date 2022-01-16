package com.xh.plugins.cmd;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.users.Habbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IgnoreSpecialMessage extends Command {

    Habbo client;
    
    public IgnoreSpecialMessage(String permission, String[] keys) {
        super(permission, keys);
    }

    @Override
    public boolean handle(GameClient gc, String[] s) throws SQLException {
        this.ignoreSpecialMessage(gc.getHabbo());
        return true;
    }

    protected void ignoreSpecialMessage(Habbo client) throws SQLException {
        try(Connection connection = Emulator.getDatabase().getDataSource().getConnection()){
            PreparedStatement getIgnoreData = connection.prepareStatement("SELECT * FROM users_settings WHERE user_id = ?");
            getIgnoreData.setInt(1,client.getHabboInfo().getId());
            ResultSet rs = getIgnoreData.executeQuery();
            while(rs.next()){
                int ignoreData = rs.getInt("ignore_specialMessage");
                if(ignoreData == 0){
                    PreparedStatement updateIgnoreData = connection.prepareStatement("UPDATE users_settings SET ignore_specialMessage = ? WHERE user_id = ?");
                    updateIgnoreData.setString(1,"1");
                    updateIgnoreData.setInt(2,client.getHabboInfo().getId());
                    updateIgnoreData.executeUpdate();
                    client.whisper(Emulator.getTexts().getValue("commands.cmd_ignoreSM.successIgnore"));
                } else {
                    PreparedStatement updateIgnoreData = connection.prepareStatement("UPDATE users_settings SET ignore_specialMessage = ? WHERE user_id = ?");
                    updateIgnoreData.setString(1,"0");
                    updateIgnoreData.setInt(2,client.getHabboInfo().getId());
                    updateIgnoreData.executeUpdate();
                    client.whisper(Emulator.getTexts().getValue("commands.cmd_ignoreSM.cancelIgnore"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
