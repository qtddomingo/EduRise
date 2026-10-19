package edurise.model;

/** Standard grading contract: every question type must implement it. */
public interface Gradable {
    boolean isCorrect(String answer);
    int getPoints();
}
