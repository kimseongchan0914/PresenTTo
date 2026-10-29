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

    /** 선택 칩 (JToggleButton 상속) - 선택되면 빨간색으로 채워짐 */
    static class Chip extends JToggleButton {
        Chip(String label) {
            super(label);
            setFont(new Font(FONT, Font.PLAIN, 14));
            setForeground(TEXT);
            setPreferredSize(new Dimension(getPreferredSize().width + 16, 36));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addItemListener(e -> {   // 선택 상태에 따라 글자색 / 굵기 변경
                setForeground(isSelected() ? Color.WHITE : TEXT);
                setFont(new Font(FONT, isSelected() ? Font.BOLD : Font.PLAIN, 14));
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight() - 1;
            if (isSelected()) {
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth() - 1, h, h, h);
            } else {
                g2.setColor(getModel().isRollover() ? ACCENT_LIGHT : BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, h, h, h);
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, h, h, h);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** 선물 상자 그림 (JComponent 상속) - 사각형과 원으로 직접 그림 */
    static class GiftIcon extends JComponent {
        private Color color;
        private int size;

        GiftIcon(Color color, int size) {
            this.color = color;
            this.size = size;
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int s = size;
            int x = (getWidth() - s) / 2;
            int y = (getHeight() - s) / 2;
            Color ribbon = color.darker();

            g2.setColor(color);
            g2.fillRoundRect(x + s / 8, y + s * 7 / 16, s * 3 / 4, s / 2, s / 8, s / 8);   // 상자 몸통
            g2.fillRoundRect(x + s / 16, y + s * 5 / 16, s * 7 / 8, s / 6, s / 8, s / 8);  // 뚜껑
            g2.setColor(ribbon);
            g2.fillRect(x + s * 7 / 16, y + s * 5 / 16, s / 8, s * 5 / 8);                  // 세로 리본
            g2.fillOval(x + s / 4, y + s / 8, s / 4, s / 5);                                // 리본 왼쪽 고리
            g2.fillOval(x + s / 2, y + s / 8, s / 4, s / 5);                                // 리본 오른쪽 고리
            g2.dispose();
        }
    }

}