import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 프로그램 시작 + 화면(창) 클래스
 *
 * JFrame을 상속(extends)해서 창을 만들고
 * ActionListener 인터페이스를 구현(implements)해서 버튼 클릭을 처리합니다.
 * 화면을 예쁘게 만들기 위한 부품(둥근 패널 / 둥근 버튼 / 선택 칩 / 선물 아이콘)은
 * 아래쪽에 Swing 클래스를 상속한 작은 클래스로 만들었습니다.
 */
public class Main extends JFrame implements ActionListener {

    // 선택지
    private static final String[] TARGETS = {"친구", "부모님", "선생님", "형제자매"};
    private static final String[] BUDGET_NAMES = {"1만원 이하", "3만원 이하", "5만원 이하", "상관없음"};
    private static final int[] BUDGET_MAX = {10000, 30000, 50000, Integer.MAX_VALUE};

    // 색깔 모음
    static final Color BG = new Color(255, 248, 243);          // 배경
    static final Color ACCENT = new Color(232, 80, 91);        // 메인 빨강
    static final Color ACCENT_DARK = new Color(201, 55, 68);   // 진한 빨강 (마우스 올렸을 때)
    static final Color ACCENT_LIGHT = new Color(255, 232, 230);// 연한 빨강
    static final Color GREEN = new Color(46, 150, 104);        // 마음 선물 색
    static final Color GREEN_LIGHT = new Color(225, 245, 235);
    static final Color TEXT = new Color(45, 40, 38);           // 기본 글자
    static final Color GRAY = new Color(130, 120, 112);        // 회색 글자
    static final Color LINE = new Color(238, 228, 220);        // 테두리
    static final String FONT = "Apple SD Gothic Neo";          // 맥 한글 글꼴

    // ==================== 화면 꾸미기용 부품 클래스 ====================

    /** 둥근 모서리 패널 (JPanel 상속) - shadow가 true면 은은한 그림자도 그림 */
    static class RoundPanel extends JPanel {
        private Color fill;
        private int radius;
        private boolean shadow;

        RoundPanel(Color fill, int radius, boolean shadow) {
            this.fill = fill;
            this.radius = radius;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 4;
            int h = getHeight() - 5;
            if (shadow) {   // 아래로 살짝 퍼지는 그림자
                for (int i = 1; i <= 4; i++) {
                    g2.setColor(new Color(150, 90, 70, 10));
                    g2.fillRoundRect(2, 1 + i, w, h, radius + i, radius + i);
                }
            }
            g2.setColor(fill);
            g2.fillRoundRect(2, 0, w, h, radius, radius);
            if (shadow) {
                g2.setColor(LINE);
                g2.drawRoundRect(2, 0, w, h, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** 둥근 버튼 (JButton 상속) - 마우스를 올리면 색이 바뀜 */
    static class RoundButton extends JButton {
        private Color normal, hover, text;
        private boolean mouseOver = false;

        RoundButton(String label, Color normal, Color hover, Color text) {
            super(label);
            this.normal = normal;
            this.hover = hover;
            this.text = text;
            setFont(new Font(FONT, Font.BOLD, 15));
            setForeground(text);
            setPreferredSize(new Dimension(160, 46));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { mouseOver = true; repaint(); }
                public void mouseExited(MouseEvent e) { mouseOver = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setForeground(isEnabled() ? text : new Color(200, 190, 185));
            g2.setColor(mouseOver && isEnabled() ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.setColor(isEnabled() ? ACCENT : LINE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

}