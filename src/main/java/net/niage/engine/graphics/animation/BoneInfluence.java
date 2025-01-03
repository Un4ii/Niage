package net.niage.engine.graphics.animation;

public class BoneInfluence {

    private final Bone bone;
    private final float weight;

    public BoneInfluence(Bone bone, float weight) {
        this.bone = bone;
        this.weight = weight;
    }

    public Bone bone() {
        return bone;
    }

    public float weight() {
        return weight;
    }

}
