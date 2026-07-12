package model;

import java.util.List;
public record Question(String prompt, List<String> choices, int correctIndex, String explanation) {
    public Question { choices=List.copyOf(choices); if(correctIndex<0||correctIndex>=choices.size())throw new IllegalArgumentException("Invalid correct answer"); }
    public boolean isCorrect(int index){return index==correctIndex;}
}
