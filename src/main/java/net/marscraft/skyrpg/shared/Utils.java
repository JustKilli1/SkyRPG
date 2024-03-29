package net.marscraft.skyrpg.shared;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Utils {



    /**
     * Gets Command from String[] args starting at: startingPoint
     * @param target Command
     * @param startIndex Index where to start in target Array
     * @return String from String[] target
     * */
    public static String getStrFromArray(String[] target, int startIndex) {
        String tmpStr = "";
        for(int i = startIndex; i < target.length; i++) {
            String str = target[i];
            if(i == startIndex) tmpStr = str;
            else tmpStr += " " + str;
        }
        return tmpStr;
    }

    /**
     * Converts ItemStack to String using Base64Coder
     * @param item ItemStack that gets converted to String
     * @return ItemStack as String
     * */
    public static @Nullable String itemStackToBase64(@Nullable ItemStack item) {
        if(item == null) return null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static double getRandomOffset() {
        double random = Math.random();
        if (Math.random() > 0.5) random *= -1;
        return random;
    }
    /**
     * Converts String to ItemStack
     * @param iStackAsStr ItemStack as String that gets Converted to ItemStack
     * @return ItemStack from String
     * */
    public static @Nullable ItemStack itemStackFromBase64(@Nullable String iStackAsStr) {
        if(iStackAsStr == null || iStackAsStr.equalsIgnoreCase("null")) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(iStackAsStr));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ItemStack removePersistantDataFromItem(ItemStack item, NamespacedKey key, PersistentDataType type) {
        if(item == null) return null;
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.getPersistentDataContainer().has(key, type)) itemMeta.getPersistentDataContainer().remove(key);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static int getItemIndexInInv(Inventory inv, ItemStack item) {
        ItemStack[] invContents = inv.getContents();

        for(int i = 0; i < invContents.length; i++) {
            ItemStack invItem = invContents[i];
            if(invItem.equals(item)) return i;
        }
        return -1;
    }

    public static int getItemIndexInInv(ItemStack[] invContents, ItemStack item) {
        for(int i = 0; i < invContents.length; i++) {
            ItemStack invItem = invContents[i];
            if(invItem == null) continue;
            if(invItem.equals(item)) return i;
        }
        return -1;
    }

    /**
     * Goes through given Inventory and checks if Inventory has enough space
     * @param inv Inventory that gets checked
     * @param neededSpace Space thats needed in Inventory
     * @return true if Inventory has enough space
     * */
    public static boolean enoughSpaceInInv(Inventory inv, int neededSpace) {
        ItemStack[] invContents = inv.getContents();
        for(int i = 0; i < invContents.length; i++) {
            if(neededSpace == 0) return true;
            ItemStack invContent = invContents[i];
            if(invContent == null || invContent.getType() == Material.AIR) neededSpace--;
        }
        return false;
    }

    /**
     * Gets Location From String
     * @param locationAsStr Location as String in Format:
     *                      <p>[worldName],[X],[Y],[Z],[Pitch],[Yaw]</p>
     * @return Location from String
     * */
    public static Location locationFromStr(String locationAsStr) {
        String[] locationSplit = locationAsStr.split(",");
        if(locationSplit.length != 6) return null;


        Location loc = new Location(
                                    Bukkit.getWorld(locationSplit[0]),
                                    doubleFromStr(locationSplit[1]),
                                    doubleFromStr(locationSplit[2]),
                                    doubleFromStr(locationSplit[3]),
                                    floatFromStr(locationSplit[4]),
                                    floatFromStr(locationSplit[5])
        );
        return loc;
    }

    /**
     * Converts Location To String
     * @param location Location that gets turned into a String
     * @return Location as String in Format:
     *         <p>[worldName],[X],[Y],[Z],[Pitch],[Yaw]</p>
     * */
    public static String locationToStr(Location location) {
        locationFromStr("");
        return location.getWorld().getName() + ","
               + location.getX() + ","
               + location.getY() + ","
               + location.getZ() + ","
               + location.getPitch() + ","
               + location.getYaw();
    }

    /**
     * Converts Given String to Double
     * @param doubleAsStr Double as String
     * @return Double From String. returns -1 if String can't be converted
     * */
    public static double doubleFromStr(String doubleAsStr) {
        try {
            return Double.parseDouble(doubleAsStr);
        } catch (Exception ex) {
            return -1;
        }
    }

    public static int intFromStr(String intAsStr) {
        try {
            return Integer.parseInt(intAsStr);
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Converts Given String to Float
     * @param floatAsStr Float as String
     * @return Float From String. returns -1f if String can't be converted
     * */
    public static float floatFromStr(String floatAsStr) {
        try {
            return Float.parseFloat(floatAsStr);
        } catch (Exception ex) {
            return -1f;
        }
    }

}
