import javax.swing.*;
import java.awt.*;

/**
 * 모서리가 둥근 버튼 (마우스를 올리면 색이 진해짐)
 *
 * [상속] JButton을 상속하고 paintComponent()를 오버라이딩해서 모양을 바꿨습니다.
 */
public class RoundButton extends JButton {

    private Color color;   // 버튼 색

    public RoundButton(String text, Color color) {
        super(text);
        this.color = color;
        setFont(new Font("Apple SD Gothic Neo", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(118, 48));
        setContentAreaFilled(false);   // 원래 버튼 배경은 그리지 않기
        setBorderPainted(false);       // 원래 버튼 테두리도 그리지 않기
        setFocusPainted(false);
        setRolloverEnabled(true);      // 마우스를 올린 걸 알아차리게 하기
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isRollover()) {
            g.setColor(color.darker());   // 마우스를 올리면 진한 색
        } else {
            g.setColor(color);
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
        super.paintComponent(g);          // 글자는 원래 버튼이 그려 줌
    }
}
