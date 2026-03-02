package com.bawnorton.trimica.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import java.util.function.Function;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment(value = "client")
@Mixin(ItemModelResolver.class)
public interface ItemModelResolverAccessor {
    @Accessor("modelGetter")
    Function<Identifier, ItemModel> trimica$modelGetter();
}
