package necessities.block;

import necessities.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class ModBlocks {
    public static final Block BELL = register(
            "bell",
            BellBlock::new,
            s -> s.mapColor(MapColor.GOLD).sounds(BlockSoundGroup.AMETHYST_BLOCK),
            s -> s
    );
    public static final Block TINY_POTATO = register(
            "tiny_potato",
            TinyPotatoBlock::new,
            s -> s.mapColor(MapColor.TERRACOTTA_BROWN).sounds(BlockSoundGroup.CROP),
            s -> s.food(new FoodComponent.Builder().alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 500, 1), 1.0f).build())
    );

    private static Block register(String name, Function<AbstractBlock.Settings, Block> factory, Function<AbstractBlock.Settings, AbstractBlock.Settings> blockSettings,
                                  Function<Item.Settings, Item.Settings> itemSettings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Main.id(name));
        Block block = factory.apply(blockSettings.apply(AbstractBlock.Settings.create()));

        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Main.id(name));
        BlockItem blockItem = new BlockItem(block, itemSettings.apply(new Item.Settings()));
        Registry.register(Registries.ITEM, itemKey, blockItem);

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((itemGroup) -> itemGroup.add(ModBlocks.BELL.asItem()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ModBlocks.TINY_POTATO.asItem()));
    }
}
