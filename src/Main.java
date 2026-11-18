import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private static final String[] TARGETS = {"친구", "연인", "썸 상대", "부모님", "조부모님",
            "형제자매", "조카", "선후배", "직장 동료", "선생님"};
    private static final String[] BUDGET_NAMES = {"1만원 이하", "3만원 이하", "5만원 이하", "상관없음"};
    private static final int[] BUDGET_MAX = {10000, 30000, 50000, Integer.MAX_VALUE};
    // 정렬 칩 순서는 GiftRecommender.RANDOM(0) / CHEAP_FIRST(1) / EXPENSIVE_FIRST(2) 와 같음
    private static final String[] SORT_NAMES = {"랜덤", "가격 낮은 순", "가격 높은 순"};
    private static final int CHIP_WIDTH = 82;   // 예산 칩 너비 (받는 사람 칩 한 칸과 비슷하게)

    // 색깔 모음
    static final Color BG = new Color(253, 248, 245);          // 배경 (따뜻한 크림색)
    static final Color ACCENT = new Color(240, 84, 100);       // 메인 코랄 (기념일 / 추천 버튼)
    static final Color ACCENT_DARK = new Color(214, 60, 80);   // 진한 코랄 (마우스 올렸을 때)
    static final Color ACCENT_LIGHT = new Color(255, 234, 236);// 연한 코랄
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
    static final Color HOVER_DARK = new Color(238, 233, 230);  // 칩에 마우스를 올렸을 때
    static final String EMOJI_FONT = "Apple Color Emoji";      // 맥 컬러 이모지 글꼴
    static final int RADIUS = 10;                              // 칩 / 버튼 / 입력 칸 모서리 둥글기
    static final String FONT = "Apple SD Gothic Neo";          // 맥 한글 글꼴

    private Anniversary[] anniversaries = Anniversaries.getAll();
    private Chip[] occasionChips = new Chip[anniversaries.length];
    private Chip[] targetChips = new Chip[TARGETS.length];
    private Chip[] budgetChips = new Chip[BUDGET_NAMES.length];
    private Chip[] sortChips = new Chip[SORT_NAMES.length];

    private RoundButton recommendButton = new RoundButton("선물 추천받기", ACCENT, ACCENT_DARK, Color.WHITE);
    private RoundButton wishButton = new RoundButton("♥ 찜 목록 (0)", ACCENT_LIGHT, new Color(255, 222, 226), ACCENT);
    private JLabel conditionLabel = new JLabel(" ");
    private JLabel messageLabel = new JLabel(" ");
    private BudgetField budgetField = new BudgetField();   // 예산 직접 입력 칸
    private JPanel cardPanel = new JPanel(new GridLayout(1, GiftRecommender.COUNT, 14, 0));   // 선물 카드 5장이 들어갈 곳

    private GiftRecommender recommender = new GiftRecommender();
    private Wishlist wishlist = new Wishlist();

    // 지금 화면에 보이는 카드 정보 (찜 목록을 닫은 뒤 하트를 다시 그릴 때 사용)
    private ArrayList<Gift> shownGifts = new ArrayList<>();
    private Anniversary shownAnniversary;
    private String shownTarget;

    public Main() {
        setTitle("준비된 선물");
        setSize(1100, 760);   // 13인치 노트북 화면에도 들어가는 크기
        setMinimumSize(new Dimension(1080, 720));
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
        rows.add(makeChipRow("📅  기념일", occasionNames, occasionDates, occasionChips, 7, ACCENT));
        occasionChips[nearestAnniversary()].setSelected(true);   // 가장 가까운 기념일을 기본 선택
        rows.add(Box.createVerticalStrut(18));
        rows.add(makeChipRow("👥  받는 사람", TARGETS, null, targetChips, TARGETS.length, VIOLET));
        rows.add(Box.createVerticalStrut(18));
        JPanel budgetRow = makeChipRow("💰  예산", BUDGET_NAMES, null, budgetChips, 0, TEAL);
        rows.add(budgetRow);
        setupBudgetField();

        recommendButton.addActionListener(this);
        wishButton.addActionListener(this);   // 두 버튼 모두 actionPerformed 하나에서 처리

        // 버튼은 예산 줄 오른쪽 빈자리에 (세로 공간 아끼기)
        wishButton.setPreferredSize(new Dimension(112, 34));
        recommendButton.setPreferredSize(new Dimension(132, 34));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(wishButton);
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
     * subs(날짜)가 있으면 칩 아래에 작은 글자로 날짜를 보여줌
     * columns가 0이면 칩을 같은 너비로 왼쪽부터 이어 붙이고, 0보다 크면 그 개수만큼씩 칸을 나눠 배치
     * color: 이 줄에서 선택된 칩의 색
     */
    private JPanel makeChipRow(String title, String[] names, String[] subs, Chip[] chips, int columns, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(100, 34));
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setBorder(new EmptyBorder(subs == null ? 8 : 13, 0, 0, 0));   // 첫 줄 칩 글자와 높이 맞추기

        JPanel chipLine = new JPanel();
        if (columns == 0) {
            chipLine.setLayout(new BoxLayout(chipLine, BoxLayout.X_AXIS));   // 왼쪽부터 차례로
        } else {
            chipLine.setLayout(new GridLayout(0, columns, 8, 8));            // columns개씩 줄바꿈
        }
        chipLine.setOpaque(false);
        ButtonGroup group = new ButtonGroup();   // 한 줄에서 하나만 선택되게
        for (int i = 0; i < names.length; i++) {
            chips[i] = new Chip(names[i], subs == null ? null : subs[i]);
            chips[i].setColor(color);
            group.add(chips[i]);
            if (columns == 0) {
                chips[i].setFixedWidth(CHIP_WIDTH);   // 받는 사람 칩과 같은 너비로 맞춤
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

    /** 결과가 나오는 부분 (조건 + 응원 문구 + 정렬 + 선물 카드) */
    private JPanel makeResultArea() {
        conditionLabel.setFont(new Font(FONT, Font.BOLD, 17));
        conditionLabel.setForeground(TEXT);
        messageLabel.setFont(new Font(FONT, Font.PLAIN, 13));
        messageLabel.setForeground(GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        textPanel.add(conditionLabel);
        textPanel.add(messageLabel);

        // 오른쪽: 정렬 버튼 3개를 하나로 붙인 모양 (누르면 바로 다시 추천)
        RoundPanel segment = new RoundPanel(new Color(238, 236, 233), RADIUS + 2, false);
        segment.setLayout(new GridLayout(1, SORT_NAMES.length, 2, 0));
        segment.setBorder(new EmptyBorder(3, 3, 3, 3));
        ButtonGroup sortGroup = new ButtonGroup();
        for (int i = 0; i < SORT_NAMES.length; i++) {
            sortChips[i] = new Chip(SORT_NAMES[i], null);
            sortChips[i].setSegment(true);
            sortChips[i].addActionListener(this);
            sortGroup.add(sortChips[i]);
            segment.add(sortChips[i]);
        }
        sortChips[0].setSelected(true);
        JPanel sortBox = new JPanel(new GridBagLayout());   // 세로 가운데 정렬
        sortBox.setOpaque(false);
        sortBox.add(segment);

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.add(textPanel, BorderLayout.CENTER);
        header.add(sortBox, BorderLayout.EAST);

        cardPanel.setOpaque(false);

        JPanel result = new JPanel(new BorderLayout(0, 16));
        result.setOpaque(false);
        result.add(header, BorderLayout.NORTH);
        result.add(cardPanel, BorderLayout.CENTER);
        return result;
    }

    /** 아직 추천받기 전 화면 */
    private void showEmpty() {
        conditionLabel.setText("어떤 선물이 좋을까요?");
        messageLabel.setText("위에서 조건을 고르고 [선물 추천받기]를 눌러 보세요.");
        showEmptyCards();
    }

    /** 카드 자리에 흐린 빈 카드 보여주기 (처음 화면 / 예산 오류일 때) */
    private void showEmptyCards() {
        cardPanel.removeAll();
        for (int i = 0; i < GiftRecommender.COUNT; i++) {
            JPanel card = new DashedCard();
            card.setLayout(new BorderLayout());
            card.add(new GiftIcon(new Color(226, 223, 220), 40), BorderLayout.CENTER);
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * 선물 한 개를 보여주는 카드 만들기
     *  ┌──────────────────┐
     *  │ [종류 태그]  [하트] │  ← 연한 색 그림 영역 (남는 높이를 채움)
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

        // ----- 위: 그림 영역 (태그 + 하트 + 선물 그림) -----
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

        JPanel thumbTop = new JPanel(new BorderLayout());
        thumbTop.setOpaque(false);
        thumbTop.add(tagLine, BorderLayout.WEST);
        thumbTop.add(heart, BorderLayout.EAST);

        // 종류 색 그라데이션 배경 + 가운데 큰 이모지 그림
        GradientPanel thumb = new GradientPanel(light, mix(light, main, 0.22), 12);
        thumb.setLayout(new BorderLayout());
        thumb.setBorder(new EmptyBorder(8, 8, 8, 6));
        thumb.add(thumbTop, BorderLayout.NORTH);
        JLabel picture = new JLabel(gift.getEmoji(), SwingConstants.CENTER);
        picture.setFont(new Font(EMOJI_FONT, Font.PLAIN, 54));
        thumb.add(picture, BorderLayout.CENTER);

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

        JLabel priceLabel = new JLabel(gift.getPriceText());   // 다형성: 종류마다 가격 글자가 다름
        priceLabel.setFont(new Font(FONT, Font.BOLD, 15));
        priceLabel.setForeground(main);
        priceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(10, 0, 0, 0)));

        JPanel info = new JPanel(new BorderLayout(0, 6));
        info.setOpaque(false);
        info.add(nameLabel, BorderLayout.NORTH);
        info.add(descLabel, BorderLayout.CENTER);
        // 글 길이와 상관없이 글 영역 높이를 같게 → 카드마다 그림 영역 높이가 똑같아짐
        info.setPreferredSize(new Dimension(0, 100));
        JPanel infoWithPrice = new JPanel(new BorderLayout(0, 12));
        infoWithPrice.setOpaque(false);
        infoWithPrice.add(info, BorderLayout.CENTER);
        infoWithPrice.add(priceLabel, BorderLayout.SOUTH);

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
        if (e.getSource() == wishButton) {   // 어떤 버튼이 눌렸는지 구분
            showWishlist();
        } else if (e.getSource() == recommendButton || e.getSource() == budgetField) {   // 입력 칸에서 Enter
            recommend();
        } else if (shownAnniversary != null) {   // 정렬 칩: 이미 추천받은 상태면 바로 다시 정렬
            recommend();
        }
    }

    /** [선물 추천받기] 버튼 */
    private void recommend() {
        // 선택된 칩 찾기
        Anniversary anniversary = anniversaries[selectedIndex(occasionChips)];
        String target = TARGETS[selectedIndex(targetChips)];
        int order = selectedIndex(sortChips);

        // 예산: 직접 입력한 값이 있으면 그걸 쓰고, 없으면 선택된 칩
        int maxPrice;
        String budgetName;
        String typed = budgetField.getText().trim();
        if (!typed.isEmpty()) {
            try {
                maxPrice = parseBudget(typed);
            } catch (BudgetException ex) {   // 잘못 입력하면 추천하지 않고 이유만 보여줌
                showBudgetError(ex.getMessage());
                return;
            }
            budgetName = String.format("%,d원 이하", maxPrice);
        } else {
            int budget = selectedIndex(budgetChips);
            maxPrice = BUDGET_MAX[budget];
            budgetName = BUDGET_NAMES[budget];
        }
        budgetField.setError(false);
        messageLabel.setForeground(GRAY);

        ArrayList<Gift> gifts = recommender.recommend(anniversary, target, maxPrice, order);

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

        // 한 번 누른 뒤에는 버튼 글자만 바꿈 (가격순일 때는 결과가 거의 같아서 "다시 추천받기")
        recommendButton.setText(order == GiftRecommender.RANDOM ? "다른 추천 보기" : "다시 추천받기");
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

    /** 찜 목록 버튼 글자를 개수에 맞게 바꾸기 */
    private void updateWishButton() {
        wishButton.setText("♥ 찜 목록 (" + wishlist.size() + ")");
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
            JLabel price = new JLabel(gift.getTag() + "  ·  " + gift.getPriceText());   // 다형성: 종류마다 다른 태그 / 가격 글자
            price.setFont(new Font(FONT, Font.PLAIN, 13));
            price.setForeground(GRAY);

            JPanel texts = new JPanel(new GridLayout(2, 1, 0, 2));
            texts.setOpaque(false);
            texts.add(name);
            texts.add(price);

            RoundButton remove = new RoundButton("빼기", Color.WHITE, HOVER, TEXT);
            remove.setFont(new Font(FONT, Font.PLAIN, 13));
            remove.setPreferredSize(new Dimension(60, 32));
            remove.addActionListener(e -> {
                wishlist.remove(gift);
                fillWishlist(dialog);   // 목록 다시 그리기
            });
            JPanel removeBox = new JPanel(new GridBagLayout());   // 버튼을 세로 가운데에
            removeBox.setOpaque(false);
            removeBox.add(remove);

            JLabel picture = new JLabel(gift.getEmoji());
            picture.setFont(new Font(EMOJI_FONT, Font.PLAIN, 26));

            RoundPanel row = new RoundPanel(Color.WHITE, 12, true);
            row.setLayout(new BorderLayout(12, 0));
            row.setBorder(new EmptyBorder(10, 14, 12, 14));
            row.add(picture, BorderLayout.WEST);
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
        total.setFont(new Font(FONT, Font.BOLD, 17));
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

    // ==================== 예산 직접 입력 ====================

    /** 예산 칩 옆에 입력 칸 붙이고, 칩과 입력 칸 중 하나만 쓰이게 연결 */
    private void setupBudgetField() {
        Container chipLine = budgetChips[0].getParent();   // 칩들이 들어 있는 줄
        chipLine.add(Box.createHorizontalStrut(8));
        chipLine.add(budgetField);                          // 줄 맨 끝에 추가
        budgetField.addActionListener(this);          // Enter를 누르면 바로 추천

        // 칩을 누르면 → 입력 칸 비우기
        for (Chip chip : budgetChips) {
            chip.addActionListener(e -> {
                budgetField.setText("");
                budgetField.setError(false);
            });
        }

        // 입력 칸에 글자를 쓰면 → 칩 선택 해제 / 다 지우면 → "상관없음" 다시 선택
        ButtonGroup group = ((DefaultButtonModel) budgetChips[0].getModel()).getGroup();
        budgetField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { changed(); }
            public void removeUpdate(DocumentEvent e) { changed(); }
            public void changedUpdate(DocumentEvent e) { changed(); }

            private void changed() {
                budgetField.setError(false);
                if (!budgetField.getText().isEmpty()) {
                    group.clearSelection();
                } else if (group.getSelection() == null) {
                    budgetChips[budgetChips.length - 1].setSelected(true);
                }
            }
        });
    }

    /**
     * 입력한 글자를 예산(원)으로 바꾸기
     * "25000", "25,000원", "3만", "3만원", "2만5000" 모두 가능
     * 잘못된 값이면 BudgetException을 던짐
     */
    private int parseBudget(String text) throws BudgetException {
        String clean = text.replace(",", "").replace("원", "").replace(" ", "");

        long value;
        try {
            int man = clean.indexOf("만");
            if (man >= 0) {   // "2만5000" → 2 × 10000 + 5000
                String front = clean.substring(0, man);
                String back = clean.substring(man + 1);
                value = Long.parseLong(front) * 10000 + (back.isEmpty() ? 0 : Long.parseLong(back));
            } else {
                value = Long.parseLong(clean);
            }
        } catch (NumberFormatException e) {   // 숫자가 아닌 글자가 섞여 있으면
            throw new BudgetException("예산은 숫자로 입력해 주세요. (예: 25000, 3만)");
        }

        if (value <= 0) {
            throw new BudgetException("예산은 1원 이상으로 입력해 주세요.");
        }
        if (value > 100000000) {
            throw new BudgetException("예산이 너무 커요. 1억 원 이하로 입력해 주세요.");
        }
        return (int) value;
    }

    /** 예산 입력이 잘못됐을 때 빨간 글씨로 알려주기 */
    private void showBudgetError(String message) {
        budgetField.setError(true);
        conditionLabel.setText("예산을 다시 확인해 주세요");
        messageLabel.setForeground(ACCENT);
        messageLabel.setText(message);
        budgetField.requestFocusInWindow();

        // 이전 추천 카드가 남아 있으면 헷갈리니까 빈 카드로 바꾸기
        shownGifts = new ArrayList<>();
        shownAnniversary = null;   // 찜 목록을 닫거나 정렬 칩을 눌러도 이전 카드가 다시 나오지 않게
        showEmptyCards();
        recommendButton.setText("선물 추천받기");
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
     * 선택 칩 (JToggleButton 상속) - 선택되면 빨간색으로 채워짐, sub가 있으면 아래에 작은 글자
     * segment가 true면 정렬 버튼처럼 테두리 없이 붙어 있는 모양 (선택되면 흰 배경)
     */
    static class Chip extends JToggleButton {
        private String title;
        private String sub;              // 아래 작은 글자 (예: 날짜), 없으면 null
        private boolean segment = false;
        private Color color = ACCENT;    // 선택됐을 때 색 (줄마다 다름)

        Chip(String title, String sub) {
            this.title = title;
            this.sub = sub;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(0, 8, 0, 8));   // 기본 버튼 여백 대신 좁은 여백 (글자가 잘리지 않게)
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            updateLook();
            setPreferredSize(new Dimension(getPreferredSize().width + 8, sub == null ? 34 : 44));
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

        /** 정렬 버튼 모양으로 바꾸기 */
        void setSegment(boolean segment) {
            this.segment = segment;
            setBorder(new EmptyBorder(0, 14, 0, 14));
            setPreferredSize(null);   // 글자 크기에 맞게 다시 계산
            setPreferredSize(new Dimension(getPreferredSize().width, 28));
            updateLook();
        }

        /** 선택 상태에 따라 글자색 / 굵기 변경 */
        private void updateLook() {
            boolean on = isSelected();
            if (segment) {
                setForeground(on ? TEXT : GRAY);
            } else {
                setForeground(on ? Color.WHITE : TEXT);
            }
            setFont(new Font(FONT, on ? Font.BOLD : Font.PLAIN, 13));
            if (sub == null) {
                setText(title);
            } else {   // html로 두 줄 만들기 (윗줄: 이름, 아랫줄: 작은 날짜)
                String subColor = on ? "#FFFFFF" : "#A3A3AA";
                setText("<html><center>" + title + "<br><span style='font-size:9px; font-weight:normal; color:"
                        + subColor + "'>" + sub + "</span></center></html>");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            if (segment) {   // 정렬 버튼: 선택된 것만 흰 배경
                if (isSelected()) {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
                }
            } else if (isSelected()) {
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

    /** 예산 입력 칸 (JTextField 상속) - 비어 있으면 "직접 입력" 안내 글자, 오른쪽에 "원" */
    static class BudgetField extends JTextField {
        private boolean error = false;   // 잘못 입력했으면 빨간 테두리

        BudgetField() {
            setFont(new Font(FONT, Font.PLAIN, 13));
            setForeground(TEXT);
            setCaretColor(TEAL);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 12, 0, 26));   // 오른쪽은 "원" 글자 자리
            Dimension d = new Dimension(CHIP_WIDTH + 20, 34);
            setPreferredSize(d);
            setMaximumSize(d);
            addFocusListener(new java.awt.event.FocusAdapter() {   // 클릭하면 테두리 색 바꾸기
                public void focusGained(java.awt.event.FocusEvent e) { repaint(); }
                public void focusLost(java.awt.event.FocusEvent e) { repaint(); }
            });
        }

        void setError(boolean error) {
            this.error = error;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth() - 1;
            int h = getHeight() - 1;
            boolean active = hasFocus() || !getText().isEmpty();

            // 배경 + 테두리 (칩과 같은 모양, 입력 중이면 빨간 테두리)
            g2.setColor(active ? Color.WHITE : HOVER);
            g2.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
            if (error || active) {
                g2.setColor(error ? ACCENT : TEAL);
                g2.setStroke(new BasicStroke(error ? 2f : 1f));
                g2.drawRoundRect(0, 0, w, h, RADIUS, RADIUS);
            }

            // 안내 글자 / "원"
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            if (getText().isEmpty() && !hasFocus()) {
                g2.setColor(GRAY);
                g2.drawString("직접 입력", 12, textY);
            }
            g2.setColor(active ? TEXT : GRAY);
            g2.drawString("원", w - 20, textY);
            g2.dispose();
            super.paintComponent(g);   // 입력한 글자와 커서는 원래 JTextField가 그림
        }
    }

    /** 찜하기 하트 버튼 (JToggleButton 상속) - 누르면 빨간 하트로 채워짐 */
    static class HeartButton extends JToggleButton {
        HeartButton() {
            setPreferredSize(new Dimension(28, 28));
            setBorder(new EmptyBorder(0, 0, 0, 0));
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
                g2.setColor(HOVER);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            }

            // 곡선 두 개로 하트 모양 만들기
            double w = 16, h = 14;
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
                g2.setColor(new Color(180, 178, 184));
                g2.setStroke(new BasicStroke(1.6f));
                g2.draw(heart);
            }
            g2.dispose();
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

    /** 추천받기 전 빈 카드 (JPanel 상속) - 점선 테두리만 */
    static class DashedCard extends JPanel {
        DashedCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(222, 219, 216));
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{6f, 5f}, 0f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);
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

    /** 맨 위 제목 띠 (JPanel 상속) - 코랄 → 복숭아색 그라데이션 */
    static class Header extends JPanel {
        Header() {
            setLayout(new BorderLayout(10, 0));
            setPreferredSize(new Dimension(0, 64));
            setBorder(new EmptyBorder(0, 24, 0, 24));

            JLabel icon = new JLabel("🎁");
            icon.setFont(new Font(EMOJI_FONT, Font.PLAIN, 28));
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