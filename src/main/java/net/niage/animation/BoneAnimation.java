package net.niage.animation;

import java.util.ArrayList;
import java.util.List;

public class BoneAnimation {

    private Bone bone;
    private List<KeyFrame> keyFrames;

    public BoneAnimation(Bone bone) {
        this.bone = bone;
        this.keyFrames = new ArrayList<>();
    }

    public BoneAnimation(Bone bone, List<KeyFrame> keyFrames) {
        this.bone = bone;
        this.keyFrames = keyFrames;
    }

    public void addKeyFrame(KeyFrame keyFrame) {
        keyFrames.add(keyFrame);
    }

    public List<KeyFrame> keyFrames() {
        return keyFrames;
    }

    public Bone bone() {
        return bone;
    }

}
