package com.xl.skylantern.client.render;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.client.model.PaperLanternPinkModel;
import com.xl.skylantern.common.entities.SkyLanternEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SkyLanternRender extends EntityRenderer<SkyLanternEntity> {

    protected PaperLanternPinkModel model;

    public SkyLanternRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PaperLanternPinkModel(context.bakeLayer(PaperLanternPinkModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(SkyLanternEntity entity) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(SkyLanternsReborn.MODID, "textures/entities/sky_lantern_" + entity.getColor().getRegistryPrefix() + ".png");
        SkyLanternsReborn.LOGGER.debug("SkyLantern texture: color={}, registryPrefix={}, path={}", entity.getColor(), entity.getColor().getRegistryPrefix(), loc);
        return loc;
    }

    @Override
    public void render(SkyLanternEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        SkyLanternsReborn.LOGGER.debug("SkyLantern render: model={}, color={}", this.model != null ? "NOT NULL" : "NULL", entity.getColor());
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        float scale = 0.25F;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180));

        long time = entity.level().getGameTime();
        long timeBase = time + (entity.getId() * 10L);
        float rate = 5;

        float tiltMax = (float) Math.sin(Math.toRadians(((timeBase) * 1F) % 360)) * 5F;
        float tiltCurX = (float) Math.sin(Math.toRadians(((timeBase) * rate) % 360)) * tiltMax;
        float tiltCurY = (float) Math.sin(Math.toRadians(((timeBase + 45) * rate) % 360)) * tiltMax;
        float tiltCurZ = (float) Math.sin(Math.toRadians(((timeBase + 90) * rate) % 360)) * tiltMax;

        float rotateY = (((float) timeBase * 0.1F) % 360);

        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(tiltCurX));
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(tiltCurY + rotateY));
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(tiltCurZ));

        this.setupRotations(entity, poseStack, entityYaw, partialTicks);

        if (this.model != null) {
            this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, entityYaw, 0.0F);
            VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
            this.model.renderToBuffer(poseStack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, -1);
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        Entity leashHolder = entity.getLeashHolder();
        if (leashHolder != null) {
            // 1.21.1 中 leash 渲染由 MobRenderer 自动处理
            // 如果需要自定义，可以在此添加
        }
    }

    protected void setupRotations(SkyLanternEntity entity, PoseStack poseStack, float rotationYaw, float partialTicks) {
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F - rotationYaw));

        if (entity.deathTime > 0) {
            float f = ((float) entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = net.minecraft.util.Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(f * 700F));
        }
    }
}
