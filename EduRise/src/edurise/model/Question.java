package edurise.model;

import java.io.Serializable;

/** Base class of all question types (inheritance). */
public abstract class Question implements Gradable, Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String topic;
    private final int stage;
    private final String text;
    private final String answer;

    protected Question(int id, String topic, int stage, String text, String answer) {
        this.id = id;
        this.topic = topic;
        this.stage = stage;
        this.text = text;
        this.answer = answer;
    }

    public abstract String[] getChoices();
    public abstract String getType();

    @Override
    public boolean isCorrect(String given) {
        return given != null && answer.equalsIgnoreCase(given.trim());
    }

    public int getId() { return id; }
    public String getTopic() { return topic; }
    public int getStage() { return stage; }
    public String getText() { return text; }
    public String getAnswer() { return answer; }
}
