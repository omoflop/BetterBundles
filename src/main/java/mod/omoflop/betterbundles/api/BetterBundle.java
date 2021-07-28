package mod.omoflop.betterbundles.api;

import mod.omoflop.betterbundles.BetterBundlesMod;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class BetterBundle {

    public ItemStack stack;
    public BetterBundle(ItemStack stack) {
        this.stack = stack;
    }

    public static boolean isBundle(ItemStack stack) {
        return isBundle(stack.getItem());
    }
    public static boolean isBundle(Item item) {
        return BetterBundlesMod.BUNDLE_TAG.contains(item);
    }

    public static Optional<BetterBundle> fetchFromInventory(Inventory inv, @Nullable Predicate<ItemStack> predicate) {
        for (int i = 0; i < inv.size(); i++) {
            BetterBundle bundle = new BetterBundle(inv.getStack(i));
            if (bundle.isBundle()) {
                if (bundle.stack.getNbt() != null) {
                    if (predicate != null) {
                        Optional<ItemStack> bundledItem = bundle.getItem(predicate);
                        if (bundledItem.isPresent()) {
                            return Optional.of(bundle);
                        }
                    } else {
                        return Optional.of(bundle);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Optional<ItemStack> getItem(Predicate<ItemStack> condition) {
        NbtCompound nbtCompound = this.stack.getOrCreateNbt();
        if (nbtCompound.contains("Items")) {
            NbtList nbtList = nbtCompound.getList("Items", 10);
            if (!nbtList.isEmpty()) {
                for (int i = 0; i < nbtList.size(); i++) {
                    NbtCompound itemNBTEntry = nbtList.getCompound(i);
                    ItemStack curBundleItem = ItemStack.fromNbt(itemNBTEntry);
                    if (condition.test(curBundleItem)) return Optional.of(curBundleItem);
                }
            }
        }
        return Optional.empty();
    }

    public boolean decrementItem(int amount, Predicate<ItemStack> itemToDecrement) {
        NbtCompound nbtCompound = this.stack.getOrCreateNbt();
        if (!nbtCompound.contains("Items")) {
            return false;
        } else {
            NbtList nbtList = nbtCompound.getList("Items", 10);
            if (!nbtList.isEmpty()) {
                for (int i = 0; i < nbtList.size(); i++) {
                    NbtCompound itemNBTEntry = nbtList.getCompound(i);
                    ItemStack curBundleItem = ItemStack.fromNbt(itemNBTEntry);
                    if (itemToDecrement.test(curBundleItem)) {
                        int underflow = curBundleItem.getCount() - amount;
                        if (underflow < 0) {
                            return false;
                        } else if (underflow == 0) {
                            nbtList.remove(i);
                        } else {
                            NbtCompound modifiedStack = itemNBTEntry.copy();
                            modifiedStack.putByte("Count", (byte) underflow);
                            nbtList.set(i, modifiedStack);
                        }

                        if (nbtList.isEmpty()) {
                            this.stack.removeSubNbt("Items");
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean isBundle() {
        return isBundle(this.stack);
    }

}