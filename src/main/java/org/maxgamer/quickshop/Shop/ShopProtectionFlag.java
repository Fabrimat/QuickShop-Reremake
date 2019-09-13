package org.maxgamer.quickshop.Shop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ShopProtectionFlag {
    private String itemStackString;
    private String mark = "QuickShop DisplayItem";
    private String shopLocation;

    public ShopProtectionFlag(String shopLocation, String itemStackString) {
        this.shopLocation = shopLocation;
        this.itemStackString = itemStackString;
    }
    public static String getDefaultMark(){
        return "QuickShop DisplayItem";
    }
}
