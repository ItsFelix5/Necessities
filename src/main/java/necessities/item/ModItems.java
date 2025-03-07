package necessities.item;

import necessities.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class ModItems {
    public static final Item CONFETTI_BOMB = register(
            "confetti_bomb",
            ConfettiBombItem::new,
            new Item.Settings()
    );
    public static final Item TOMATO = register(
            "tomato",
            TomatoItem::new,
            new Item.Settings()
    );
    public static final Item YIPPEE = register(
            "yippee",
            YippeeItem::new,
            new Item.Settings().useCooldown(0.2F)
    );
    public static final Item LASHING_POTATO = register("lashing_potato", LashingPotatoItem::new, new Item.Settings().maxCount(1).maxDamage(100));

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Main.id(name));
        Item blockItem = factory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, blockItem);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
            itemGroup.add(CONFETTI_BOMB);
            itemGroup.add(YIPPEE);
            itemGroup.add(LASHING_POTATO);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.add(TOMATO));
    }
}
