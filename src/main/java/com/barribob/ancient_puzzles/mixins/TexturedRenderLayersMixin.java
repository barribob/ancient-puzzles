package com.barribob.ancient_puzzles.mixins;

import com.barribob.ancient_puzzles.Mod;
import com.barribob.ancient_puzzles.blocks.AncientChestBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(TexturedRenderLayers.class)
public abstract class TexturedRenderLayersMixin {
    private static final SpriteIdentifier stoneBrickChestTexture = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, Mod.INSTANCE.identifier("model/stone_brick_chest"));

    @Inject(method = "getChestTexture(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/block/enums/ChestType;Z)Lnet/minecraft/client/util/SpriteIdentifier;",
            at = @At("HEAD"),
            cancellable = true)
    private static void ancientPuzzles_getChestTexture(BlockEntity blockEntity, ChestType type, boolean christmas, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (blockEntity instanceof AncientChestBlockEntity) {
            cir.setReturnValue(stoneBrickChestTexture);
        }
    }

    @Inject(method = "addDefaultTextures", at = @At("TAIL"))
    private static void ancientPuzzles_addDefaultTextures(Consumer<SpriteIdentifier> adder, CallbackInfo ci) {
        adder.accept(stoneBrickChestTexture);
    }
}
