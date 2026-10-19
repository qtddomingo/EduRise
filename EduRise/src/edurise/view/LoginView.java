package edurise.view;

import edurise.controller.AppController;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Login screen that can switch to registration ("Already a member?" in the flowchart). */
public class LoginView extends JPanel {
    private final JTextField username = Theme.field(new JTextField());
    private final JPasswordField password = Theme.field(new JPasswordField());
    private final JTextField fullName = Theme.field(new JTextField());
    private final JLabel fullNameLabel = Theme.label("Full name", Theme.SMALL, Theme.MUTED);
    private final JLabel message = Theme.label(" ", Theme.SMALL, Theme.ERROR);
    private final JButton submit = Theme.button("Log in", true);
    private final JButton toggle = Theme.button("Create an account", false);
    private boolean registerMode = false;

    public LoginView(AppController controller) {
        setBackground(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setBackground(Theme.BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(300, 400));

        form.add(Theme.left(Theme.label("EduRise", Theme.TITLE, Theme.ACCENT)));
        form.add(Theme.left(Theme.label("Short quizzes. Steady progress.", Theme.BASE, Theme.MUTED)));
        form.add(Box.createVerticalStrut(28));

        form.add(Theme.left(fullNameLabel));
        form.add(Theme.left(sized(fullName)));
        form.add(Box.createVerticalStrut(10));
        form.add(Theme.left(Theme.label("Username", Theme.SMALL, Theme.MUTED)));
        form.add(Theme.left(sized(username)));
        form.add(Box.createVerticalStrut(10));
        form.add(Theme.left(Theme.label("Password", Theme.SMALL, Theme.MUTED)));
        form.add(Theme.left(sized(password)));
        form.add(Box.createVerticalStrut(8));
        form.add(Theme.left(message));
        form.add(Box.createVerticalStrut(8));

        JPanel buttons = new JPanel();
        buttons.setBackground(Theme.BG);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(submit);
        buttons.add(Box.createHorizontalStrut(8));
        buttons.add(toggle);
        form.add(Theme.left(buttons));
        form.add(Box.createVerticalStrut(16));

        JButton exit = Theme.button("Exit", false);
        exit.addActionListener(e -> System.exit(0));        // "Exit" branch of the flowchart
        form.add(Theme.left(exit));
        add(form);

        submit.addActionListener(e -> {
            String pw = new String(password.getPassword());
            String error = registerMode
                    ? controller.register(username.getText(), fullName.getText(), pw)
                    : controller.login(username.getText(), pw);
            if (error != null) {
                show(error, true);                           // login failed / invalid input
            } else if (registerMode) {
                setMode(false);                              // registration saved, go back to login
                password.setText("");
                show("Account created. Please log in.", false);
            }
        });
        password.addActionListener(e -> submit.doClick());
        toggle.addActionListener(e -> setMode(!registerMode));
        setMode(false);
    }

    private <T extends javax.swing.JComponent> T sized(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return c;
    }

    private void setMode(boolean register) {
        registerMode = register;
        fullName.setVisible(register);
        fullNameLabel.setVisible(register);
        submit.setText(register ? "Register" : "Log in");
        toggle.setText(register ? "Back to login" : "Create an account");
        show(" ", false);
    }

    private void show(String text, boolean error) {
        message.setText(text);
        message.setForeground(error ? Theme.ERROR : Theme.MUTED);
    }
}
