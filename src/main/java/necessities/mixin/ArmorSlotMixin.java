package necessities.mixin;

import necessities.block.ModBlocks;
import necessities.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.slot.ArmorSlot")
public class ArmorSlotMixin {
    @Shadow
    @Final
    private EquipmentSlot equipmentSlot;

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(ModBlocks.TINY_POTATO.asItem()) || stack.isOf(ModItems.YIPPEE) && equipmentSlot == EquipmentSlot.HEAD) cir.setReturnValue(true);
    }
}
