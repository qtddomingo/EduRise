package edurise.model;

import java.io.Serializable;

/** One graded attempt. Serializable so the history can be exported to a .ser file. */
public class QuizResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int userId;
    private final String topic;
    private final int stage;
    private final int correct;
    private final int total;
    private final int score;
    private final String takenAt;

    public QuizResult(int id, int userId, String topic, int stage,
                      int correct, int total, int score, String takenAt) {
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        this.stage = stage;
        this.correct = correct;
        this.total = total;
        this.score = score;
        this.takenAt = takenAt;
    }

    public int getPercent() { return total == 0 ? 0 : correct * 100 / total; }
    public boolean isPerfect() { return total > 0 && correct == total; }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTopic() { return topic; }
    public int getStage() { return stage; }
    public int getCorrect() { return correct; }
    public int getTotal() { return total; }
    public int getScore() { return score; }
    public String getTakenAt() { return takenAt; }

    @Override
    public String toString() {
        return takenAt + " | " + topic + " (Stage " + stage + ") | " + correct + "/" + total + " | " + score + " pts";
    }
}
