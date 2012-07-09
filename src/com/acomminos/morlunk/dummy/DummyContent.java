package com.acomminos.morlunk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;

import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftAccountFragment;
import com.acomminos.morlunk.account.minecraft.MorlunkMinecraftStoreFragment;

public class DummyContent {

    public static class DummyItem {

        public String id;
        public String content;
        public Class<? extends Fragment> fragmentClass;

        public DummyItem(String id, String content, Class<? extends Fragment> fragmentClass) {
            this.id = id;
            this.content = content;
            this.fragmentClass = fragmentClass;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
    	addItem(new DummyItem("0", "My Account", MorlunkMinecraftAccountFragment.class));
        addItem(new DummyItem("1", "Morlunk Co. Store", MorlunkMinecraftStoreFragment.class));
        addItem(new DummyItem("2", "Paoso Conversion Rates", null));
        addItem(new DummyItem("3", "Redeem Paoso Coupons", null));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
