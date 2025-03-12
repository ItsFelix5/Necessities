package necessities.block;

import necessities.Sounds;
import necessities.extension.PlayerEntityExtension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BellBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 5, 13.0);

    public BellBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.POWER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWER);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public void press(BlockState state, World world, BlockPos pos, @Nullable Entity presser) {
        if (world.isClient || state.get(Properties.POWER) == 15) return;
        world.emitGameEvent(presser, GameEvent.BLOCK_ACTIVATE, pos);
        world.setBlockState(pos, state.with(Properties.POWER, state.get(Properties.POWER) + 1), Block.NOTIFY_ALL);
        world.playSound(null, pos, Sounds.DING, SoundCategory.BLOCKS, 1.0F, 1.1F);
        for (Direction d : DIRECTIONS) world.updateNeighborsExcept(pos.offset(d), this, d.getOpposite());
        world.scheduleBlockTick(pos, this, 8);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int power = state.get(Properties.POWER);
        if (power > 0) {
            world.setBlockState(pos, state.with(Properties.POWER, power - 1), Block.NOTIFY_ALL);
            for (Direction d : DIRECTIONS) world.updateNeighborsExcept(pos.offset(d), this, d.getOpposite());
            if (power > 1) world.scheduleBlockTick(pos, this, 8);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(Properties.POWER) == 15) {
            ((PlayerEntityExtension) player).necessities$setNoClipTicks(80);
            Vec3d vec = player.getEyePos().subtract(Vec3d.of(pos));
            double size = (1.0 - vec.length() / 6) * 15 / vec.length();
            player.addVelocity(vec.multiply(size * 3, size, size * 3));
        } else press(state, world, pos, player);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        press(state, world, hit.getBlockPos(), projectile.getOwner());
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWER) > 0 ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWER) > 0 ? 15 : 0;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }
}
