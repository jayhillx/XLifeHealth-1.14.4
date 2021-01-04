package me.daqem.xlifehealth.utils;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;

public class XLifeModifiers {

    public static void applyMaxHealthModifier(PlayerEntity player, float amount) {
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        attribute.applyPersistentModifier(new AttributeModifier("MaxHealth", amount, AttributeModifier.Operation.ADDITION));
        player.setHealth(player.getMaxHealth());
    }

    public static void removeMaxHealthModifiers(PlayerEntity player) {
        ModifiableAttributeInstance iAttributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
        Collection<AttributeModifier> modifiers = iAttributeInstance.getModifierListCopy();
        for (AttributeModifier modifier : modifiers) {
            if (modifier.getName().equals("MaxHealth")) {
                iAttributeInstance.removeModifier(modifier);
            }
        }
    }
}
