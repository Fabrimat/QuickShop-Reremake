package org.maxgamer.quickshop.Util;

import java.util.List;
import java.util.Map;

import lombok.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.maxgamer.quickshop.QuickShop;

@AllArgsConstructor
public class ItemMatcher {
    private QuickShop plugin;
    private final ItemMetaMatcher itemMetaMatcher = new ItemMetaMatcher();

    /**
     * Compares two items to each other. Returns true if they match.
     * Rewrite it to use more faster hashCode.
     *
     * @param stack1 The first item stack
     * @param stack2 The second item stack
     * @return true if the itemstacks match. (Material, durability, enchants, name)
     */
    public boolean matches(ItemStack stack1, ItemStack stack2) {

        if (plugin.getConfig().getBoolean("shop.strict-matches-check"))
            if (stack1.equals(stack2))
                return false;

        if (stack1 == stack2)
            return true; // Referring to the same thing, or both are null.

        if (stack1 == null || stack2 == null)
            return false; // One of them is null (Can't be both, see above)

        if (stack1.hashCode() == stack2.hashCode())
            return true;

        if (!typeMatches(stack1, stack2)) {
            Util.debugLog("Type not matches.");
            return false;
        }

        if (stack1.hasItemMeta()) {
            if (!itemMetaMatcher.matches(stack1, stack2))
                return false;
            // /** DisplayName check **/
            // if (stack1Meta.hasDisplayName() != stack2Meta.hasDisplayName())
            //     return false; //Has displayName check
            // if (stack1Meta.hasDisplayName()) {
            //     String stack1DisplayName = stack1Meta.getDisplayName();
            //     String stack2DisplayName = stack2Meta.getDisplayName();
            //     if (!stack1DisplayName.equals(stack2DisplayName))
            //         return false; //DisplayName check;
            // }
            // /** Lores check **/
            // if (stack1Meta.hasLore() != stack2Meta.hasLore())
            //     return false;
            // if (stack1Meta.hasLore()) {
            //     if (stack1Meta.getLore().hashCode() != stack2Meta.getLore().hashCode())
            //         return false;
            // }
            // /** Enchants check **/
            // if (stack1Meta.hasEnchants() != stack2Meta.hasEnchants())
            //     return false;
            // if (stack1Meta.hasEnchants()) {
            //     Map<Enchantment, Integer> stack1Ench = stack1Meta.getEnchants();
            //     Map<Enchantment, Integer> stack2Ench = stack2Meta.getEnchants();
            //     if (stack1Ench.hashCode() != stack2Ench.hashCode())
            //         return false;
            // }
            // /** Damage check **/
            // if (stack1Meta instanceof Damageable != stack2Meta instanceof Damageable)
            //     return false;
            // if (stack1Meta instanceof Damageable) {
            //     Damageable stack1Damage = (Damageable) stack1Meta;
            //     Damageable stack2Damage = (Damageable) stack2Meta;
            //     if (stack1Damage.hashCode() != stack2Damage.hashCode())
            //         return false;
            // }
            // /** Potion check **/
            // if (stack1Meta instanceof PotionMeta != stack2Meta instanceof Damageable)
            //     return false;
            // if (stack1Meta instanceof PotionMeta) {
            //     PotionMeta stack1Potion = (PotionMeta) stack1Meta;
            //     PotionMeta stack2Potion = (PotionMeta) stack2Meta;
            //     if (stack1Potion.hashCode() != stack2Potion.hashCode())
            //         return false;
            //
            // }
        }
        return true;
    }

    private boolean typeMatches(ItemStack stack1, ItemStack stack2) {
        return (stack1.getType().equals(stack2.getType()));
    }

    class ItemMetaMatcher {
        boolean matches(ItemStack stack1, ItemStack stack2) {
            if (stack1.hasItemMeta() != stack2.hasItemMeta())
                return false;
            if (!stack1.hasItemMeta())
                return true; //Passed check. no meta need to check.
            ItemMeta meta1 = stack1.getItemMeta();
            ItemMeta meta2 = stack2.getItemMeta();
            if ((meta1 == null) != (meta2 == null))
                return false;
            if (meta1 == null)
                return true; //Both null...
            if (!rootMatches(meta1, meta2)) //Directly check itemMeta.
                return false;
            if (!damageMatches(meta1, meta2)) //Directly check itemMeta.
                return false;
            if (!displayMatches(meta1, meta2))
                return false;
            if (!loresMatches(meta1, meta2))
                return false;
            if (!enchMatches(meta1, meta2))
                return false;
            if (!attributeModifiersMatches(meta1, meta2))
                return false;
            if (!itemFlagsMatches(meta1, meta2))
                return false;
            if (!customModelDataMatches(meta1, meta2))
                return false;
            return true;
        }

        private boolean rootMatches(ItemMeta meta1, ItemMeta meta2) {
            return (meta1.equals(meta2));
        }

        private boolean damageMatches(ItemMeta meta1, ItemMeta meta2) {
            if ((meta1 instanceof Damageable) != (meta2 instanceof Damageable))
                return false;

            if (!(meta1 instanceof Damageable))
                return false; //No damage need to check.

            Damageable damage1 = (Damageable) meta1;
            Damageable damage2 = (Damageable) meta2;

            if (damage1.hasDamage() != damage2.hasDamage())
                return false;

            if (!damage1.hasDamage())
                return true; //No damage need to check.

            return (damage1.getDamage() == damage2.getDamage());

        }

        private boolean displayMatches(ItemMeta meta1, ItemMeta meta2) {
            if (meta1.hasDisplayName() != meta2.hasDisplayName())
                return false;

            if (!meta1.hasDisplayName())
                return true; //Passed check. no display need to check

            return (meta1.getDisplayName().equals(meta2.getDisplayName()));
        }

        private boolean loresMatches(ItemMeta meta1, ItemMeta meta2) {
            if (meta1.hasLore() != meta2.hasLore())
                return false;

            if (!meta1.hasLore())
                return true; // No lores need to check.

            List<String> lores1 = meta1.getLore();
            List<String> lores2 = meta2.getLore();

            if (lores1.size() != lores2.size())
                return false;

            return (lores2.hashCode() == lores2.hashCode());
        }

        private boolean enchMatches(ItemMeta meta1, ItemMeta meta2) {
            if (meta1.hasEnchants() != meta2.hasEnchants())
                return false;

            if (!meta1.hasEnchants())
                return true; //No enchs need to check

            Map<Enchantment, Integer> enchMap1 = meta1.getEnchants();
            Map<Enchantment, Integer> enchMap2 = meta2.getEnchants();

            return (enchMap1.hashCode() == enchMap2.hashCode());
        }

        private boolean potionMatches(ItemMeta meta1, ItemMeta meta2) {
            if ((meta1 instanceof PotionMeta) != (meta2 instanceof PotionMeta))
                return false;

            if (!(meta1 instanceof PotionMeta))
                return true; //No potion meta need to check.

            PotionMeta potion1 = (PotionMeta) meta1;
            PotionMeta potion2 = (PotionMeta) meta2;

            if (potion1.hasColor() != potion2.hasColor())
                return false;

            if (!potion1.getColor().equals(potion2.getColor()))
                return false;

            if (potion1.hasCustomEffects() != potion2.hasCustomEffects())
                return false;

            if (potion1.getCustomEffects().hashCode() != potion2.getCustomEffects().hashCode())
                return false;

            if (potion1.getBasePotionData().hashCode() != potion2.getBasePotionData().hashCode())
                return false;

            return true;
        }

        private boolean attributeModifiersMatches(ItemMeta meta1, ItemMeta meta2) {
            if (meta1.hasAttributeModifiers() != meta2.hasAttributeModifiers())
                return false;

            if (!meta1.hasAttributeModifiers())
                return true; //No attributeModifiers need to check

            return (meta1.getAttributeModifiers().hashCode() == meta2.getAttributeModifiers().hashCode());
        }

        private boolean itemFlagsMatches(ItemMeta meta1, ItemMeta meta2) {
            return (meta1.getItemFlags().hashCode() == meta2.getItemFlags().hashCode());
        }

        private boolean customModelDataMatches(ItemMeta meta1, ItemMeta meta2) {
            if (meta1.hasCustomModelData() != meta2.hasCustomModelData())
                return false;
            if (!meta1.hasCustomModelData())
                return true; //No customModelData need to check.
            return (meta1.getCustomModelData() == meta2.getCustomModelData());
        }

    }
}