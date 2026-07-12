package model;

public final class ScoreManager {
    public static final int CORRECT=10, BONUS=5, PERFECT_LEVEL=20;
    private ScoreManager(){}
    public static int stars(int score,int maximum){double ratio=maximum==0?0:(double)score/maximum;return Math.max(1,Math.min(5,(int)Math.ceil(ratio*5)));}
}
