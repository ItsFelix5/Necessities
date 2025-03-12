package necessities.item;

import necessities.entity.LashingPotatoHookEntity;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LashingPotatoItem extends Item {
    public LashingPotatoItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        LashingPotatoHookEntity entity = ((PlayerEntityExtension) user).necessities$getLashingPotatoHook();
        if (entity != null) {
            if (!world.isClient()) {
                entity.discard();
                ((PlayerEntityExtension) user).necessities$setLashingPotatoHook(null);
            }

            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            if (!world.isClient) world.spawnEntity(new LashingPotatoHookEntity(world, user));

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_FISHING_BOBBER_THROW,
                    SoundCategory.NEUTRAL,
                    0.5F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
