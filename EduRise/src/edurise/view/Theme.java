package edurise.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/** One place for the minimalist look: white background, one accent color, thin lines. */
public final class Theme {
    public static final Color BG = Color.WHITE;
    public static final Color TEXT = new Color(0x1F2937);
    public static final Color MUTED = new Color(0x6B7280);
    public static final Color ACCENT = new Color(0x2563EB);
    public static final Color LINE = new Color(0xE5E7EB);
    public static final Color ERROR = new Color(0xDC2626);

    public static final Font BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font BOLD = BASE.deriveFont(Font.BOLD);
    public static final Font SMALL = BASE.deriveFont(12f);
    public static final Font HEADING = BASE.deriveFont(Font.BOLD, 18f);
    public static final Font TITLE = BASE.deriveFont(Font.BOLD, 26f);

    private Theme() { }

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JButton button(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI());          // flat button, ignores the OS look
        b.setFont(BOLD);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBackground(primary ? ACCENT : BG);
        b.setForeground(primary ? Color.WHITE : TEXT);
        b.setBorder(primary
                ? new EmptyBorder(9, 18, 9, 18)
                : new CompoundBorder(new LineBorder(LINE), new EmptyBorder(8, 17, 8, 17)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static <T extends JTextField> T field(T f) {
        f.setFont(BASE);
        f.setBorder(new CompoundBorder(new LineBorder(LINE), new EmptyBorder(7, 9, 7, 9)));
        return f;
    }

    public static JPanel page() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(32, 40, 32, 40));
        return p;
    }

    public static <T extends JComponent> T left(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }
}
