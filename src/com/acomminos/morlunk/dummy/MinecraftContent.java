package com.acomminos.morlunk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;

import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftAccountFragment;
import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftStoreFragment;

public class MinecraftContent {

    public static class MinecraftItem {

        public String id;
        public String content;
        public Class<? extends Fragment> fragmentClass;

        public MinecraftItem(String id, String content, Class<? extends Fragment> fragmentClass) {
            this.id = id;
            this.content = content;
            this.fragmentClass = fragmentClass;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<MinecraftItem> ITEMS = new ArrayList<MinecraftItem>();
    public static Map<String, MinecraftItem> ITEM_MAP = new HashMap<String, MinecraftItem>();

    static {
    	addItem(new MinecraftItem("0", "My Account", MorlunkMinecraftAccountFragment.class));
        addItem(new MinecraftItem("1", "Morlunk Co. Store", MorlunkMinecraftStoreFragment.class));
        addItem(new MinecraftItem("2", "Paoso Conversion Rates", null));
        addItem(new MinecraftItem("3", "Redeem Paoso Coupons", null));
        addItem(new MinecraftItem("4", "Buy Paosos", null));
    }

    private static void addItem(MinecraftItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
