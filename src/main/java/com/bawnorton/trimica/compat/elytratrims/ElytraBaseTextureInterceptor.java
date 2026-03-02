package com.bawnorton.trimica.compat.elytratrims;

import com.bawnorton.trimica.api.BaseTextureInterceptor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.Nullable;

/**
 * This interceptor is used to provide custom base textures for the Elytra item when ElytraTrims is present.
 */
public class ElytraBaseTextureInterceptor implements BaseTextureInterceptor {

    @Override
    public Identifier interceptItemTexture(
        @Nullable Identifier expectedBaseTexture,
        ItemStack itemWithTrim,
        ArmorTrim armourTrim
    ) {
        if (itemWithTrim.getItem() != Items.ELYTRA) return expectedBaseTexture;

        return Identifier.withDefaultNamespace(
            "textures/trims/items/wings_trim.png"
        );
    }

    @Override
    public Identifier interceptArmourTexture(
        @Nullable Identifier expectedBaseTexture,
        ItemStack itemWithTrim,
        ArmorTrim armourTrim
    ) {
        if (itemWithTrim.getItem() != Items.ELYTRA) return expectedBaseTexture;

        TrimPattern pattern = armourTrim.pattern().value();
        String assetId = pattern.assetId().getPath();
        return Identifier.withDefaultNamespace(
            "textures/trims/entity/wings/%s.png".formatted(assetId)
        );
    }
}
