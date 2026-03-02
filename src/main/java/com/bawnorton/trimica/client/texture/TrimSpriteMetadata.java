package com.bawnorton.trimica.client.texture;

import com.bawnorton.trimica.client.palette.TrimPalette;
import com.bawnorton.trimica.trim.TrimmedType;
import java.nio.file.Path;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

public record TrimSpriteMetadata(
    ArmorTrim trim,
    TrimPalette palette,
    Identifier baseTexture,
    TrimmedType trimmedType
) {
    public boolean isAnimated() {
        return palette.isAnimated();
    }

    public Path toDebugPath() {
        String pattern = trim
            .pattern()
            .value()
            .assetId()
            .toString()
            .replace(':', '/');
        String material = trim
            .material()
            .value()
            .assets()
            .base()
            .suffix()
            .replace(':', '/');
        String metadata = palette.getMetadataString();
        String baseTexturePath =
            baseTexture.getNamespace() +
            "/" +
            baseTexture.getPath().replace(':', '/');
        baseTexturePath = baseTexturePath.substring(
            0,
            baseTexturePath.lastIndexOf('.')
        );
        String fileName = String.format(
            "%s/%s/%s/%s",
            baseTexturePath,
            pattern,
            material,
            metadata
        );
        return Path.of(fileName);
    }
}
