package com.barribob.ancient_puzzles.mixins;

import com.barribob.ancient_puzzles.Mod;
import com.barribob.ancient_puzzles.blocks.AncientChestBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Shadow
    @Final
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void ancientPuzzles_Render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockState blockState = ((BlockItem) item).getBlock().getDefaultState();
            if (blockState.isOf(Mod.INSTANCE.getBlocks().getAncientChest())) {
                blockEntityRenderDispatcher.renderEntity(new AncientChestBlockEntity(BlockPos.ORIGIN, Mod.INSTANCE.getBlocks().getAncientChest().getDefaultState()), matrices, vertexConsumers, light, overlay);
                ci.cancel();
            }
        }
    }
}
