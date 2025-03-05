package necessities.block;

import necessities.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class BellBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 5, 13.0);

    public BellBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public void press(BlockState state, World world, BlockPos pos, @Nullable Entity player){
        if(!world.isClient) {
            world.playSound(null, pos, Sounds.DING, SoundCategory.BLOCKS, 1.0F, 1.1F);
            world.setBlockState(pos, state.with(Properties.POWERED, true), Block.NOTIFY_ALL);
            for (Direction d:DIRECTIONS) world.updateNeighborsExcept(pos.offset(d), this, d.getOpposite(), null);
            world.scheduleBlockTick(pos, this, 4);
            world.emitGameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(Properties.POWERED)) {
            world.setBlockState(pos, state.with(Properties.POWERED, false), Block.NOTIFY_ALL);
            for (Direction d:DIRECTIONS) world.updateNeighborsExcept(pos.offset(d), this, d.getOpposite(), null);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        press(state, world, pos, player);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        press(state, world, hit.getBlockPos(), projectile.getOwner());
    }

    @Override
    protected void onExploded(BlockState state, ServerWorld world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        super.onExploded(state, world, pos, explosion, stackMerger);
        press(state, world, pos, explosion.getCausingEntity());
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWERED)? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWERED) ? 15 : 0;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }
}
