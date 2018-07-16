package com.example.ast;

public class ViewAndroid {

    private CProperties cProperties;
    private Block block;

    public ViewAndroid(CProperties cProperties, Block block) {
        this.cProperties = cProperties;
        this.block = block;
    }

    public CProperties getcProperties() {
        return cProperties;
    }

    public void setcProperties(CProperties cProperties) {
        this.cProperties = cProperties;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
