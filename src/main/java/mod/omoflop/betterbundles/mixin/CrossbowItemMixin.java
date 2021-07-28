package mod.omoflop.betterbundles.mixin;

import mod.omoflop.betterbundles.api.BetterBundle;
import mod.omoflop.betterbundles.ItemStackPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "loadProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"))
    private static void loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative, CallbackInfoReturnable<Boolean> cir) {
        if (shooter.isPlayer()) {
            PlayerEntity player = (PlayerEntity) shooter;

            Predicate<ItemStack> ammoPredicate = new ItemStackPredicate(projectile);
            Optional<BetterBundle> ammoBundle = BetterBundle.fetchFromInventory(player.getInventory(), ammoPredicate);
            if (ammoBundle.isPresent()) {
                if (!(creative)) {
                    ammoBundle.get().decrementItem(1, ammoPredicate);
                }
            }
        }
    }


}
