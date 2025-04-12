package necessities.item;

import necessities.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
            new Item.Settings());

    public static final Item LASHING_POTATO = register("lashing_potato", LashingPotatoItem::new, new Item.Settings().maxCount(1));
    public static final Item AXE_OF_DISMEMBERING = register(
            "axe_of_dismembering",
            DismemberingAxeItem::new,
            new Item.Settings()
                    .maxCount(1)
                    .attributeModifiers(AttributeModifiersComponent.builder()
                            .add(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                    new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, 8, EntityAttributeModifier.Operation.ADD_VALUE),
                                    AttributeModifierSlot.MAINHAND
                            )
                            .add(
                                    EntityAttributes.GENERIC_ATTACK_SPEED,
                                    new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -3.8, EntityAttributeModifier.Operation.ADD_VALUE),
                                    AttributeModifierSlot.MAINHAND
                            )
                            .build())
    );
    public static final Item BAT = register(
            "bat",
            Item::new,
            new Item.Settings()
                    .maxCount(1)
                    .attributeModifiers(AttributeModifiersComponent.builder()
                            .add(
                                    EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                                    new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, 10, EntityAttributeModifier.Operation.ADD_VALUE),
                                    AttributeModifierSlot.MAINHAND
                            )
                            .build())
    );

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Main.id(name));
        Item blockItem = factory.apply(settings);
        return Registry.register(Registries.ITEM, itemKey, blockItem);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
            itemGroup.add(CONFETTI_BOMB);
            itemGroup.add(YIPPEE);
            itemGroup.add(LASHING_POTATO);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
            itemGroup.add(AXE_OF_DISMEMBERING);
            itemGroup.add(BAT);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((itemGroup) -> itemGroup.add(TOMATO));
    }
}
