package necessities.item;

import necessities.Sounds;
import necessities.particle.ModParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class YippeeItem extends Item {
    public YippeeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                if (player.getEyePos().distanceTo(user.getEyePos()) < 16) {
                    player.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(world.random.nextInt(15) == 0 ? Sounds.YIPPEE2 : Sounds.YIPPEE), SoundCategory.PLAYERS,
                            user.getX(), user.getEyeY(), user.getZ(),
                            1, world.getRandom().nextFloat() * 0.5F + 0.8F, world.getRandom().nextLong()));
                    player.networkHandler.sendPacket(new ParticleS2CPacket(
                            ModParticles.CONFETTI, false, user.getX(), user.getEyeY(), user.getZ(), 0, 0, 0, 0, 100
                    ));
                }
            }
        }
        user.getItemCooldownManager().set(this, 4);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
