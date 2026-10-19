package edurise.model;

public class TrueFalseQuestion extends Question {
    private static final long serialVersionUID = 1L;

    public TrueFalseQuestion(int id, String topic, int stage, String text, String answer) {
        super(id, topic, stage, text, answer);
    }

    @Override public String[] getChoices() { return new String[] {"True", "False"}; }
    @Override public String getType() { return "TF"; }
    @Override public int getPoints() { return 5; }
}
