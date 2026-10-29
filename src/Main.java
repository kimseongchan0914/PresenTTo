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

    private Anniversary[] anniversaries = Anniversaries.getAll();
    private Chip[] occasionChips = new Chip[anniversaries.length];
    private Chip[] targetChips = new Chip[TARGETS.length];
    private Chip[] budgetChips = new Chip[BUDGET_NAMES.length];

    private RoundButton recommendButton = new RoundButton("선물 추천받기", ACCENT, ACCENT_DARK, Color.WHITE);
    private RoundButton againButton = new RoundButton("다른 추천 보기", Color.WHITE, ACCENT_LIGHT, ACCENT);
    private JLabel conditionLabel = new JLabel(" ");
    private JLabel messageLabel = new JLabel(" ");
    private JPanel cardPanel = new JPanel(new GridLayout(1, 3, 16, 0));   // 선물 카드 3장이 들어갈 곳

    private GiftRecommender recommender = new GiftRecommender();

    public Main() {
        setTitle("준비된 선물");
        setSize(840, 720);
        setMinimumSize(new Dimension(780, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(22, 26, 26, 26));
        body.add(makeSelectBox(), BorderLayout.NORTH);
        body.add(makeResultArea(), BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout());
        main.add(new Header(), BorderLayout.NORTH);
        main.add(body, BorderLayout.CENTER);
        add(main);

        showEmpty();
    }

    // ==================== 화면 만들기 ====================

    /** 조건 고르는 흰색 상자 (기념일 / 받는 사람 / 예산 + 버튼) */
    private JPanel makeSelectBox() {
        String[] occasionNames = new String[anniversaries.length];
        for (int i = 0; i < anniversaries.length; i++) {
            occasionNames[i] = anniversaries[i].getName();
        }

        JPanel rows = new JPanel(new GridLayout(3, 1, 0, 12));
        rows.setOpaque(false);
        rows.add(makeChipRow("기념일", occasionNames, occasionChips));
        rows.add(makeChipRow("받는 사람", TARGETS, targetChips));
        rows.add(makeChipRow("예산", BUDGET_NAMES, budgetChips));

        recommendButton.addActionListener(this);
        againButton.addActionListener(this);
        againButton.setEnabled(false);   // 처음에는 추천받기를 먼저 눌러야 함

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(againButton);
        buttons.add(recommendButton);

        RoundPanel box = new RoundPanel(Color.WHITE, 22, true);
        box.setLayout(new BorderLayout(0, 18));
        box.setBorder(new EmptyBorder(20, 22, 22, 22));
        box.add(rows, BorderLayout.CENTER);
        box.add(buttons, BorderLayout.SOUTH);
        return box;
    }

    /** "기념일 [생일] [크리스마스] ..." 처럼 한 줄짜리 선택 칩 만들기 */
    private JPanel makeChipRow(String title, String[] names, Chip[] chips) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 14));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(80, 36));

        JPanel chipLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipLine.setOpaque(false);
        ButtonGroup group = new ButtonGroup();   // 한 줄에서 하나만 선택되게
        for (int i = 0; i < names.length; i++) {
            chips[i] = new Chip(names[i]);
            group.add(chips[i]);
            chipLine.add(chips[i]);
        }
        chips[0].setSelected(true);   // 첫 번째를 기본 선택

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(label, BorderLayout.WEST);
        row.add(chipLine, BorderLayout.CENTER);
        return row;
    }

    /** 결과가 나오는 부분 (조건 + 응원 문구 + 선물 카드) */
    private JPanel makeResultArea() {
        conditionLabel.setFont(new Font(FONT, Font.BOLD, 18));
        conditionLabel.setForeground(TEXT);
        messageLabel.setFont(new Font(FONT, Font.PLAIN, 14));
        messageLabel.setForeground(GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(conditionLabel);
        textPanel.add(messageLabel);

        cardPanel.setOpaque(false);

        JPanel result = new JPanel(new BorderLayout(0, 14));
        result.setOpaque(false);
        result.add(textPanel, BorderLayout.NORTH);
        result.add(cardPanel, BorderLayout.CENTER);
        return result;
    }

    /** 아직 추천받기 전 화면 */
    private void showEmpty() {
        conditionLabel.setText("어떤 선물이 좋을까요?");
        messageLabel.setText("위에서 조건을 고르고 [선물 추천받기]를 눌러 보세요.");
        for (int i = 0; i < 3; i++) {
            RoundPanel card = new RoundPanel(new Color(255, 255, 255, 150), 20, false);
            card.setLayout(new BorderLayout());
            card.add(new GiftIcon(new Color(240, 225, 215), 56), BorderLayout.CENTER);
            cardPanel.add(card);
        }
    }

    /** 선물 한 개를 보여주는 카드 만들기 */
    private JPanel makeGiftCard(Gift gift, int number) {
        boolean handmade = gift instanceof HandmadeGift;   // 마음 선물이면 초록색으로
        Color main = handmade ? GREEN : ACCENT;
        Color light = handmade ? GREEN_LIGHT : ACCENT_LIGHT;

        // 위쪽: 태그 + 선물 아이콘
        JLabel tag = new JLabel(handmade ? "마음 선물" : "추천 " + number);
        tag.setFont(new Font(FONT, Font.BOLD, 12));
        tag.setForeground(main);
        RoundPanel tagBox = new RoundPanel(light, 14, false);
        tagBox.setBorder(new EmptyBorder(4, 10, 4, 10));
        tagBox.add(tag);

        JPanel tagLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagLine.setOpaque(false);
        tagLine.add(tagBox);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(tagLine, BorderLayout.WEST);
        top.add(new GiftIcon(main, 40), BorderLayout.EAST);

        // 가운데: 이름 + 가격
        JLabel nameLabel = new JLabel("<html>" + gift.getName() + "</html>");
        nameLabel.setFont(new Font(FONT, Font.BOLD, 19));
        nameLabel.setForeground(TEXT);

        JLabel priceLabel = new JLabel(gift.getPriceText());   // 다형성: 마음 선물은 가격이 다르게 나옴
        priceLabel.setFont(new Font(FONT, Font.BOLD, 16));
        priceLabel.setForeground(main);

        JPanel middle = new JPanel(new GridLayout(2, 1, 0, 4));
        middle.setOpaque(false);
        middle.add(nameLabel);
        middle.add(priceLabel);

        // 아래: 설명 (html을 쓰면 자동 줄바꿈)
        JLabel descLabel = new JLabel("<html><div style='line-height:1.4'>" + gift.getDescription() + "</div></html>");
        descLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        descLabel.setForeground(GRAY);
        descLabel.setVerticalAlignment(SwingConstants.TOP);
        descLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(12, 0, 0, 0)));

        JPanel info = new JPanel(new BorderLayout(0, 14));
        info.setOpaque(false);
        info.add(middle, BorderLayout.NORTH);
        info.add(descLabel, BorderLayout.CENTER);

        RoundPanel card = new RoundPanel(Color.WHITE, 20, true);
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.add(top, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    // ==================== 버튼 클릭 처리 ====================

    /** 버튼을 눌렀을 때 실행 (ActionListener 인터페이스의 메소드) */
    @Override
    public void actionPerformed(ActionEvent e) {
        // 선택된 칩 찾기
        Anniversary anniversary = anniversaries[selectedIndex(occasionChips)];
        String target = TARGETS[selectedIndex(targetChips)];
        int budget = selectedIndex(budgetChips);

        ArrayList<Gift> gifts = recommender.recommend(anniversary, target, BUDGET_MAX[budget]);

        // 선택한 조건과 응원 문구 보여주기
        conditionLabel.setText(target + "에게 주는 " + anniversary.getName() + " 선물  ·  " + BUDGET_NAMES[budget]);
        String message = anniversary.getMessage();   // 다형성: 기념일마다 다른 문구가 나옴
        if (target.equals("선생님")) {
            message = "청탁금지법 때문에 선생님께는 물건 대신 마음 선물을 추천해요.";
        }
        if (gifts.isEmpty()) {
            message = "조건에 맞는 선물이 없어요. 예산을 늘려 보세요!";
        }
        messageLabel.setText(message);

        // 선물 카드 다시 채우기
        cardPanel.removeAll();
        for (int i = 0; i < gifts.size(); i++) {
            cardPanel.add(makeGiftCard(gifts.get(i), i + 1));
        }
        cardPanel.revalidate();   // 바뀐 화면 다시 그리기
        cardPanel.repaint();

        againButton.setEnabled(true);
    }

    /** 칩 배열에서 선택된 칩의 번호 찾기 */
    private int selectedIndex(Chip[] chips) {
        for (int i = 0; i < chips.length; i++) {
            if (chips[i].isSelected()) {
                return i;
            }
        }
        return 0;
    }

    /** 프로그램 시작점 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

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

    /** 맨 위 그라데이션 제목 띠 (JPanel 상속) */
    static class Header extends JPanel {
        Header() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(0, 120));
            setBorder(new EmptyBorder(24, 30, 24, 30));

            JLabel title = new JLabel("준비된 선물");
            title.setFont(new Font(FONT, Font.BOLD, 30));
            title.setForeground(Color.WHITE);
            JLabel subtitle = new JLabel("기념일과 받는 사람과 예산만 고르면 딱 맞는 선물을 골라 드려요");
            subtitle.setFont(new Font(FONT, Font.PLAIN, 14));
            subtitle.setForeground(new Color(255, 235, 235));

            JPanel texts = new JPanel(new GridLayout(2, 1, 0, 4));
            texts.setOpaque(false);
            texts.add(title);
            texts.add(subtitle);
            add(texts, BorderLayout.CENTER);
            add(new GiftIcon(new Color(255, 255, 255, 230), 64), BorderLayout.EAST);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, new Color(240, 98, 104), getWidth(), getHeight(), new Color(250, 140, 120)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 25));   // 장식용 동그라미
            g2.fillOval(getWidth() - 220, -60, 180, 180);
            g2.fillOval(getWidth() - 120, 50, 120, 120);
            g2.dispose();
        }
    }
}