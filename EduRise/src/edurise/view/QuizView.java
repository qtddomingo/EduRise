package edurise.view;

import edurise.controller.AppController;
import edurise.model.Question;
import edurise.model.Quiz;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

/** Shows one question at a time ("Loop: display question and collect input"). */
public class QuizView extends JPanel {
    private final AppController controller;
    private final Quiz quiz;
    private int index = 0;

    private final JLabel progress = Theme.label(" ", Theme.SMALL, Theme.MUTED);
    private final JLabel question = Theme.label(" ", Theme.HEADING, Theme.TEXT);
    private final JLabel hint = Theme.label(" ", Theme.SMALL, Theme.ERROR);
    private final JPanel choices = new JPanel();
    private final JButton next = Theme.button("Next", true);
    private ButtonGroup group = new ButtonGroup();

    public QuizView(AppController controller, Quiz quiz) {
        this.controller = controller;
        this.quiz = quiz;
        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel body = Theme.page();
        body.setLayout(new BorderLayout(0, 20));

        JPanel top = new JPanel();
        top.setBackground(Theme.BG);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(Theme.left(progress));
        top.add(javax.swing.Box.createVerticalStrut(12));
        top.add(Theme.left(question));

        choices.setBackground(Theme.BG);
        choices.setLayout(new BoxLayout(choices, BoxLayout.Y_AXIS));

        JButton quit = Theme.button("Quit quiz", false);
        quit.addActionListener(e -> controller.showDashboard());
        next.addActionListener(e -> onNext());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Theme.BG);
        bottom.add(quit, BorderLayout.WEST);
        bottom.add(hint, BorderLayout.CENTER);
        bottom.add(next, BorderLayout.EAST);

        body.add(top, BorderLayout.NORTH);
        body.add(choices, BorderLayout.CENTER);
        body.add(bottom, BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        showQuestion();
    }

    private void showQuestion() {
        Question q = quiz.getQuestions().get(index);
        progress.setText(quiz.getTopic() + " - Stage " + quiz.getStage()
                + "   |   Question " + (index + 1) + " of " + quiz.size());
        question.setText("<html><body style='width:440px'>" + q.getText() + "</body></html>");
        hint.setText(" ");
        next.setText(index == quiz.size() - 1 ? "Submit" : "Next");

        choices.removeAll();
        group = new ButtonGroup();
        for (String choice : q.getChoices()) {
            JRadioButton r = new JRadioButton(choice);
            r.setActionCommand(choice);
            r.setFont(Theme.BASE);
            r.setBackground(Theme.BG);
            r.setForeground(Theme.TEXT);
            r.setFocusPainted(false);
            r.setBorder(new EmptyBorder(6, 0, 6, 0));
            group.add(r);
            choices.add(Theme.left(r));
        }
        choices.revalidate();
        choices.repaint();
    }

    private void onNext() {
        if (group.getSelection() == null) {
            hint.setText("  Pick an answer first.");
            return;
        }
        quiz.setAnswer(index, group.getSelection().getActionCommand());
        if (index == quiz.size() - 1) {
            controller.submitQuiz(quiz);       // "Submit assessment"
        } else {
            index++;
            showQuestion();
        }
    }
}
