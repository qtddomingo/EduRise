package edurise.model;

public class MultipleChoiceQuestion extends Question {
    private static final long serialVersionUID = 1L;
    private final String[] choices;

    public MultipleChoiceQuestion(int id, String topic, int stage, String text,
                                  String answer, String[] choices) {
        super(id, topic, stage, text, answer);
        this.choices = choices;
    }

    @Override public String[] getChoices() { return choices; }
    @Override public String getType() { return "MC"; }
    @Override public int getPoints() { return 10; }   // harder format, more points
}
