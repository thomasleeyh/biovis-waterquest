package model;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class Player {
    private String name="Scientist"; private int score, lives=3, currentLevel=1, correctAnswers, wrongAnswers;
    private Instant startedAt=Instant.now(); private final Set<Achievement> achievements=EnumSet.noneOf(Achievement.class);
    public String getName(){return name;} public void setName(String value){name=value==null||value.isBlank()?"Scientist":value.trim();}
    public int getScore(){return score;} public void addScore(int value){score=Math.max(0,score+value);}
    public int getLives(){return lives;} public void loseLife(){lives=Math.max(0,lives-1);}
    public int getCurrentLevel(){return currentLevel;} public void setCurrentLevel(int value){currentLevel=value;}
    public int getCorrectAnswers(){return correctAnswers;} public int getWrongAnswers(){return wrongAnswers;}
    public void recordAnswer(boolean correct){if(correct)correctAnswers++;else wrongAnswers++;}
    public double getAccuracy(){int total=correctAnswers+wrongAnswers;return total==0?0:(100.0*correctAnswers/total);}
    public Duration getTimePlayed(){return Duration.between(startedAt,Instant.now());}
    public Set<Achievement> getAchievements(){return Collections.unmodifiableSet(achievements);}
    public boolean unlock(Achievement achievement){return achievements.add(achievement);}
    public void reset(){score=0;lives=3;currentLevel=1;correctAnswers=wrongAnswers=0;achievements.clear();startedAt=Instant.now();}
}
