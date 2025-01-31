package magiclibrary.block;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import magiclibrary.MagicLibrary;
import magiclibrary.block.entity.MagicLibraryBlockEntity;
import magiclibrary.item.components.MagicLibraryComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MagicLibraryBlock extends Block implements BlockEntityProvider {
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty FILLED = IntProperty.of("filled", 0, 18);
    public static final MapCodec<MagicLibraryBlock> CODEC = createCodec(MagicLibraryBlock::new);
    
    private MagicLibraryBlockEntity breakingBlockEntity = null;
    
    public MagicLibraryBlock(Settings settings) {
        super(settings);
        this.setDefaultState(stateManager.getDefaultState().with(FACING, Direction.NORTH).with(FILLED, 0));
    }    

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicLibraryBlockEntity(pos, state);
    }


    @Override
    protected MapCodec<MagicLibraryBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, FILLED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.getBlockEntity(pos) instanceof MagicLibraryBlockEntity breakingBlockEntity) {
            var enchantmentsString = itemStack.get(MagicLibraryComponents.ENCHANTMENTS_COMPONENT);
            if (enchantmentsString != null) {
                breakingBlockEntity.setEnchantmentsString(enchantmentsString);
                updateFilled(world, pos, state);
            }
            var points = itemStack.get(MagicLibraryComponents.POINTS_COMPONENT);
            if (points != null) {
                breakingBlockEntity.setPoints(points);
            }
        }
    }

    @Override
    protected void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        breakingBlockEntity = (MagicLibraryBlockEntity)world.getBlockEntity(pos);
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, net.minecraft.loot.context.LootWorldContext.Builder builder) {
        var result = new ArrayList<ItemStack>();
        var entry = Registries.ITEM.getEntry(MagicLibrary.id("magic_library"));
        if (entry.isPresent() && breakingBlockEntity != null) {
            var stack = new ItemStack(entry.get());
            stack.set(MagicLibraryComponents.ENCHANTMENTS_COMPONENT, breakingBlockEntity.getEnchantmentsString());
            stack.set(MagicLibraryComponents.POINTS_COMPONENT, breakingBlockEntity.getPoints());
            result.add(stack);
        }
        return result;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) { 
            return ActionResult.SUCCESS;
        }

        if (stack.getItem() != Items.ENCHANTED_BOOK) {
            var factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
                return ActionResult.SUCCESS;
            }
            else {
                return ActionResult.FAIL;
            }
        }

        if (world.getBlockEntity(pos) instanceof MagicLibraryBlockEntity libraryBlockEntity && EnchantmentHelper.hasEnchantments(stack)) {
            libraryBlockEntity.importEnchantments(stack);

            // consume book
            stack.decrement(1);

            // update state to indicate fullness, but only if a enchantment was registered
            updateFilled(world, pos, state);

            player.sendMessage(Text.translatable("text.magiclibrary.book_registered"), true);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Nullable
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (MagicLibraryBlockEntity)world.getBlockEntity(pos);
    }

    public void updateFilled(World world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof MagicLibraryBlockEntity libraryBlockEntity) {
            var size = libraryBlockEntity.getEnchantmentsSize();
            if (size > 18) { size = 18; }
            world.setBlockState(pos, state.with(FILLED, size));
        }
    }
}
