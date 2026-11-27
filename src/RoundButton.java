import javax.swing.JButton;
import java.awt.*;

// 모서리가 둥근 버튼 (마우스를 올리면 색이 진해짐)
public class RoundButton extends JButton {
    private Color color;

    public RoundButton(String text, Color color) {
        super(text);
        this.color = color;
        setFont(PlantView.font(16));
        setPreferredSize(new Dimension(118, 48));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // 버튼 모양을 직접 그림 (오버라이딩)
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g.setColor(color.darker().darker());
        } else if (getModel().isRollover()) {
            g.setColor(color.darker());
        } else {
            g.setColor(color);
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

        // 글자를 가운데에
        g.setColor(Color.WHITE);
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(getText())) / 2;
        int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(getText(), tx, ty);
    }
}
