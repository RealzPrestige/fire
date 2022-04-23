package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", "Friend <Add/Del> <Name>");
    }

    @Override
    public void listener(String string) {
        try {
            final String[] split = string.split(" ");
            final String split1 = split[1];
            final String split2 = split[2];
            if (split1.equalsIgnoreCase("add")) {
                Main.friendManager.addFriend(split2);
                completeMessage("added " + split2 + " to your friends list");
                return;
            }
            if (split1.equalsIgnoreCase("del")) {
                Main.friendManager.removeFriend(split2);
                completeMessage("removed " + split2 + " from your friends list");
            }
        } catch (Exception ignored) {
            throwException(format);
        }
    }
}
