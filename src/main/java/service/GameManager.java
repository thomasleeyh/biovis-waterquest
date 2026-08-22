package service;

import model.*;import java.util.*;
public final class GameManager {
 private static final GameManager INSTANCE=new GameManager(); private final Player player=new Player();private final Set<Integer> completed=new HashSet<>();private final Set<String> completedLearningTopics=new HashSet<>();private Quiz finalQuiz;
 private GameManager(){} public static GameManager getInstance(){return INSTANCE;} public Player getPlayer(){return player;}
 public void newGame(String name){player.reset();player.setName(name);completed.clear();completedLearningTopics.clear();finalQuiz=null;}
 public void completeLearningTopic(String topicId){if(topicId!=null&&!topicId.isBlank()&&completedLearningTopics.add(topicId)&&completedLearningTopics.size()>=6)player.unlock(Achievement.KNOWLEDGE_EXPLORER);}
 public int completedLearningTopicCount(){return completedLearningTopics.size();}
 public boolean hasCompletedLearningNotes(){return completedLearningTopics.size()>=6;}
 public void recordAnswer(boolean correct){player.recordAnswer(correct);if(correct){player.addScore(ScoreManager.CORRECT);player.unlock(Achievement.WATER_BEGINNER);}else player.loseLife();}
 public boolean completeLevel(int level){if(!completed.add(level))return false;player.addScore(ScoreManager.BONUS);player.setCurrentLevel(Math.min(7,level+1));Achievement[] rewards={Achievement.HYDROGEN_BOND_EXPERT,Achievement.MASTER_CHEMIST,Achievement.HEAT_MASTER,Achievement.COOLING_HERO,Achievement.ICE_SCIENTIST,Achievement.COHESION_CHAMPION};player.unlock(rewards[level-1]);return true;}
 public int crystals(){return completed.size();} public boolean isUnlocked(int level){return level<=player.getCurrentLevel();}
 public Quiz startFinalQuiz(){finalQuiz=new Quiz(QuestionLoader.load(),10);return finalQuiz;} public Quiz getFinalQuiz(){return finalQuiz;}
}
