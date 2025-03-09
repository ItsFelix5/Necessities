package necessities.item;

import necessities.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DismemberingAxeItem extends Item {
    public DismemberingAxeItem(Settings settings) {
        super(settings);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(target instanceof PlayerEntity player && player.getHealth() - baseAttackDamage <= 0) {
            player.getDataTracker().set(Main.OFFHAND, false);
            player.dropItem(player.getOffHandStack(), false);
            player.getInventory().removeStack(PlayerInventory.OFF_HAND_SLOT);
            return -baseAttackDamage + 1;
        }
        return 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }
}
