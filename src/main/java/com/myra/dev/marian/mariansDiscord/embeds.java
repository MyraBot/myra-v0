package com.myra.dev.marian.mariansDiscord;

import net.dv8tion.jda.api.EmbedBuilder;

public class embeds {

    //W E L C O M E
    public EmbedBuilder welcome(String guildName, String guildImage) {
        EmbedBuilder welcome = new EmbedBuilder();
        welcome.setTitle("welcome to " + guildName);
        welcome.setThumbnail(guildImage);
        welcome.setColor(0x00FFFF);
        welcome.addField("", guildName + " is a server dedicated to have fun and write with other people", false);
        welcome.addField("", "Underneath you can find all information you will need to get you started in our server! If you still have questions after reading this, just contact one of our staff members! Also check out <#696420618995761192> in case something changes.", false);
        welcome.addField("", "**\uD83D\uDD17 invite link : https://discord.gg/nG4uKuB**", false);

        return welcome;
    }
    //R O L E S

    //partner
    public EmbedBuilder partner() {
        EmbedBuilder partner = new EmbedBuilder();
        partner.setTitle("Partner");
        partner.setColor(0xB200FF);
        partner.addField("REQUIREMENTS FOR PARTNER \n •at least 100 members \n •international server \n •a nice environment \n •organized chat and role system \n •use my bot :)", "<@&709350588436971620>", true);
        partner.setFooter("We don't support racist or sexist servers. Even if you meet all the conditions, your application can still be rejected. If you are interested, please write a direct message to Marian.", "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/twitter/248/construction_1f6a7.png");

        return partner;
    }

    //buy able roles
    public EmbedBuilder colour() {
        EmbedBuilder buy = new EmbedBuilder();
        buy.setTitle("buyable roles");
        buy.setColor(0xFFD800);
        buy.setDescription("You can buy these roles by typing in commands `..buy`");
        buy.addField("", "25 000 \n <@&715462771688603678>  <@&715508379807645697>" +
                "\n15 000 \n  <@&715462773512863804> <@&715498605162791042> <@&715500070610534480>" +
                "\n15 000 \n  <@&715462773512863804> <@&715498605162791042> <@&715500070610534480>" +
                "\n10 000 \n  <@&715462773525708832> <@&715462772615413811> <@&715462649390956567>" +
                "\n5 000 \n  <@&715498892900302929>", false);
        buy.addField("", "10 000 \n <@&714787219784597544> ➪ BE ABLE TO ADVERTISE IN  <#668403563223056384>" +
                "\n1 000 000 \n <@&732929835814617099> ➪ secret :D", false);

        return buy;
    }

    //leveling roles
    public EmbedBuilder leveling() {
        EmbedBuilder leveling = new EmbedBuilder();
        leveling.setTitle("leveling roles");
        leveling.setColor(0xFF006E);
        leveling.setDescription("You can get these roles if you reach a specific level.");
        leveling.addField("", "level 5 ➪ <@&688477543594197044>", true);
        leveling.addField("", "level 15 ➪ <@&688477562313244763>", true);
        leveling.addField("", "level 25 ➪ <@&688480420685545527>", true);
        leveling.addField("", "level 50 ➪ <@&688480479371985031>", true);
        leveling.addField("", "level 75 ➪ <@&688480479371985031>", true);
        leveling.addField("", "level 100 ➪ <@&689878622361747494>", true);

        return leveling;
    }

    //designer roles
    public EmbedBuilder designer() {
        EmbedBuilder designer = new EmbedBuilder();
        designer.setTitle("designer roles");
        designer.setColor(0x00FF90);
        designer.setDescription("These roles you can get by reaching **desinger rank** of a specific kind");
        designer.addField("IF YOU REACHED THE **DESIGNER** RANK WITH MINECRAFT DESINGS", "<@&698884619130634292>", false);
        designer.addField("IF YOU REACHED THE **DESIGNER** RANK WITH INTROS", "<@&711972287330648155>", false);
        designer.addField("IF YOU REACHED THE **DESIGNER** RANK WITH ANIME DESIGNS", "<@&711972209580572732>", false);
        designer.addField("WHEN YOU REACHED THE **DESIGNER** RANK WITH TEXTURE PACKS", "<@&698884621110083717>", false);
        designer.addField("", "<@&698884600654594108> \n <@&698884548783636500> \n <@&698884462460534835> \n <@&698884227059417128> \n <@&698883418070450177> \n <@&698883364773429258> \n <@&698883381966143568>", false);

        return designer;
    }
    //O T H E R

    //my social Media
    public EmbedBuilder socialMedia() {
        EmbedBuilder socialMedia = new EmbedBuilder();
        socialMedia.setTitle("my social Media");
        socialMedia.setColor(0x0094FF);
        socialMedia.addField("\uD83D\uDD17 │ YouTube", "[texture packs releases](https://www.youtube.com/channel/UCw4EmB5OUHFN5RplLLon_Xw? \"ᴍᴀʀɪᴀɴ ⚡ on YouTube\")", false);
        socialMedia.addField("\uD83D\uDD17 │ Instagram", "[edits](https://www.instagram.com/mar._.ian/ \"ᴍᴀʀɪᴀɴ ⚡ on Instagram\")", false);
        socialMedia.addField("\uD83D\uDD17 │ Twitter", "[idk lol](https://twitter.com/MarianGFX \"ᴍᴀʀɪᴀɴ ⚡ on Twitter\")", true);
        socialMedia.addField("\uD83D\uDD17 │ Discord", "[join my discord server](https://discord.gg/nG4uKuB \"Marians discord server\")", false);

        return socialMedia;
    }
    //R E A C T I O N  R O L E S

    //bot related channels
    public EmbedBuilder botChannels() {
        EmbedBuilder botChannels = new EmbedBuilder();
        botChannels.setTitle("bot related channels");
        botChannels.setColor(0xE9E637);
        botChannels.setDescription("React with [\uD83D\uDD0C] to see the bot related channels. You will see updates, bugs and planned features for the bot!");

        return botChannels;
    }

    //texture pack related channels
    public EmbedBuilder packChannels() {
        EmbedBuilder packChannels = new EmbedBuilder();
        packChannels.setTitle("texture pack related channels");
        packChannels.setColor(0x40DAAB);
        packChannels.setDescription("React with [\u270F\uFE0F] to see the texture pack related channels. You will see sneak peaks and my releases!");

        return packChannels;
    }
}
