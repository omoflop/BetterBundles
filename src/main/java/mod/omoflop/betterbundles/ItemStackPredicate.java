package mod.omoflop.betterbundles;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackPredicate implements Predicate<ItemStack> {

    private final ItemStack comparator;

    public ItemStackPredicate(ItemStack comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        return comparator.isItemEqual(itemStack);
    }
}
