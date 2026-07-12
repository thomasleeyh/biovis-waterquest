package model;

import java.util.List;

/** Immutable data used by the Learning Notes textbook. */
public record LearningTopic(String id, String title, String description, String imagePath,
                            String secondaryImagePath, List<String> keyPoints,
                            String funFact, String examTip) {
    public LearningTopic {
        keyPoints = List.copyOf(keyPoints == null ? List.of() : keyPoints);
    }
}
