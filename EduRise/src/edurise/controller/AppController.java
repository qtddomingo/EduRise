package edurise.controller;

import edurise.data.Database;
import edurise.data.QuestionRepository;
import edurise.data.ResultRepository;
import edurise.data.UserRepository;
import edurise.model.Question;
import edurise.model.Quiz;
import edurise.model.QuizResult;
import edurise.model.StageInfo;
import edurise.model.User;
import edurise.view.DashboardView;
import edurise.view.LoginView;
import edurise.view.QuizView;
import edurise.view.ResultView;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Connects the views to the model and the database; follows the system flowchart. */
public class AppController {
    public static final int STAGES = 2;          // stages per topic
    public static final int PASS_PERCENT = 70;   // needed to unlock the next stage

    private final JFrame frame = new JFrame("EduRise");
    private final UserRepository users = new UserRepository();
    private final QuestionRepository questions = new QuestionRepository();
    private final ResultRepository results = new ResultRepository();
    private User current;

    public void start() {
        Database.init();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(760, 560));
        showLogin();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void show(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }

    public void showLogin() { show(new LoginView(this)); }

    public void logout() {
        current = null;
        showLogin();
    }

    // ---- Registration and login ----

    /** Returns an error message, or null when the account was saved via JDBC. */
    public String register(String username, String fullName, String password) {
        username = username.trim();
        if (username.isEmpty() || fullName.isBlank() || password.length() < 4) {
            return "Fill in all fields (password: 4+ characters).";
        }
        if (users.findByUsername(username).isPresent()) return "Username is already taken.";
        users.save(new User(0, username, fullName.trim(), User.hash(username, password)));
        return null;
    }

    /** Returns an error message, or null when the credentials are valid. */
    public String login(String username, String password) {
        Optional<User> found = users.findByUsername(username.trim());
        if (found.isEmpty() || !found.get().getPasswordHash().equals(User.hash(username.trim(), password))) {
            return "Login failed: invalid username or password.";
        }
        current = found.get();
        showDashboard();
        return null;
    }

    // ---- Dashboard ----

    public void showDashboard() {
        List<QuizResult> history = results.findByUser(current.getId());
        int totalPoints = history.stream().mapToInt(QuizResult::getScore).sum();

        Map<String, List<StageInfo>> topics = new LinkedHashMap<>();
        for (String topic : questions.findTopics()) {
            List<StageInfo> stages = new ArrayList<>();
            boolean unlocked = true;                       // stage 1 is always open
            for (int s = 1; s <= STAGES; s++) {
                int best = bestPercent(history, topic, s);
                stages.add(new StageInfo(s, best, unlocked));
                unlocked = best >= PASS_PERCENT;           // next stage opens only after passing this one
            }
            topics.put(topic, stages);
        }
        show(new DashboardView(this, current, totalPoints, topics));
    }

    private int bestPercent(List<QuizResult> history, String topic, int stage) {
        int best = -1;
        for (QuizResult r : history) {
            if (r.getTopic().equals(topic) && r.getStage() == stage) best = Math.max(best, r.getPercent());
        }
        return best;
    }

    // ---- Quiz ----

    /** Loads the questions from the database; each row becomes an MC or True/False object. */
    public void startQuiz(String topic, int stage) {
        List<Question> list = new ArrayList<>(questions.findByTopicStage(topic, stage));
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No questions available yet.");
            return;
        }
        Collections.shuffle(list);
        show(new QuizView(this, new Quiz(topic, stage, list)));
    }

    /** Grades through the Gradable contract, saves the result via JDBC, then shows it. */
    public void submitQuiz(Quiz quiz) {
        QuizResult result = quiz.grade(current.getId());
        results.save(result);

        boolean passed = result.getPercent() >= PASS_PERCENT;
        String note = (passed && quiz.getStage() < STAGES)
                ? "Stage " + (quiz.getStage() + 1) + " is now unlocked." : null;
        show(new ResultView(this, result, passed, note));
    }

    // ---- Offline backup (serialization) ----

    /** Writes the student's whole quiz history into a local .ser file. */
    public void backup() {
        File file = new File("backup_" + current.getUsername() + ".ser");
        ArrayList<QuizResult> history = new ArrayList<>(results.findByUser(current.getId()));
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(history);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Backup failed: " + e.getMessage());
            return;
        }
        JOptionPane.showMessageDialog(frame, "Backup saved to:\n" + file.getAbsolutePath());
    }
}
