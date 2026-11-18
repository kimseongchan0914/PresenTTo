import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
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
    private static final String[] TARGETS = {"친구", "연인", "부모님", "조부모님", "형제자매", "선생님"};
    private static final String[] BUDGET_NAMES = {"1만원 이하", "3만원 이하", "5만원 이하", "상관없음"};
    private static final int[] BUDGET_MAX = {10000, 30000, 50000, Integer.MAX_VALUE};

    // 색깔 모음
    static final Color BG = new Color(255, 248, 243);          // 배경
    static final Color ACCENT = new Color(232, 80, 91);        // 메인 빨강
    static final Color ACCENT_DARK = new Color(201, 55, 68);   // 진한 빨강 (마우스 올렸을 때)
    static final Color ACCENT_LIGHT = new Color(255, 232, 230);// 연한 빨강
    static final Color GREEN = new Color(46, 150, 104);        // 마음 선물 색
    static final Color GREEN_LIGHT = new Color(225, 245, 235);
    static final Color BLUE = new Color(52, 120, 220);         // 모바일 선물 색
    static final Color BLUE_LIGHT = new Color(228, 238, 252);
    static final Color TEXT = new Color(45, 40, 38);           // 기본 글자
    static final Color GRAY = new Color(130, 120, 112);        // 회색 글자
    static final Color LINE = new Color(238, 228, 220);        // 테두리
    static final String FONT = "Apple SD Gothic Neo";          // 맥 한글 글꼴

    private Anniversary[] anniversaries = Anniversaries.getAll();
    private Chip[] occasionChips = new Chip[anniversaries.length];
    private Chip[] targetChips = new Chip[TARGETS.length];
    private Chip[] budgetChips = new Chip[BUDGET_NAMES.length];

    private RoundButton recommendButton = new RoundButton("선물 추천받기", ACCENT, ACCENT_DARK, Color.WHITE);
    private RoundButton wishButton = new RoundButton("찜 목록 (0)", Color.WHITE, ACCENT_LIGHT, ACCENT);
    private JLabel conditionLabel = new JLabel(" ");
    private JLabel messageLabel = new JLabel(" ");
    private JPanel cardPanel = new JPanel(new GridLayout(1, 3, 16, 0));   // 선물 카드 3장이 들어갈 곳

    private GiftRecommender recommender = new GiftRecommender();
    private Wishlist wishlist = new Wishlist();

    // 지금 화면에 보이는 카드 정보 (찜 목록을 닫은 뒤 하트를 다시 그릴 때 사용)
    private ArrayList<Gift> shownGifts = new ArrayList<>();
    private Anniversary shownAnniversary;

    public Main() {
        setTitle("준비된 선물");
        setSize(880, 820);
        setMinimumSize(new Dimension(840, 780));
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
        String[] occasionDates = new String[anniversaries.length];
        for (int i = 0; i < anniversaries.length; i++) {
            occasionNames[i] = anniversaries[i].getName();
            occasionDates[i] = anniversaries[i].getDate();
            String dday = anniversaries[i].getDdayText();   // 다형성: 생일 등은 null이 나옴
            if (dday != null) {
                occasionDates[i] += " · " + dday;
            }
        }

        // 줄마다 높이가 달라서 GridLayout 대신 위에서 아래로 쌓는 BoxLayout 사용
        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setOpaque(false);
        rows.add(makeChipRow("기념일", occasionNames, occasionDates, occasionChips));
        occasionChips[nearestAnniversary()].setSelected(true);   // 가장 가까운 기념일을 기본 선택
        rows.add(Box.createVerticalStrut(14));
        rows.add(makeChipRow("받는 사람", TARGETS, null, targetChips));
        rows.add(Box.createVerticalStrut(14));
        rows.add(makeChipRow("예산", BUDGET_NAMES, null, budgetChips));

        recommendButton.addActionListener(this);
        wishButton.addActionListener(this);   // 두 버튼 모두 actionPerformed 하나에서 처리

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(wishButton);
        buttons.add(recommendButton);

        RoundPanel box = new RoundPanel(Color.WHITE, 22, true);
        box.setLayout(new BorderLayout(0, 18));
        box.setBorder(new EmptyBorder(20, 22, 22, 22));
        box.add(rows, BorderLayout.CENTER);
        box.add(buttons, BorderLayout.SOUTH);
        return box;
    }

    /**
     * "기념일 [생일] [크리스마스] ..." 처럼 선택 칩 한 줄 만들기
     * subs(날짜)가 있으면 칩 아래에 작은 글자로 날짜를 보여주고, 4칸씩 줄을 나눠 배치
     */
    private JPanel makeChipRow(String title, String[] names, String[] subs, Chip[] chips) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 14));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(80, 36));
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setBorder(new EmptyBorder(subs == null ? 9 : 16, 0, 0, 0));   // 첫 줄 칩과 높이 맞추기

        JPanel chipLine;
        if (subs == null) {
            chipLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        } else {
            chipLine = new JPanel(new GridLayout(0, 4, 8, 8));   // 4개씩 줄바꿈
        }
        chipLine.setOpaque(false);
        ButtonGroup group = new ButtonGroup();   // 한 줄에서 하나만 선택되게
        for (int i = 0; i < names.length; i++) {
            chips[i] = new Chip(names[i], subs == null ? null : subs[i]);
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
    private JPanel makeGiftCard(Gift gift, int number, Anniversary anniversary) {
        // 다형성: 선물 종류(일반 / 마음 / 모바일)마다 다른 색과 태그가 나옴 → if문 없이 처리
        Color main = gift.getColor();
        Color light = gift.getLightColor();
        String tagText = gift.getTag(number);
        if (!gift.fitsOccasion(anniversary.getName())) {
            tagText = "이런 것도 있어요";   // 기념일은 안 맞지만 빈자리를 채운 선물
        }

        // 위쪽: 태그 + 선물 아이콘
        JLabel tag = new JLabel(tagText);
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

        // 오른쪽 위: 하트(찜하기) + 선물 아이콘
        HeartButton heart = new HeartButton();
        heart.setSelected(wishlist.contains(gift));   // 이미 찜한 선물이면 채워진 하트
        heart.addActionListener(e -> {
            if (heart.isSelected()) {
                wishlist.add(gift);
            } else {
                wishlist.remove(gift);
            }
            updateWishButton();
        });
        JPanel icons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        icons.setOpaque(false);
        icons.add(heart);
        icons.add(new GiftIcon(main, 40));
        top.add(icons, BorderLayout.EAST);

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
        if (e.getSource() == wishButton) {   // 어떤 버튼이 눌렸는지 구분
            showWishlist();
        } else {
            recommend();
        }
    }

    /** [선물 추천받기] 버튼 */
    private void recommend() {
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

        shownGifts = gifts;
        shownAnniversary = anniversary;
        showCards();

        recommendButton.setText("다른 추천 보기");   // 한 번 누른 뒤에는 버튼 글자만 바꿈
    }

    /** 선물 카드 다시 채우기 */
    private void showCards() {
        cardPanel.removeAll();
        for (int i = 0; i < shownGifts.size(); i++) {
            cardPanel.add(makeGiftCard(shownGifts.get(i), i + 1, shownAnniversary));
        }
        cardPanel.revalidate();   // 바뀐 화면 다시 그리기
        cardPanel.repaint();
    }

    /** 찜 목록 버튼 글자를 개수에 맞게 바꾸기 */
    private void updateWishButton() {
        wishButton.setText("찜 목록 (" + wishlist.size() + ")");
    }

    // ==================== 찜 목록 창 ====================

    /** [찜 목록] 버튼 - 찜한 선물과 총액을 보여주는 작은 창 */
    private void showWishlist() {
        JDialog dialog = new JDialog(this, "찜 목록", true);   // true: 창을 닫기 전까지 뒤 화면을 못 누름
        dialog.setSize(440, 520);
        dialog.setLocationRelativeTo(this);
        fillWishlist(dialog);
        dialog.setVisible(true);   // 창이 닫힐 때까지 여기서 기다림

        // 창을 닫은 뒤: 찜 목록에서 뺀 선물이 있을 수 있으니 하트와 버튼 글자 다시 그리기
        updateWishButton();
        if (shownAnniversary != null) {
            showCards();
        }
    }

    /** 찜 목록 창 내용 채우기 (선물을 뺄 때마다 다시 호출) */
    private void fillWishlist(JDialog dialog) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        if (wishlist.size() == 0) {
            JLabel empty = new JLabel("아직 찜한 선물이 없어요. 카드의 하트를 눌러 보세요!");
            empty.setFont(new Font(FONT, Font.PLAIN, 14));
            empty.setForeground(GRAY);
            list.add(empty);
        }
        for (Gift gift : wishlist.getGifts()) {
            JLabel name = new JLabel(gift.getName());
            name.setFont(new Font(FONT, Font.BOLD, 15));
            name.setForeground(TEXT);
            JLabel price = new JLabel(gift.getPriceText());   // 다형성: 선물 종류마다 다른 가격 글자
            price.setFont(new Font(FONT, Font.BOLD, 13));
            price.setForeground(gift.getColor());

            JPanel texts = new JPanel(new GridLayout(2, 1, 0, 2));
            texts.setOpaque(false);
            texts.add(name);
            texts.add(price);

            RoundButton remove = new RoundButton("빼기", Color.WHITE, ACCENT_LIGHT, ACCENT);
            remove.setFont(new Font(FONT, Font.BOLD, 13));
            remove.setPreferredSize(new Dimension(64, 34));
            remove.setMargin(new Insets(0, 0, 0, 0));   // 버튼 안쪽 여백을 없애서 글자가 잘리지 않게
            remove.addActionListener(e -> {
                wishlist.remove(gift);
                fillWishlist(dialog);   // 목록 다시 그리기
            });
            JPanel removeBox = new JPanel(new GridBagLayout());   // 버튼을 세로 가운데에
            removeBox.setOpaque(false);
            removeBox.add(remove);

            RoundPanel row = new RoundPanel(Color.WHITE, 16, true);
            row.setLayout(new BorderLayout());
            row.setBorder(new EmptyBorder(10, 16, 12, 14));
            row.add(texts, BorderLayout.CENTER);
            row.add(removeBox, BorderLayout.EAST);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            list.add(row);
            list.add(Box.createVerticalStrut(8));
        }

        JPanel listWrap = new JPanel(new BorderLayout());   // 목록을 위쪽부터 채우기
        listWrap.setOpaque(false);
        listWrap.add(list, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(listWrap);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JLabel total = new JLabel("총 " + wishlist.size() + "개  ·  " + String.format("%,d원", wishlist.getTotalPrice()));
        total.setFont(new Font(FONT, Font.BOLD, 18));
        total.setForeground(ACCENT);
        total.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(14, 2, 0, 0)));

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        content.add(scroll, BorderLayout.CENTER);
        content.add(total, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.revalidate();
        dialog.repaint();
    }

    /** D-day가 가장 적게 남은 기념일의 번호 찾기 (날짜가 없는 기념일은 건너뜀) */
    private int nearestAnniversary() {
        int best = 0;
        int bestLeft = Integer.MAX_VALUE;
        for (int i = 0; i < anniversaries.length; i++) {
            int left = anniversaries[i].getDaysLeft();
            if (left >= 0 && left < bestLeft) {
                best = i;
                bestLeft = left;
            }
        }
        return best;
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

    /** 선택 칩 (JToggleButton 상속) - 선택되면 빨간색으로 채워짐, sub가 있으면 아래에 작은 글자 */
    static class Chip extends JToggleButton {
        private String title;
        private String sub;   // 아래 작은 글자 (예: 날짜), 없으면 null

        Chip(String title, String sub) {
            this.title = title;
            this.sub = sub;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            updateLook();
            setPreferredSize(new Dimension(getPreferredSize().width + 16, sub == null ? 36 : 52));
            addItemListener(e -> updateLook());   // 선택 상태가 바뀔 때마다 모양 갱신
        }

        /** 선택 상태에 따라 글자색 / 굵기 변경 */
        private void updateLook() {
            boolean on = isSelected();
            setForeground(on ? Color.WHITE : TEXT);
            setFont(new Font(FONT, on ? Font.BOLD : Font.PLAIN, 14));
            if (sub == null) {
                setText(title);
            } else {   // html로 두 줄 만들기 (윗줄: 이름, 아랫줄: 작은 날짜)
                String subColor = on ? "#FFE4E4" : "#A09184";
                setText("<html><center>" + title + "<br><span style='font-size:9px; font-weight:normal; color:"
                        + subColor + "'>" + sub + "</span></center></html>");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight() - 1;
            int arc = sub == null ? h : 20;   // 두 줄 칩은 알약 모양 대신 둥근 사각형
            if (isSelected()) {
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth() - 1, h, arc, arc);
            } else {
                g2.setColor(getModel().isRollover() ? ACCENT_LIGHT : BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, h, arc, arc);
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, h, arc, arc);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** 찜하기 하트 버튼 (JToggleButton 상속) - 누르면 빨간 하트로 채워짐 */
    static class HeartButton extends JToggleButton {
        HeartButton() {
            setPreferredSize(new Dimension(32, 32));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText("찜하기");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {   // 마우스를 올리면 연한 동그라미 배경
                g2.setColor(ACCENT_LIGHT);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            }

            // 곡선 두 개로 하트 모양 만들기
            double w = 18, h = 16;
            double cx = getWidth() / 2.0;
            double y = (getHeight() - h) / 2.0 + 1;
            Path2D heart = new Path2D.Double();
            heart.moveTo(cx, y + h);                                                  // 아래 뾰족한 끝
            heart.curveTo(cx - w * 0.95, y + h * 0.5, cx - w * 0.5, y - h * 0.3, cx, y + h * 0.25);
            heart.curveTo(cx + w * 0.5, y - h * 0.3, cx + w * 0.95, y + h * 0.5, cx, y + h);

            if (isSelected()) {
                g2.setColor(ACCENT);
                g2.fill(heart);
            } else {
                g2.setColor(GRAY);
                g2.setStroke(new BasicStroke(1.8f));
                g2.draw(heart);
            }
            g2.dispose();
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