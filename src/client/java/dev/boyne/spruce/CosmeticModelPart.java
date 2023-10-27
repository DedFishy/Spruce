package dev.boyne.spruce;

import net.minecraft.client.model.ModelPart;

import java.util.ArrayList;

public class CosmeticModelPart {
    public ModelPart modelPart;

    public ArrayList<Float> parentRotation;
    public ModelPart parent;

    public CosmeticModelPart(ModelPart modelPart, ArrayList<Float> parentRotation, ModelPart parent) {
        this.modelPart = modelPart;
        this.parentRotation = parentRotation;
        this.parent = parent;

    }
}
