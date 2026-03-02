package com.bawnorton.trimica.data.tags;

import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;

public final class ForcedTagEntry extends TagEntry {

    public ForcedTagEntry(Identifier id) {
        super(id, true, true);
    }

    @Override
    public boolean verifyIfPresent(
        Predicate<Identifier> elementPredicate,
        Predicate<Identifier> tagPredicate
    ) {
        return true;
    }
}
