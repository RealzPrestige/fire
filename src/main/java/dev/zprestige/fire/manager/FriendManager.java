package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;

public class FriendManager {
    protected final ArrayList<FriendPlayer> friendList = new ArrayList<>();
    public void addFriend(String name) {
        if (!isFriend(name)) {
            friendList.add(new FriendPlayer(name));
        }
    }
    public void removeFriend(String name) {
        friendList.removeIf(player -> player.getName().equals(name));
    }

    public ArrayList<FriendPlayer> getFriendList() {
        return friendList;
    }

    public boolean isFriend(String name) {
        return friendList.stream().anyMatch(player -> player.getName().equals(name));
    }

    public void saveFriends(){
        final File file = Main.fileManager.registerFileAndCreate(Main.configManager.getConfigFolder() + Main.configManager.getSeparator() + "Friends.txt");
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(file);
        friendList.forEach(friendPlayer -> Main.fileManager.writeLine(bufferedWriter, "\"" + friendPlayer.getName() + "\""));
        Main.fileManager.closeBufferedWriter(bufferedWriter);
    }

    public static class FriendPlayer {
        protected final String name;

        public FriendPlayer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}