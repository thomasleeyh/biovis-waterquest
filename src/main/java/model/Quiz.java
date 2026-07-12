package model;

import java.util.ArrayList;import java.util.Collections;import java.util.List;
public final class Quiz {
    private final List<Question> questions; private int index, correct;
    public Quiz(List<Question> source,int count){var copy=new ArrayList<>(source);Collections.shuffle(copy);questions=List.copyOf(copy.subList(0,Math.min(count,copy.size())));}
    public Question current(){return isFinished()?null:questions.get(index);} public boolean answer(int choice){boolean ok=current().isCorrect(choice);if(ok)correct++;index++;return ok;}
    public int getIndex(){return index;} public int size(){return questions.size();} public int getCorrect(){return correct;} public boolean isFinished(){return index>=questions.size();}
}
