package edurise.data;

import edurise.model.MultipleChoiceQuestion;
import edurise.model.Question;
import edurise.model.TrueFalseQuestion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionRepository extends BaseRepository<Question> {

    @Override protected String table() { return "questions"; }

    /** Instantiates the right subclass depending on the stored question type. */
    @Override
    protected Question map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String topic = rs.getString("topic");
        int stage = rs.getInt("stage");
        String text = rs.getString("text");
        String answer = rs.getString("answer");
        if ("TF".equals(rs.getString("type"))) {
            return new TrueFalseQuestion(id, topic, stage, text, answer);
        }
        return new MultipleChoiceQuestion(id, topic, stage, text, answer, rs.getString("options").split("\\|"));
    }

    @Override
    public boolean save(Question q) {
        return update("INSERT INTO questions(topic, stage, type, text, options, answer) VALUES(?,?,?,?,?,?)",
                q.getTopic(), q.getStage(), q.getType(), q.getText(),
                String.join("|", q.getChoices()), q.getAnswer());
    }

    public List<Question> findByTopicStage(String topic, int stage) {
        return query("SELECT * FROM questions WHERE topic = ? AND stage = ? ORDER BY id", topic, stage);
    }

    public List<String> findTopics() {
        return findAll().stream().map(Question::getTopic).distinct().toList();
    }

    // ---- sample content, loaded only when the table is empty ----
    public void seedIfEmpty() {
        if (!findAll().isEmpty()) return;
        String oop = "OOP Concepts";
        tf(oop, 1, "A class is a blueprint for creating objects.", true);
        mc(oop, 1, "Which keyword makes a class inherit from another class?", "extends", "extends", "implements", "inherits", "super");
        tf(oop, 1, "An object can exist without any class definition in Java.", false);
        mc(oop, 2, "Which keyword is used to implement an interface?", "implements", "implements", "extends", "interface", "import");
        tf(oop, 2, "Java supports multiple inheritance of classes.", false);
        mc(oop, 2, "Redefining a superclass method in a subclass is called?", "Overriding", "Overloading", "Overriding", "Encapsulation", "Casting");

        String db = "Databases & JDBC";
        mc(db, 1, "Which interface represents a database connection in JDBC?", "Connection", "Connection", "Statement", "ResultSet", "Driver");
        tf(db, 1, "A ResultSet holds the rows returned by a SELECT query.", true);
        mc(db, 1, "Which class opens a Connection from a database URL?", "DriverManager", "DriverManager", "Connection", "PreparedStatement", "Class");
        tf(db, 2, "PreparedStatement helps prevent SQL injection.", true);
        mc(db, 2, "Which SQL command retrieves data from a table?", "SELECT", "SELECT", "INSERT", "UPDATE", "DELETE");
        mc(db, 2, "Which JDBC method runs an INSERT or UPDATE?", "executeUpdate", "executeQuery", "executeUpdate", "fetch", "run");
    }

    private void mc(String topic, int stage, String text, String answer, String... options) {
        save(new MultipleChoiceQuestion(0, topic, stage, text, answer, options));
    }

    private void tf(String topic, int stage, String text, boolean answer) {
        save(new TrueFalseQuestion(0, topic, stage, text, answer ? "True" : "False"));
    }
}
