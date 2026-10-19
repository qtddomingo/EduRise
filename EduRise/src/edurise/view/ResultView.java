package edurise.view;

import edurise.controller.AppController;
import edurise.model.QuizResult;
import edurise.model.Quiz;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/** Result screen + the "Want offline backup?" decision from the flowchart. */
public class ResultView extends JPanel {

    public ResultView(AppController controller, QuizResult r, boolean passed, String note) {
        setBackground(Theme.BG);
        setLayout(new java.awt.BorderLayout());

        JPanel body = Theme.page();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(Theme.left(Theme.label(r.getTopic() + " - Stage " + r.getStage(), Theme.SMALL, Theme.MUTED)));
        body.add(Box.createVerticalStrut(8));
        body.add(Theme.left(Theme.label(r.getScore() + " points", Theme.TITLE, Theme.ACCENT)));
        body.add(Box.createVerticalStrut(8));
        body.add(Theme.left(Theme.label(r.getCorrect() + " of " + r.getTotal()
                + " correct (" + r.getPercent() + "%) - " + (passed ? "Passed" : "Not yet passed"),
                Theme.HEADING, Theme.TEXT)));
        if (r.isPerfect()) {
            body.add(Box.createVerticalStrut(6));
            body.add(Theme.left(Theme.label("Perfect score bonus: +" + Quiz.PERFECT_BONUS + " points",
                    Theme.BASE, Theme.MUTED)));
        }
        if (note != null) {
            body.add(Box.createVerticalStrut(6));
            body.add(Theme.left(Theme.label(note, Theme.BASE, Theme.MUTED)));
        }
        body.add(Box.createVerticalStrut(32));

        JButton back = Theme.button("Back to dashboard", true);
        back.addActionListener(e -> controller.showDashboard());
        JButton backup = Theme.button("Save offline backup", false);
        backup.addActionListener(e -> controller.backup());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setBackground(Theme.BG);
        buttons.add(back);
        buttons.add(Box.createHorizontalStrut(8));
        buttons.add(backup);
        body.add(Theme.left(buttons));

        add(body, java.awt.BorderLayout.NORTH);
    }
}
