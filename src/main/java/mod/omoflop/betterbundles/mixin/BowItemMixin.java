package mod.omoflop.betterbundles.mixin;
;
import mod.omoflop.betterbundles.api.BetterBundle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack bowItem = user.getStackInHand(hand);
        Optional<BetterBundle> ammoBundle = BetterBundle.fetchFromInventory(user.getInventory(), BowItem.BOW_PROJECTILES);
        if (ammoBundle.isPresent()) {
            Optional<ItemStack> ammoStack = ammoBundle.get().getItem(BowItem.BOW_PROJECTILES);
            if (ammoStack.isPresent()) {
                user.setCurrentHand(hand);
                cir.setReturnValue(TypedActionResult.consume(bowItem));
            }

        }
    }

    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user.isPlayer()) {
            PlayerEntity player = (PlayerEntity) user;

            Optional<BetterBundle> ammoBundle = BetterBundle.fetchFromInventory(player.getInventory(), BowItem.BOW_PROJECTILES);
            if (ammoBundle.isPresent()) {
                Optional<ItemStack> ammoItemStack = ammoBundle.get().getItem(BowItem.BOW_PROJECTILES);
                if (ammoItemStack.isPresent()) {
                    boolean creative = player.getAbilities().creativeMode;
                    boolean hasInfinity = EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 && ammoItemStack.get().getItem() == Items.ARROW;
                    if (!(creative || hasInfinity)) {

                        ammoBundle.get().decrementItem(1, BowItem.BOW_PROJECTILES);
                        return;
                    }
                }
            }
        }
    }

}
