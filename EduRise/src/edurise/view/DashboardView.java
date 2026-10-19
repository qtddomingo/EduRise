package edurise.view;

import edurise.controller.AppController;
import edurise.model.StageInfo;
import edurise.model.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/** Minimalist dashboard: a greeting, total points, and one line per topic with its stages. */
public class DashboardView extends JPanel {

    public DashboardView(AppController controller, User user, int totalPoints,
                         Map<String, List<StageInfo>> topics) {
        setBackground(Theme.BG);
        setLayout(new BorderLayout());

        JPanel body = Theme.page();
        body.setLayout(new BorderLayout(0, 24));

        // header: who is logged in + backup / log out
        JPanel who = new JPanel();
        who.setBackground(Theme.BG);
        who.setLayout(new BoxLayout(who, BoxLayout.Y_AXIS));
        who.add(Theme.left(Theme.label("Hi, " + user.getFullName(), Theme.TITLE, Theme.TEXT)));
        who.add(Theme.left(Theme.label(totalPoints + " total points", Theme.BASE, Theme.MUTED)));

        JButton backup = Theme.button("Backup (.ser)", false);
        backup.addActionListener(e -> controller.backup());
        JButton logout = Theme.button("Log out", false);
        logout.addActionListener(e -> controller.logout());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG);
        actions.add(backup);
        actions.add(logout);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.add(who, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        // topic list: one thin-lined row per topic
        JPanel list = new JPanel(new GridLayout(0, 1));
        list.setBackground(Theme.BG);
        for (Map.Entry<String, List<StageInfo>> entry : topics.entrySet()) {
            String topic = entry.getKey();

            JPanel stages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            stages.setBackground(Theme.BG);
            for (StageInfo s : entry.getValue()) {
                String text = "Stage " + s.stage();
                if (!s.unlocked()) text += " (locked)";
                else if (s.bestPercent() >= 0) text += " - " + s.bestPercent() + "%";

                JButton b = Theme.button(text, s.unlocked());
                b.setEnabled(s.unlocked());
                int stage = s.stage();
                b.addActionListener(e -> controller.startQuiz(topic, stage));
                stages.add(b);
            }

            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Theme.BG);
            row.setBorder(new CompoundBorder(
                    new MatteBorder(1, 0, 0, 0, Theme.LINE), new EmptyBorder(14, 0, 14, 0)));
            row.add(Theme.label(topic, Theme.HEADING, Theme.TEXT), BorderLayout.WEST);
            row.add(stages, BorderLayout.EAST);
            list.add(row);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG);
        wrapper.add(list, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.BG);

        body.add(header, BorderLayout.NORTH);
        body.add(scroll, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
