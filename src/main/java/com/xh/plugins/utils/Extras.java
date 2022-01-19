package com.xh.plugins.utils;

import com.eu.habbo.Emulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Extras {

    public static void load(){
        registerTexts();
        checkDatabase();
        registerDatabaseForIgnoreSM();
    }

    private static void registerTexts() {
        Emulator.getTexts().register("commands.keys.cmd_ignoreSM","ignore_sm");
        Emulator.getTexts().register("commands.description.cmd_ignoreSM",":ignore_sm - Ozel mesajlarin yollanmasini engeller.");

        Emulator.getTexts().register("commands.keys.cmd_editPrefixSM","editprefix");
        Emulator.getTexts().register("commands.description.cmd_editPrefixSM",":editprefix (new-prefix) - Updating the prefix we use when sending private messages");

        Emulator.getTexts().register("commands.cmd_ignoreSM.successIgnore","Bir daha sana ozel mesaj yollayamayacaklar !");
        Emulator.getTexts().register("commands.cmd_ignoreSM.cancelIgnore","Artik sana ozel mesaj yollayabilecekler !");
        Emulator.getTexts().register("commands.cmd_ignoreSM.targetIgnoredSM","%target% ozel mesajlari yasaklamis, mesaj gonderilemedi.");

        Emulator.getTexts().register("commands.cmd_editPrefixSM.notNew","Prefix guncellerken eski prefixinizi giremezsiniz.");
        Emulator.getTexts().register("commands.cmd_editPrefixSM.updatedPrefix","Prefix guncellendi. Yeni prefix : %newPrefix%");
        Emulator.getTexts().register("commands.cmd_editPrefixSM.failUpdatePrefix","Prefix guncellenemedi. Prefinix : %prefix%");

        Emulator.getTexts().register("commands.SM.wrongUsing","Komutu yanlis kullandiniz. Kullanim = :sm [@player-name] [message]. Mesaj zorunlu degil.");
        Emulator.getTexts().register("commands.SM.notFoundUser","Kullanici bulunamadi yada kullanici ismini dogru girmediniz. Ornek = %user% bla bla");
        Emulator.getTexts().register("commands.SM.notSelfSM","Kendine mesaj yollayamazsin !");
        Emulator.getTexts().register("commands.SM.lowCredits","Ozel mesaj yollayabilmek icin %credits% krediye sahip olmalisiniz.");

        Emulator.getTexts().register("commands.SM.message", "%client% dedi ki: %message%");
        Emulator.getTexts().register("commands.SM.look","https://cdn.leet.vc/imaging/avatarimage?figure=%look%&gesture=sml");

        Emulator.getConfig().register("specialMessage.creditsValue","5000");
        Emulator.getConfig().register("specialMessage.creditsOption","1");
        Emulator.getConfig().register("specialMessage.customizablePrefix","@");
    }

    private static boolean registerPermission(String name, String options, String defaultValue, boolean defaultReturn)
    {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement("ALTER TABLE  `permissions` ADD  `" + name +"` ENUM(  " + options + " ) NOT NULL DEFAULT  '" + defaultValue + "'"))
            {
                statement.execute();
                return true;
            }
        }
        catch (SQLException e)
        {}

        return defaultReturn;
    }

    public static void checkDatabase() {
        boolean reloadPermissions = false;
        reloadPermissions = registerPermission("cmd_ignoreSM", "'0', '1', '2'", "1", reloadPermissions);
        reloadPermissions = registerPermission("cmd_editPrefixSM", "'0', '1', '2'", "1", reloadPermissions);
        if (reloadPermissions)
        {
            Emulator.getGameEnvironment().getPermissionsManager().reload();
        }
    }

    private static void registerDatabaseForIgnoreSM(){
        try(Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement stmt = connection.prepareStatement("ALTER TABLE users_settings ADD COLUMN IF NOT EXISTS ignore_specialMessage ENUM(\"0\",\"1\") DEFAULT \"0\" AFTER max_friends")){
            stmt.execute();
        } catch (SQLException e){
            System.out.println(e);
        }
    }


}
