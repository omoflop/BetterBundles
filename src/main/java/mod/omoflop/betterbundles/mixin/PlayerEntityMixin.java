package mod.omoflop.betterbundles.mixin;

import mod.omoflop.betterbundles.api.BetterBundle;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    @Shadow @Final private PlayerInventory inventory;

    @Shadow public abstract PlayerAbilities getAbilities();

    @Inject(method = "getArrowType", at = @At("HEAD"), cancellable = true)
    public void getArrowType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        Optional<BetterBundle> ammoBundle = BetterBundle.fetchFromInventory(inventory, BowItem.BOW_PROJECTILES);
        if (ammoBundle.isPresent()) {
            Optional<ItemStack> ammoItemStack = ammoBundle.get().getItem(BowItem.BOW_PROJECTILES);
            if (ammoItemStack.isPresent()) {
                cir.setReturnValue(ammoItemStack.get());
            }
        }
    }
}
