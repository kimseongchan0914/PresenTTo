import javax.swing.*;
import java.awt.*;

/**
 * 모서리가 둥근 버튼 (마우스를 올리면 색이 진해짐)
 *
 * [상속] JButton을 상속하고 paintComponent()를 오버라이딩해서 모양을 바꿨습니다.
 */
public class RoundButton extends JButton {
    private Color color;

    public RoundButton(String text, Color color) {
        super(text);
        this.color = color;
        setFont(PlantView.font(16));
        setPreferredSize(new Dimension(118, 48));
        setBorderPainted(false);
        setRolloverEnabled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // 버튼 모양을 직접 그림 (오버라이딩)
    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getParent().getBackground());   // 모서리 바깥은 배경색
        g.fillRect(0, 0, getWidth(), getHeight());
        if (getModel().isRollover()) {
            g.setColor(color.darker());
        } else {
            g.setColor(color);
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

        g.setColor(Color.WHITE);   // 글자를 가운데에
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        g.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
    }
}
