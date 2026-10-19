package edurise.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** A running quiz: holds the questions and the student's answers, then grades them. */
public class Quiz {
    public static final int PERFECT_BONUS = 20;   // promo-style bonus for a perfect stage

    private final String topic;
    private final int stage;
    private final List<Question> questions;
    private final String[] answers;

    public Quiz(String topic, int stage, List<Question> questions) {
        this.topic = topic;
        this.stage = stage;
        this.questions = questions;
        this.answers = new String[questions.size()];
    }

    public void setAnswer(int index, String answer) { answers[index] = answer; }
    public List<Question> getQuestions() { return questions; }
    public String getTopic() { return topic; }
    public int getStage() { return stage; }
    public int size() { return questions.size(); }

    /** Grading goes through the Gradable contract, so any question type works. */
    public QuizResult grade(int userId) {
        int correct = 0;
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Gradable g = questions.get(i);
            if (g.isCorrect(answers[i])) {
                correct++;
                score += g.getPoints();
            }
        }
        if (correct == questions.size()) score += PERFECT_BONUS;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new QuizResult(0, userId, topic, stage, correct, questions.size(), score, now);
    }
}
