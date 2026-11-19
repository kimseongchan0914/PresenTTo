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
 * 화면을 예쁘게 만들기 위한 부품(둥근 패널 / 둥근 버튼 / 선택 칩 / 이모지 그림)은
 * 아래쪽에 Swing 클래스를 상속한 작은 클래스로 만들었습니다.
 */
public class Main extends JFrame implements ActionListener {

    // 선택지
    private static final String[] TARGETS = {"친구", "연인", "부모님", "형제자매", "선생님"};
    private static final String[] BUDGET_NAMES = {"1만원 이하", "3만원 이하", "5만원 이하", "상관없음"};
    private static final int[] BUDGET_MAX = {10000, 30000, 50000, Integer.MAX_VALUE};
    private static final int CHIP_WIDTH = 82;   // 받는 사람 / 예산 칩 너비

    // 색깔 모음
    static final Color BG = new Color(253, 248, 245);          // 배경 (따뜻한 크림색)
    static final Color ACCENT = new Color(240, 84, 100);       // 메인 코랄 (기념일 / 추천 버튼)
    static final Color ACCENT_LIGHT = new Color(255, 234, 236);// 연한 코랄
    static final Color ACCENT_DARK = new Color(214, 60, 80);   // 진한 코랄 (마우스 올렸을 때)
    static final Color VIOLET = new Color(118, 94, 232);       // 받는 사람 줄 색
    static final Color TEAL = new Color(18, 158, 132);         // 예산 줄 색
    static final Color GREEN = new Color(31, 168, 108);        // 마음 선물 색
    static final Color GREEN_LIGHT = new Color(222, 247, 234);
    static final Color BLUE = new Color(52, 128, 240);         // 모바일 선물 색
    static final Color BLUE_LIGHT = new Color(226, 238, 255);
    static final Color PURPLE = new Color(136, 90, 240);       // 체험 선물 색
    static final Color PURPLE_LIGHT = new Color(240, 233, 255);
    static final Color TEXT = new Color(34, 34, 38);           // 기본 글자
    static final Color GRAY = new Color(122, 122, 130);        // 회색 글자
    static final Color LINE = new Color(238, 230, 226);        // 테두리
    static final Color HOVER = new Color(246, 242, 240);       // 선택 안 된 칩 배경 (연한 회색)
    static final String EMOJI_FONT = "Apple Color Emoji";      // 맥 컬러 이모지 글꼴
    static final int RADIUS = 10;                              // 칩 / 버튼 / 입력 칸 모서리 둥글기
    static final String FONT = "Apple SD Gothic Neo";          // 맥 한글 글꼴

    private Anniversary[] anniversaries = Anniversaries.getAll();
    private Chip[] occasionChips = new Chip[anniversaries.length];
    private Chip[] targetChips = new Chip[TARGETS.length];
    private Chip[] budgetChips = new Chip[BUDGET_NAMES.length];

    private RoundButton recommendButton = new RoundButton("다른 추천 보기", ACCENT, ACCENT_DARK, Color.WHITE);
    private JLabel conditionLabel = new JLabel(" ");
    private JLabel messageLabel = new JLabel(" ");

    private JPanel cardPanel = new JPanel(new GridLayout(1, GiftRecommender.COUNT, 14, 0));   // 선물 카드 5장이 들어갈 곳

    private GiftRecommender recommender = new GiftRecommender();

    // 지금 화면에 보이는 카드 정보
    private ArrayList<Gift> shownGifts = new ArrayList<>();
    private Anniversary shownAnniversary;
    private String shownTarget;

    public Main() {
        setTitle("준비된 선물");
        setSize(1100, 800);   // 13인치 노트북 화면에도 들어가는 크기
        setMinimumSize(new Dimension(1080, 760));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel body = new JPanel(new BorderLayout(0, 22));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(22, 24, 22, 24));
        body.add(makeSelectBox(), BorderLayout.NORTH);
        body.add(makeResultArea(), BorderLayout.CENTER);

        JPanel main = new JPanel(new BorderLayout());
        main.add(new Header(), BorderLayout.NORTH);
        main.add(body, BorderLayout.CENTER);
        add(main);

        recommend();   // 처음부터 추천 결과를 보여줌
    }

    // ==================== 화면 만들기 ====================

    /** 조건 고르는 흰색 상자 (기념일 / 받는 사람 / 예산 + 버튼) */
    private JPanel makeSelectBox() {
        String[] occasionNames = new String[anniversaries.length];
        for (int i = 0; i < anniversaries.length; i++) {
            occasionNames[i] = anniversaries[i].getName();
        }

        // 줄마다 높이가 달라서 GridLayout 대신 위에서 아래로 쌓는 BoxLayout 사용
        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setOpaque(false);
        rows.add(makeChipRow("📅  기념일", occasionNames, occasionChips, 7, ACCENT));
        rows.add(Box.createVerticalStrut(18));
        rows.add(makeChipRow("👥  받는 사람", TARGETS, targetChips, 0, VIOLET));
        rows.add(Box.createVerticalStrut(18));
        JPanel budgetRow = makeChipRow("💰  예산", BUDGET_NAMES, budgetChips, 0, TEAL);
        rows.add(budgetRow);

        recommendButton.addActionListener(this);   // 클릭하면 actionPerformed()가 실행됨

        // 버튼은 예산 줄 오른쪽 빈자리에 (세로 공간 아끼기)
        recommendButton.setPreferredSize(new Dimension(132, 34));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(recommendButton);
        budgetRow.add(buttons, BorderLayout.EAST);

        RoundPanel box = new RoundPanel(Color.WHITE, 16, true);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(22, 26, 22, 26));
        box.add(rows, BorderLayout.CENTER);
        return box;
    }

    /**
     * "기념일 [생일] [크리스마스] ..." 처럼 선택 칩 한 줄 만들기
     * columns가 0이면 칩을 같은 너비로 왼쪽부터 이어 붙이고, 0보다 크면 그 개수만큼씩 칸을 나눠 배치
     * color: 이 줄에서 선택된 칩의 색
     */
    private JPanel makeChipRow(String title, String[] names, Chip[] chips, int columns, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(100, 34));
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setBorder(new EmptyBorder(8, 0, 0, 0));   // 첫 줄 칩 글자와 높이 맞추기

        JPanel chipLine = new JPanel();
        if (columns == 0) {
            chipLine.setLayout(new BoxLayout(chipLine, BoxLayout.X_AXIS));   // 왼쪽부터 차례로
        } else {
            chipLine.setLayout(new GridLayout(0, columns, 8, 8));            // columns개씩 줄바꿈
        }
        chipLine.setOpaque(false);
        ButtonGroup group = new ButtonGroup();   // 한 줄에서 하나만 선택되게
        for (int i = 0; i < names.length; i++) {
            chips[i] = new Chip(names[i]);
            chips[i].setColor(color);
            group.add(chips[i]);
            if (columns == 0) {
                chips[i].setFixedWidth(CHIP_WIDTH);   // 칩 너비를 똑같이 맞춤
                if (i > 0) {
                    chipLine.add(Box.createHorizontalStrut(8));
                }
            }
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
        conditionLabel.setFont(new Font(FONT, Font.BOLD, 17));
        conditionLabel.setForeground(TEXT);
        messageLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        messageLabel.setForeground(GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        textPanel.add(conditionLabel);
        textPanel.add(messageLabel);

        cardPanel.setOpaque(false);

        JPanel result = new JPanel(new BorderLayout(0, 16));
        result.setOpaque(false);
        result.add(textPanel, BorderLayout.NORTH);
        result.add(cardPanel, BorderLayout.CENTER);
        return result;
    }

    /**
     * 선물 한 개를 보여주는 카드 만들기
     *  ┌──────────────────┐
     *  │ [종류 태그]         │  ← 연한 색 그림 영역 (남는 높이를 채움)
     *  │      선물 그림      │
     *  └──────────────────┘
     *   선물 이름
     *   추천 이유 / 설명
     *   ───────── 가격
     */
    private JPanel makeGiftCard(Gift gift, Anniversary anniversary) {
        // 다형성: 선물 종류(일반 / 마음 / 모바일 / 체험)마다 다른 색과 태그가 나옴 → if문 없이 처리
        Color main = gift.getColor();
        Color light = gift.getLightColor();
        String tagText = gift.getTag();
        boolean fits = gift.fitsOccasion(anniversary.getName());
        if (!fits) {   // 기념일은 안 맞지만 빈자리를 채운 선물은 회색으로
            tagText = "이런 것도 있어요";
            main = GRAY;
            light = HOVER;
        }

        // ----- 위: 그림 영역 (태그 + 선물 그림) -----
        JLabel tag = new JLabel(tagText);
        tag.setFont(new Font(FONT, Font.BOLD, 11));
        tag.setForeground(Color.WHITE);
        RoundPanel tagBox = new RoundPanel(main, 8, false);   // 종류 색으로 채운 태그
        tagBox.setLayout(new BorderLayout());
        tagBox.setBorder(new EmptyBorder(3, 8, 3, 8));
        tagBox.add(tag);
        JPanel tagLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagLine.setOpaque(false);
        tagLine.add(tagBox);

        // 종류 색 그라데이션 배경 + 가운데 큰 이모지 그림
        GradientPanel thumb = new GradientPanel(light, mix(light, main, 0.22), 12);
        thumb.setLayout(new BorderLayout());
        thumb.setBorder(new EmptyBorder(8, 8, 8, 6));
        thumb.add(tagLine, BorderLayout.NORTH);
        thumb.add(new EmojiPicture(gift.getEmoji(), 64), BorderLayout.CENTER);

        // ----- 아래: 이름 + 추천 이유 + 설명 + 가격 (카드가 좁으니 width를 정해서 길면 줄바꿈, 스윙은 px를 조금 크게 계산해서 125px) -----
        JLabel nameLabel = new JLabel("<html><div style='width:125px'>" + gift.getName() + "</div></html>");
        nameLabel.setFont(new Font(FONT, Font.BOLD, 16));
        nameLabel.setForeground(TEXT);

        String reason = gift.getReason(anniversary.getName(), shownTarget);
        String reasonColor = String.format("#%06X", main.getRGB() & 0xFFFFFF);   // Color → "#3478DC"
        JLabel descLabel = new JLabel("<html><div style='width:125px; line-height:1.45'>"
                + "<span style='color:" + reasonColor + "'><b>" + reason + "</b></span><br>"
                + gift.getDescription() + "</div></html>");
        descLabel.setFont(new Font(FONT, Font.PLAIN, 12));
        descLabel.setForeground(GRAY);
        descLabel.setVerticalAlignment(SwingConstants.TOP);

        // 다형성: 종류마다 가격 글자가 다름 ("9,500원 · 바로 전송"처럼 길면 " · " 뒤를 아랫줄에 작게)
        String priceText = gift.getPriceText();
        int dot = priceText.indexOf(" · ");
        if (dot >= 0) {
            priceText = priceText.substring(0, dot) + "<br><font size='-1'>" + priceText.substring(dot + 3) + "</font>";
        }
        JLabel priceLabel = new JLabel("<html>" + priceText + "</html>");
        priceLabel.setFont(new Font(FONT, Font.BOLD, 14));
        priceLabel.setForeground(main);

        JPanel info = new JPanel(new BorderLayout(0, 6));
        info.setOpaque(false);
        info.add(nameLabel, BorderLayout.NORTH);
        info.add(descLabel, BorderLayout.CENTER);
        // 글 길이와 상관없이 글 영역 높이를 같게 → 카드마다 그림 영역 높이가 똑같아짐
        info.setPreferredSize(new Dimension(0, 88));
        JPanel infoWithPrice = new JPanel(new BorderLayout(0, 8));
        infoWithPrice.setOpaque(false);
        infoWithPrice.add(info, BorderLayout.CENTER);

        // 맨 아래 한 줄: 가격
        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(10, 0, 0, 0)));
        bottom.add(priceLabel, BorderLayout.CENTER);
        bottom.setPreferredSize(new Dimension(0, 52));   // 가격이 두 줄이어도 카드 높이가 같게
        infoWithPrice.add(bottom, BorderLayout.SOUTH);

        RoundPanel card = new RoundPanel(Color.WHITE, 16, true);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(8, 8, 14, 8));
        card.add(thumb, BorderLayout.CENTER);
        JPanel infoPad = new JPanel(new BorderLayout());
        infoPad.setOpaque(false);
        infoPad.setBorder(new EmptyBorder(0, 6, 0, 6));
        infoPad.add(infoWithPrice);
        card.add(infoPad, BorderLayout.SOUTH);
        return card;
    }

    // ==================== 버튼 클릭 처리 ====================

    /** 버튼을 눌렀을 때 실행 (ActionListener 인터페이스의 메소드) */
    @Override
    public void actionPerformed(ActionEvent e) {
        // [선물 추천받기] 버튼을 누르면 추천
        recommend();
    }

    /** [선물 추천받기] 버튼 */
    private void recommend() {
        // 선택된 칩 찾기
        Anniversary anniversary = anniversaries[selectedIndex(occasionChips)];
        String target = TARGETS[selectedIndex(targetChips)];

        int budget = selectedIndex(budgetChips);
        int maxPrice = BUDGET_MAX[budget];
        String budgetName = BUDGET_NAMES[budget];

        ArrayList<Gift> gifts = recommender.recommend(anniversary, target, maxPrice);

        // 선택한 조건과 응원 문구 보여주기
        conditionLabel.setText("🎁  " + target + "에게 주는 " + anniversary.getName() + " 선물  ·  " + budgetName);
        String message = anniversary.getMessage();   // 다형성: 기념일마다 다른 문구가 나옴
        if (target.equals("선생님")) {
            message = "청탁금지법 때문에 선생님께는 물건 대신 마음 선물을 추천해요.";
        }
        if (gifts.isEmpty()) {
            message = "조건에 맞는 선물이 없어요. 예산을 늘려 보세요!";
        }
        messageLabel.setText(message);

        shownGifts = gifts;
        shownAnniversary = anniversary;
        shownTarget = target;
        showCards();
    }

    /** 선물 카드 다시 채우기 */
    private void showCards() {
        cardPanel.removeAll();
        for (int i = 0; i < shownGifts.size(); i++) {
            cardPanel.add(makeGiftCard(shownGifts.get(i), shownAnniversary));
        }
        cardPanel.revalidate();   // 바뀐 화면 다시 그리기
        cardPanel.repaint();
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

    /** 두 색을 섞기 (t = 0이면 a, 1이면 b) */
    static Color mix(Color a, Color b, double t) {
        return new Color(
                (int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    /** 프로그램 시작점 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    // ==================== 화면 꾸미기용 부품 클래스 ====================

    /** 둥근 모서리 패널 (JPanel 상속) - border가 true면 얇은 테두리와 아주 옅은 그림자도 그림 */
    static class RoundPanel extends JPanel {
        private Color fill;
        private int radius;
        private boolean border;

        RoundPanel(Color fill, int radius, boolean border) {
            this.fill = fill;
            this.radius = radius;
            this.border = border;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - (border ? 3 : 1);
            if (border) {   // 아래쪽으로 1~2px 옅은 그림자
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(0, 2, w, h, radius, radius);
            }
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            if (border) {
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, w, h, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** 둥근 버튼 (JButton 상속) - 마우스를 올리면 색이 바뀜, 흰 버튼은 회색 테두리 */
    static class RoundButton extends JButton {
        private Color normal, hover, text;
        private boolean mouseOver = false;

        RoundButton(String label, Color normal, Color hover, Color text) {
            super(label);
            this.normal = normal;
            this.hover = hover;
            this.text = text;
            setFont(new Font(FONT, Font.BOLD, 14));
            setForeground(text);
            setPreferredSize(new Dimension(140, 34));
            setBorder(new EmptyBorder(0, 8, 0, 8));   // 기본 버튼 여백 대신 좁은 여백 (글자가 잘리지 않게)
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
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            g2.setColor(mouseOver ? hover : normal);
            g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
            if (normal.equals(Color.WHITE)) {   // 흰 버튼만 테두리
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, w, h, RADIUS, RADIUS);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * 선택 칩 (JToggleButton 상속) - 선택되면 줄 색으로 채워짐
     */
    static class Chip extends JToggleButton {
        private String title;
        private Color color = ACCENT;    // 선택됐을 때 색 (줄마다 다름)

        Chip(String title) {
            this.title = title;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(0, 8, 0, 8));   // 기본 버튼 여백 대신 좁은 여백 (글자가 잘리지 않게)
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            updateLook();
            setPreferredSize(new Dimension(getPreferredSize().width + 8, 34));
            addItemListener(e -> updateLook());   // 선택 상태가 바뀔 때마다 모양 갱신
        }

        /** BoxLayout 안에서 너비를 고정 */
        void setFixedWidth(int width) {
            Dimension d = new Dimension(width, getPreferredSize().height);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
        }

        /** 선택됐을 때 색 바꾸기 */
        void setColor(Color color) {
            this.color = color;
            repaint();
        }

        /** 선택 상태에 따라 글자색 / 굵기 변경 */
        private void updateLook() {
            boolean on = isSelected();
            setForeground(on ? Color.WHITE : TEXT);
            setFont(new Font(FONT, on ? Font.BOLD : Font.PLAIN, 13));
            setText(title);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            if (isSelected()) {
                g2.setColor(color);
                g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
            } else {   // 테두리 없이 연한 배경만 (마우스를 올리면 줄 색이 살짝 비침)
                g2.setColor(getModel().isRollover() ? mix(Color.WHITE, color, 0.12) : HOVER);
                g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** 둥근 그라데이션 패널 (JPanel 상속) - 왼쪽 위 → 오른쪽 아래로 색이 진해짐 */
    static class GradientPanel extends JPanel {
        private Color from, to;
        private int radius;

        GradientPanel(Color from, Color to, int radius) {
            this.from = from;
            this.to = to;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, from, getWidth(), getHeight(), to));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * 이모지 그림 (JComponent 상속)
     *
     * 맥의 자바는 100픽셀보다 큰 컬러 이모지를 그리지 못하고 빈칸으로 남깁니다.
     * (레티나 화면은 2배로 그려서 54 크기만 돼도 108픽셀이 되어 사라짐)
     * 그래서 이모지를 96픽셀 크기로 한 번 그림(BufferedImage)으로 만들어 두고,
     * 그 그림을 원하는 크기로 줄이거나 늘려서 보여줍니다.
     */
    static class EmojiPicture extends JComponent {
        private static final int SOURCE = 96;   // 미리 그려 두는 크기 (100픽셀 제한보다 작게)
        private static final java.util.HashMap<String, java.awt.image.BufferedImage> cache = new java.util.HashMap<>();

        private java.awt.image.BufferedImage image;
        private int size;

        EmojiPicture(String emoji, int size) {
            this.size = size;
            this.image = makeImage(emoji);
            setPreferredSize(new Dimension(size, size));
        }

        /** 이모지를 그림으로 만들기 (같은 이모지는 한 번만 만들고 재사용) */
        private static java.awt.image.BufferedImage makeImage(String emoji) {
            if (cache.containsKey(emoji)) {
                return cache.get(emoji);
            }
            int box = SOURCE + 24;   // 이모지가 잘리지 않게 여유 공간
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(box, box, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font(EMOJI_FONT, Font.PLAIN, SOURCE));
            FontMetrics fm = g2.getFontMetrics();
            int x = (box - fm.stringWidth(emoji)) / 2;
            int y = (box + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(emoji, x, y);
            g2.dispose();
            cache.put(emoji, img);
            return img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int fit = Math.min(size, Math.min(getWidth(), getHeight()) - 8);   // 자리가 좁으면 그만큼 작게
            if (fit <= 0) {
                g2.dispose();
                return;
            }
            int draw = fit * image.getWidth() / SOURCE;   // 여유 공간까지 같은 비율로
            int x = (getWidth() - draw) / 2;
            int y = (getHeight() - draw) / 2;
            g2.drawImage(image, x, y, draw, draw, null);
            g2.dispose();
        }
    }

    /** 맨 위 제목 띠 (JPanel 상속) - 코랄 → 복숭아색 그라데이션 */
    static class Header extends JPanel {
        Header() {
            setLayout(new BorderLayout(10, 0));
            setPreferredSize(new Dimension(0, 64));
            setBorder(new EmptyBorder(0, 24, 0, 24));

            EmojiPicture icon = new EmojiPicture("🎁", 30);
            JLabel title = new JLabel("준비된 선물");
            title.setFont(new Font(FONT, Font.BOLD, 20));
            title.setForeground(Color.WHITE);
            JLabel subtitle = new JLabel("기념일과 받는 사람과 예산만 고르면 딱 맞는 선물을 골라 드려요");
            subtitle.setFont(new Font(FONT, Font.PLAIN, 13));
            subtitle.setForeground(new Color(255, 236, 236));

            JPanel texts = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            texts.setOpaque(false);
            texts.add(icon);
            texts.add(title);
            texts.add(subtitle);
            JPanel textBox = new JPanel(new GridBagLayout());   // 세로 가운데
            textBox.setOpaque(false);
            textBox.add(texts);
            add(textBox, BorderLayout.WEST);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, new Color(240, 84, 100), getWidth(), 0, new Color(255, 150, 112)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

}