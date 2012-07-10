package com.acomminos.morlunk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.acomminos.morlunk.MorlunkPageFragment;
import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftAccountFragment;
import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftStoreFragment;

public class MinecraftContent {

    public static class MinecraftOption {

        public String id;
        public String content;
        public Class<? extends Fragment> fragmentClass;
        public Bundle arguments;

        public MinecraftOption(String id, String content, Class<? extends Fragment> fragmentClass, Bundle args) {
            this.id = id;
            this.content = content;
            this.fragmentClass = fragmentClass;
            this.arguments = args;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<MinecraftOption> ITEMS = new ArrayList<MinecraftOption>();
    public static Map<String, MinecraftOption> ITEM_MAP = new HashMap<String, MinecraftOption>();

    static {
    	addItem(new MinecraftOption("0", "My Account", MorlunkMinecraftAccountFragment.class, null));
        addItem(new MinecraftOption("1", "Morlunk Co. Store", MorlunkMinecraftStoreFragment.class, null));
        
        // INEXCUSABLE LAZINESS TODO FIX
        Bundle paosoPage = new Bundle();
        paosoPage.putString("pageName", "rates");
        addItem(new MinecraftOption("2", "Paoso Conversion Rates", MorlunkPageFragment.class, paosoPage));
        
        addItem(new MinecraftOption("3", "Redeem Paoso Coupons", null, null));
        addItem(new MinecraftOption("4", "Buy Paosos", null, null));
    }

    private static void addItem(MinecraftOption item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
