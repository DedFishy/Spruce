package dev.boyne.spruce;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.boyne.spruce.mixin.client.PlayerEntityModelAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.io.*;
import java.util.*;

public class JSONFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    Map<String, ModelPart> modelParts = new HashMap<>();

    int textureWidth;
    int textureHeight;
    Set<Direction> directionalSet = new HashSet<>();

    Map<String, ModelPart> playerModelParts = new HashMap<>();
    Map<String, float[]> playerModelPartOffsets = new HashMap<>();
    /*
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: head: [0.0, 0.0, 0.0]
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: body: [0.0, 0.0, 0.0]
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: leftArm: [5.0, 2.5, 0.0]
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: rightArm: [-5.0, 2.5, 0.0]
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: leftLeg: [1.9, 12.0, 0.0]
[23:09:01] [Render thread/INFO] (Minecraft) [STDOUT]: rightLeg: [-1.9, 12.0, 0.0]
     */

    public JSONFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);

        playerModelParts.put("head", context.getModel().head);
        playerModelParts.put("body", context.getModel().body);
        playerModelParts.put("leftArm", context.getModel().leftArm);
        playerModelParts.put("leftLeg", context.getModel().leftLeg);
        playerModelParts.put("rightArm", context.getModel().rightArm);
        playerModelParts.put("rightLeg", context.getModel().rightLeg);

        playerModelPartOffsets.put("head", new float[] {0.0F, 0.0F, 0.0F});
        playerModelPartOffsets.put("body", new float[] {0.0F, -11.0F, 0.0F});

        boolean isSlim = ((PlayerEntityModelAccessor) context.getModel()).thinArms();
        if (isSlim) {
            playerModelPartOffsets.put("leftArm", new float[]{5.0F - 1F, 2.5F - 12.5F, 0.0F});
            playerModelPartOffsets.put("rightArm", new float[]{-5.0F + 1F, 2.5F - 12.5F, 0.0F});
        }
        else {
            playerModelPartOffsets.put("leftArm", new float[]{5.0F, 2.5F - 12.5F, 0.0F});
            playerModelPartOffsets.put("rightArm", new float[]{-5.0F, 2.5F - 12.5F, 0.0F});
        }
        playerModelPartOffsets.put("leftLeg", new float[] {-1.9F, 12.0F - 13F, 0.0F});
        playerModelPartOffsets.put("rightLeg", new float[] {1.9F, 12.0F - 13F, 0.0F});

        InputStream fileStream = null;
        NativeImage image;
        try {
            fileStream = new FileInputStream("config/cool.png");
            image = NativeImage.read(fileStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
        Identifier textureId = new Identifier("spruce", "cosmetics/df/cool");
        textureManager.registerTexture(textureId, texture);

        directionalSet.add(Direction.UP);
        directionalSet.add(Direction.DOWN);
        directionalSet.add(Direction.EAST);
        directionalSet.add(Direction.WEST);
        directionalSet.add(Direction.NORTH);
        directionalSet.add(Direction.SOUTH);

        File model = new File("config/cool.cfg");
        String json = "";
        Scanner reader;
        try {
            reader = new Scanner(model);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            json += data + "\n";
        }
        reader.close();

        JsonObject modelJSON = JsonParser.parseString(json).getAsJsonObject();

        textureWidth = modelJSON.get("textureSize").getAsJsonArray().get(0).getAsInt();
        textureHeight = modelJSON.get("textureSize").getAsJsonArray().get(1).getAsInt();

        ArrayList<Float> zeroArray = new ArrayList<>();
        zeroArray.add(0f);
        zeroArray.add(0f);
        zeroArray.add(0f);

        addModelParts(modelParts, zeroArray, zeroArray, modelJSON.getAsJsonArray("models"));
    }

    public void addModelParts(Map<String, ModelPart> targetMap, ArrayList<Float> parentTranslation, ArrayList<Float> parentRotation, JsonArray models) {
        for (JsonElement modelEl : models) {
            JsonObject model = modelEl.getAsJsonObject();

            String name = "";
            if (model.has("id")) {
                name = model.get("id").getAsString();
            } else if (model.has("attachTo")) {
                name = model.get("attachTo").getAsString();

                parentRotation.set(0, playerModelParts.get(name).pitch);
                parentRotation.set(1, playerModelParts.get(name).yaw);
                parentRotation.set(2, playerModelParts.get(name).roll);

                parentTranslation.set(0, playerModelPartOffsets.get(name)[0]);
                parentTranslation.set(1, playerModelPartOffsets.get(name)[1]);
                parentTranslation.set(2, playerModelPartOffsets.get(name)[2]);

                System.out.println(name + ": " + parentTranslation);
            }

            for (int i = 0; i < 3; i++) {
                parentTranslation.set(i, parentTranslation.get(i) + model.get("translate").getAsJsonArray().get(i).getAsFloat());
            }

            if (model.has("rotation")) {
                for (int i = 0; i < 3; i++) {
                    parentRotation.set(i, parentRotation.get(i) + model.get("rotation").getAsJsonArray().get(i).getAsFloat());
                }
            }

            JsonArray childCuboids = model.get("boxes").getAsJsonArray();

            ArrayList<ModelPart.Cuboid> generatedChildren = new ArrayList<>();
            for (JsonElement child : childCuboids) {
                JsonObject attributes = child.getAsJsonObject();

                int u = attributes.get("textureOffset").getAsJsonArray().get(0).getAsInt();
                int v = attributes.get("textureOffset").getAsJsonArray().get(1).getAsInt();

                float x = attributes.get("coordinates").getAsJsonArray().get(0).getAsInt() - parentTranslation.get(0);
                float y = -attributes.get("coordinates").getAsJsonArray().get(1).getAsInt() + parentTranslation.get(1);
                float z = attributes.get("coordinates").getAsJsonArray().get(2).getAsInt() - parentTranslation.get(2);

                float sizeX = attributes.get("coordinates").getAsJsonArray().get(3).getAsInt();
                float sizeY = attributes.get("coordinates").getAsJsonArray().get(4).getAsInt();
                float sizeZ = attributes.get("coordinates").getAsJsonArray().get(5).getAsInt();

                ModelPart.Cuboid cuboid = new ModelPart.Cuboid(u, v, x, y, z, sizeX, sizeY, sizeZ, 0f, 0f, 0f, false, textureWidth, textureHeight, directionalSet);

                generatedChildren.add(cuboid);
            }

            Map<String, ModelPart> nextChildren = new HashMap<>();
            ModelPart newPart = new ModelPart(generatedChildren, nextChildren);

            targetMap.put(name, newPart);

            if (model.has("submodels")) {
                addModelParts(nextChildren, parentTranslation, parentRotation, model.get("submodels").getAsJsonArray());
            }
        }
    }


    //TODO: MAke this load JEM files and textures
    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        /*float o = 0;//MathHelper.lerp((float)tickDelta, (float)abstractClientPlayerEntity.prevYaw, (float)abstractClientPlayerEntity.getYaw()) - MathHelper.lerp((float)tickDelta, (float)abstractClientPlayerEntity.prevBodyYaw, (float)abstractClientPlayerEntity.bodyYaw);
        float p = 0;//MathHelper.lerp((float)tickDelta, (float)abstractClientPlayerEntity.prevPitch, (float)abstractClientPlayerEntity.getPitch());
        matrixStack.push();
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(o)); // Rotates
        //matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(p)); // Rotates
        matrixStack.translate(-0.25f, 0.0f, 0.0f); // Moves
        //matrixStack.translate(0.0f, -0.375f, 0.0f); // Moves
        //matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-p)); // Rotates
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-o)); // Rotates
        float q = 1f;
        matrixStack.scale(q, q, q); // Change the size of the model by the passed in factor
        //this.customModelPart.render(matrixStack, vertexConsumer, light, overlay);*/

        for (ModelPart modelPart : modelParts.values()) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(this.getTexture(abstractClientPlayerEntity))); // Load texture
            int overlay = LivingEntityRenderer.getOverlay((LivingEntity)abstractClientPlayerEntity, 0.0f); // Get damage overlay and all that jazz

            matrixStack.push();

            modelPart.render(matrixStack, vertexConsumer, light, overlay);

            matrixStack.pop();
        }



    }

    @Override
    protected Identifier getTexture(AbstractClientPlayerEntity Entity) {
        return new Identifier("spruce", "cosmetics/df/cool");
    }


}
