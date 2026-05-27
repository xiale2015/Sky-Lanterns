package com.xl.skylantern.client.model;

import com.xl.skylantern.SkyLanternsReborn;
import com.xl.skylantern.common.entities.SkyLanternEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class PaperLanternPinkModel extends EntityModel<SkyLanternEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(SkyLanternsReborn.MODID, "sky_lantern"), "main");

    ModelPart Bottom;
    ModelPart Top2;
    ModelPart Top3;
    ModelPart Top;
    ModelPart Top21;
    ModelPart Top31;
    ModelPart Front;
    ModelPart Left;
    ModelPart Right;
    ModelPart Back;

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Converted from old 1.20.1 model (256x256 texture)
        // Old: Bottom = new ModelRenderer(this, 57, 67); setTexSize(256,256);
        //      addBox(-24, -0.5, -24, 48, 1, 48); setPos(0, 21, 0);
        // 1.21.1: addBox uses (originX, originY, originZ, sizeX, sizeY, sizeZ)
        //         PartPose.offset = old setPos
        root.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(57, 67)
                        .addBox(-24F, -0.5F, -24F, 48F, 1F, 48F, CubeDeformation.NONE),
                PartPose.offset(0F, 21F, 0F));

        // Old: Top2 = new ModelRenderer(this, 65, 116); addBox(-22, -0.5, -22, 44, 1, 44); setPos(0, 22, 0);
        root.addOrReplaceChild("top2", CubeListBuilder.create()
                        .texOffs(65, 116)
                        .addBox(-22F, -0.5F, -22F, 44F, 1F, 44F, CubeDeformation.NONE),
                PartPose.offset(0F, 22F, 0F));

        // Old: Top3 = new ModelRenderer(this, 73, 161); addBox(-20, -0.5, -20, 40, 1, 40); setPos(0, 23, 0);
        root.addOrReplaceChild("top3", CubeListBuilder.create()
                        .texOffs(73, 161)
                        .addBox(-20F, -0.5F, -20F, 40F, 1F, 40F, CubeDeformation.NONE),
                PartPose.offset(0F, 23F, 0F));

        // Old: Top = new ModelRenderer(this, 57, 15); addBox(-24, -0.5, -24, 48, 1, 48); setPos(0, -36, 0);
        root.addOrReplaceChild("top", CubeListBuilder.create()
                        .texOffs(57, 15)
                        .addBox(-24F, -0.5F, -24F, 48F, 1F, 48F, CubeDeformation.NONE),
                PartPose.offset(0F, -36F, 0F));

        // Old: Top21 = new ModelRenderer(this, 65, 20); addBox(-22, -0.5, -22, 44, 1, 44); setPos(0, -37, 0);
        root.addOrReplaceChild("top21", CubeListBuilder.create()
                        .texOffs(65, 20)
                        .addBox(-22F, -0.5F, -22F, 44F, 1F, 44F, CubeDeformation.NONE),
                PartPose.offset(0F, -37F, 0F));

        // Old: Top31 = new ModelRenderer(this, 73, 23); addBox(-20, -0.5, -20, 40, 1, 40); setPos(0, -38, 0);
        root.addOrReplaceChild("top31", CubeListBuilder.create()
                        .texOffs(73, 23)
                        .addBox(-20F, -0.5F, -20F, 40F, 1F, 40F, CubeDeformation.NONE),
                PartPose.offset(0F, -38F, 0F));

        // Old: Front = new ModelRenderer(this, 0, 0); addBox(-24, -28, -0.5, 48, 56, 1); setPos(0, -7.5, -24);
        root.addOrReplaceChild("front", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24F, -28F, -0.5F, 48F, 56F, 1F, CubeDeformation.NONE),
                PartPose.offset(0F, -7.5F, -24F));

        // Old: Left = new ModelRenderer(this, 0, 0); addBox(-24, -28, -0.5, 48, 56, 1); setPos(-24, -7.5, 0);
        //       setRotation(Left, 0, -1.570796F, 0); -> Y rotation = -90°
        root.addOrReplaceChild("left", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24F, -28F, -0.5F, 48F, 56F, 1F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-24F, -7.5F, 0F, 0F, -1.570796F, 0F));

        // Old: Right = new ModelRenderer(this, 0, 0); addBox(-24, -28, -0.5, 48, 56, 1); setPos(24, -7.5, 0);
        //       setRotation(Right, 0, -1.570796F, 0);
        root.addOrReplaceChild("right", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24F, -28F, -0.5F, 48F, 56F, 1F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(24F, -7.5F, 0F, 0F, -1.570796F, 0F));

        // Old: Back = new ModelRenderer(this, 0, 0); addBox(-24, -28, -0.5, 48, 56, 1); setPos(0, -7.5, 24);
        root.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24F, -28F, -0.5F, 48F, 56F, 1F, CubeDeformation.NONE),
                PartPose.offset(0F, -7.5F, 24F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    public PaperLanternPinkModel(ModelPart root) {
        Bottom = root.getChild("bottom");
        Top2 = root.getChild("top2");
        Top3 = root.getChild("top3");
        Top = root.getChild("top");
        Top21 = root.getChild("top21");
        Top31 = root.getChild("top31");
        Front = root.getChild("front");
        Left = root.getChild("left");
        Right = root.getChild("right");
        Back = root.getChild("back");
    }

    @Override
    public void setupAnim(SkyLanternEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        Bottom.render(poseStack, buffer, packedLight, packedOverlay, color);
        Top2.render(poseStack, buffer, packedLight, packedOverlay, color);
        Top3.render(poseStack, buffer, packedLight, packedOverlay, color);
        Top.render(poseStack, buffer, packedLight, packedOverlay, color);
        Top21.render(poseStack, buffer, packedLight, packedOverlay, color);
        Top31.render(poseStack, buffer, packedLight, packedOverlay, color);
        Front.render(poseStack, buffer, packedLight, packedOverlay, color);
        Left.render(poseStack, buffer, packedLight, packedOverlay, color);
        Right.render(poseStack, buffer, packedLight, packedOverlay, color);
        Back.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
