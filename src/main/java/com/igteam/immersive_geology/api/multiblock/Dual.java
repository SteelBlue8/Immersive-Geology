package com.igteam.immersive_geology.api.multiblock;

public class Dual<F,S> {

    private final F first;
    private S second;

    public Dual(F first, S second){
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
