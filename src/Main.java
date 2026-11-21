import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 프로그램 시작 + 화면(창) 클래스
 *
 * JFrame을 상속(extends)해서 창을 만들고
 * ActionListener 인터페이스를 구현(implements)해서 버튼 클릭을 처리합니다.
 * 화면 부품(둥근 패널 / 둥근 버튼 / 선택 칩 / 이모지 그림 / 제목 띠)은
 * 아래쪽에 Swing 클래스를 상속한 작은 클래스로 만들었습니다.
 */
public class Main extends JFrame implements ActionListener {

    // 선택지
    private static final String[] TARGETS = {"친구", "연인", "부모님", "형제자매", "선생님"};
    private static final String[] BUDGET_NAMES = {"1만원 이하", "3만원 이하", "5만원 이하", "상관없음"};
    private static final int[] BUDGET_MAX = {10000, 30000, 50000, Integer.MAX_VALUE};

    // 색깔 모음
    static final Color BG = new Color(253, 248, 245);       // 배경 (따뜻한 크림색)
    static final Color ACCENT = new Color(240, 84, 100);    // 코랄 (기념일 줄 / 버튼 / 일반 선물)
    static final Color VIOLET = new Color(118, 94, 232);    // 받는 사람 줄
    static final Color TEAL = new Color(18, 158, 132);      // 예산 줄
    static final Color GREEN = new Color(31, 168, 108);     // 마음 선물
    static final Color BLUE = new Color(52, 128, 240);      // 모바일 선물
    static final Color PURPLE = new Color(136, 90, 240);    // 체험 선물
    static final Color TEXT = new Color(34, 34, 38);        // 기본 글자
    static final Color GRAY = new Color(122, 122, 130);     // 회색 글자
    static final Color LINE = new Color(238, 230, 226);     // 테두리
    static final Color CHIP_OFF = new Color(246, 242, 240); // 선택 안 된 칩 배경
    static final String FONT = "Apple SD Gothic Neo";       // 맥 한글 글꼴
    static final String EMOJI_FONT = "Apple Color Emoji";   // 맥 컬러 이모지 글꼴
    static final int RADIUS = 10;                           // 칩 / 버튼 모서리 둥글기

    private Anniversary[] anniversaries = Anniversaries.getAll();
    private Chip[] occasionChips = new Chip[anniversaries.length];
    private Chip[] targetChips = new Chip[TARGETS.length];
    private Chip[] budgetChips = new Chip[BUDGET_NAMES.length];

    private RoundButton recommendButton = new RoundButton("다른 추천 보기");
    private JLabel conditionLabel = new JLabel(" ");
    private JLabel messageLabel = new JLabel(" ");
    private JPanel cardPanel = new JPanel(new GridLayout(1, GiftRecommender.COUNT, 14, 0));   // 선물 카드 5장이 들어갈 곳

    private GiftRecommender recommender = new GiftRecommender();

    public Main() {
        setTitle("준비된 선물");
        setSize(1100, 800);   // 13인치 노트북 화면에도 들어가는 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel body = new JPanel(new BorderLayout(0, 22));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(22, 24, 22, 24));
        body.add(makeSelectBox(), BorderLayout.NORTH);
        body.add(makeResultArea(), BorderLayout.CENTER);

        add(new Header(), BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        recommend();   // 처음부터 추천 결과를 보여줌
    }

    // ==================== 화면 만들기 ====================

    /** 조건 고르는 흰색 상자 (기념일 / 받는 사람 / 예산 + 버튼) */
    private JPanel makeSelectBox() {
        String[] occasionNames = new String[anniversaries.length];
        for (int i = 0; i < anniversaries.length; i++) {
            occasionNames[i] = anniversaries[i].getName();
        }

        JPanel budgetRow = makeChipRow("💰  예산", BUDGET_NAMES, budgetChips, TEAL);
        budgetRow.add(recommendButton, BorderLayout.EAST);   // 버튼은 예산 줄 오른쪽에
        recommendButton.addActionListener(this);             // 클릭하면 actionPerformed()가 실행됨

        RoundPanel box = new RoundPanel(Color.WHITE, 16, true);
        box.setLayout(new GridLayout(3, 1, 0, 18));   // 세 줄을 위에서 아래로
        box.setBorder(new EmptyBorder(22, 26, 22, 26));
        box.add(makeChipRow("📅  기념일", occasionNames, occasionChips, ACCENT));
        box.add(makeChipRow("👥  받는 사람", TARGETS, targetChips, VIOLET));
        box.add(budgetRow);
        return box;
    }

    /** "기념일 [생일] [크리스마스] ..." 처럼 제목 + 선택 칩 한 줄 만들기 (color: 선택된 칩 색) */
    private JPanel makeChipRow(String title, String[] names, Chip[] chips, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(FONT, Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(100, 34));

        JPanel chipLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));   // 칩을 왼쪽부터 나란히
        chipLine.setOpaque(false);
        ButtonGroup group = new ButtonGroup();   // 한 줄에서 하나만 선택되게
        for (int i = 0; i < names.length; i++) {
            chips[i] = new Chip(names[i], color);
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
     *  ┌──────────────┐
     *  │ [태그]        │  ← 위: 연한 색 그림 영역
     *  │     🎂       │
     *  └──────────────┘
     *   이름 / 추천 이유 / 설명   ← 아래: 글 영역
     *   가격
     */
    private JPanel makeGiftCard(Gift gift, String occasion, String target) {
        // 다형성: 선물 종류(일반 / 마음 / 모바일 / 체험)마다 다른 색과 태그가 나옴 → if문 없이 처리
        Color color = gift.getColor();
        String tagText = gift.getTag();
        if (!gift.fitsOccasion(occasion)) {   // 기념일은 안 맞지만 빈자리를 채운 선물은 회색으로
            color = GRAY;
            tagText = "이런 것도 있어요";
        }
        Color light = new Color(color.getRed(), color.getGreen(), color.getBlue(), 45);   // 같은 색을 투명하게 (45 / 255)

        // ----- 위: 그림 영역 (태그 + 선물 그림) -----
        JLabel tag = new JLabel(tagText);
        tag.setFont(new Font(FONT, Font.BOLD, 11));
        tag.setForeground(Color.WHITE);
        tag.setOpaque(true);                          // 배경색을 칠하려면 true
        tag.setBackground(color);
        tag.setBorder(new EmptyBorder(3, 6, 3, 6));   // 글자 둘레 여백
        JPanel tagLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));   // 태그가 글자 크기만큼만 차지하게
        tagLine.setOpaque(false);
        tagLine.add(tag);

        RoundPanel picture = new RoundPanel(light, 12, false);
        picture.setLayout(new BorderLayout());
        picture.setBorder(new EmptyBorder(8, 8, 8, 8));
        picture.add(tagLine, BorderLayout.NORTH);
        picture.add(new EmojiPicture(gift.getEmoji(), 64), BorderLayout.CENTER);

        // ----- 아래: 이름 + 추천 이유 + 설명 (길면 자동 줄바꿈) -----
        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(makeText(gift.getName(), Font.BOLD, 16, TEXT), BorderLayout.NORTH);
        text.add(makeText(gift.getReason(occasion, target), Font.BOLD, 12, color), BorderLayout.CENTER);
        text.add(makeText(gift.getDescription(), Font.PLAIN, 12, GRAY), BorderLayout.SOUTH);
        JPanel textBox = new JPanel(new BorderLayout());
        textBox.setOpaque(false);
        textBox.add(text, BorderLayout.NORTH);              // 글은 위쪽부터
        textBox.setPreferredSize(new Dimension(0, 104));    // 글 길이와 상관없이 카드 모양을 똑같게

        // 다형성: 종류마다 가격 글자가 다름 ("0원 (정성 100%)", "9,500원 (바로 전송)")
        JLabel price = new JLabel(gift.getPriceText());
        price.setFont(new Font(FONT, Font.BOLD, 14));
        price.setForeground(color);
        price.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(10, 0, 0, 0)));   // 위에 얇은 줄

        JPanel info = new JPanel(new BorderLayout(0, 8));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(0, 6, 0, 6));
        info.add(textBox, BorderLayout.CENTER);
        info.add(price, BorderLayout.SOUTH);

        RoundPanel card = new RoundPanel(Color.WHITE, 16, true);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(8, 8, 14, 8));
        card.add(picture, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);
        return card;
    }

    // ==================== 버튼 클릭 처리 ====================

    /** 버튼을 눌렀을 때 실행 (ActionListener 인터페이스의 메소드) */
    @Override
    public void actionPerformed(ActionEvent e) {
        recommend();
    }

    /** 고른 조건으로 선물을 추천받아 화면에 보여주기 */
    private void recommend() {
        // 1. 선택된 칩 찾기
        Anniversary anniversary = anniversaries[selectedIndex(occasionChips)];
        String target = TARGETS[selectedIndex(targetChips)];
        int budget = selectedIndex(budgetChips);

        // 2. 추천받기
        ArrayList<Gift> gifts = recommender.recommend(anniversary, target, BUDGET_MAX[budget]);

        // 3. 제목과 응원 문구 바꾸기
        conditionLabel.setText("🎁  " + target + "에게 주는 " + anniversary.getName() + " 선물  ·  " + BUDGET_NAMES[budget]);
        String message = anniversary.getMessage();   // 다형성: 기념일마다 다른 문구가 나옴
        if (target.equals("선생님")) {
            message = "청탁금지법 때문에 선생님께는 물건 대신 마음 선물을 추천해요.";
        }
        if (gifts.isEmpty()) {
            message = "조건에 맞는 선물이 없어요. 예산을 늘려 보세요!";
        }
        messageLabel.setText(message);

        // 4. 카드 다시 채우기
        cardPanel.removeAll();
        for (Gift gift : gifts) {
            cardPanel.add(makeGiftCard(gift, anniversary.getName(), target));
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

    /** 길면 자동으로 줄바꿈되는 글자 만들기 (JLabel은 줄바꿈이 안 돼서 JTextArea 사용) */
    private JTextArea makeText(String content, int style, int size, Color color) {
        JTextArea area = new JTextArea(content);
        area.setFont(new Font(FONT, style, size));
        area.setForeground(color);
        area.setLineWrap(true);      // 칸이 좁으면 다음 줄로
        area.setEditable(false);     // 글자를 고칠 수 없게
        area.setFocusable(false);
        area.setOpaque(false);       // 배경은 투명하게
        return area;
    }

    /** 프로그램 시작점 */
    public static void main(String[] args) {
        Main window = new Main();
        window.setVisible(true);
    }

    // ==================== 화면 꾸미기용 부품 클래스 ====================
    // Swing 기본 부품을 상속하고 paintComponent()를 오버라이딩해서 둥글게 직접 그립니다.

    /** 둥근 모서리 패널 (JPanel 상속) - border가 true면 얇은 테두리도 그림 */
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            if (border) {
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
        }
    }

    /** 둥근 코랄 버튼 (JButton 상속) - 마우스를 올리면 조금 진해짐 */
    static class RoundButton extends JButton {
        RoundButton(String label) {
            super(label);
            setFont(new Font(FONT, Font.BOLD, 14));
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(140, 34));
            setContentAreaFilled(false);   // 기본 버튼 모양은 끄고 직접 그림
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {   // 마우스를 올렸으면
                g2.setColor(ACCENT.darker());   // 조금 진한 색
            } else {
                g2.setColor(ACCENT);
            }
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            super.paintComponent(g);   // 글자는 원래대로 그림
        }
    }

    /** 선택 칩 (JToggleButton 상속) - 선택되면 줄 색으로 채워지고 글자가 흰색 */
    static class Chip extends JToggleButton {
        private Color color;   // 선택됐을 때 색 (줄마다 다름)

        Chip(String title, Color color) {
            super(title);
            this.color = color;
            setFont(new Font(FONT, Font.PLAIN, 13));
            setForeground(TEXT);
            setPreferredSize(new Dimension(100, 34));
            setMargin(new Insets(0, 0, 0, 0));   // 기본 여백을 없애서 글자가 잘리지 않게
            setContentAreaFilled(false);   // 기본 버튼 모양은 끄고 직접 그림
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected()) {   // 선택됨: 줄 색 배경 + 흰 글자
                g2.setColor(color);
                setForeground(Color.WHITE);
            } else {              // 선택 안 됨: 연한 회색 배경 + 검은 글자
                g2.setColor(CHIP_OFF);
                setForeground(TEXT);
            }
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            super.paintComponent(g);   // 글자는 원래대로 그림
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

        private BufferedImage image;
        private int size;

        EmojiPicture(String emoji, int size) {
            this.size = size;
            this.image = makeImage(emoji);
            setPreferredSize(new Dimension(size, size));
        }

        /** 이모지를 96픽셀 그림으로 만들기 */
        private static BufferedImage makeImage(String emoji) {
            int box = SOURCE + 24;   // 이모지가 잘리지 않게 여유 공간
            BufferedImage img = new BufferedImage(box, box, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font(EMOJI_FONT, Font.PLAIN, SOURCE));
            FontMetrics fm = g2.getFontMetrics();
            int x = (box - fm.stringWidth(emoji)) / 2;
            int y = (box + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(emoji, x, y);
            g2.dispose();
            return img;
        }

        /** 만들어 둔 그림을 size 크기로 줄여서 가운데에 그리기 */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            int draw = size * image.getWidth() / SOURCE;   // 여유 공간까지 같은 비율로
            int x = (getWidth() - draw) / 2;
            int y = (getHeight() - draw) / 2;
            g2.drawImage(image, x, y, draw, draw, null);
        }
    }

    /** 맨 위 제목 띠 (JPanel 상속) - 코랄 → 복숭아색 그라데이션 */
    static class Header extends JPanel {
        Header() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 12, 14));   // 왼쪽부터 그림 / 제목 / 설명
            setBorder(new EmptyBorder(0, 12, 0, 0));

            JLabel title = new JLabel("준비된 선물");
            title.setFont(new Font(FONT, Font.BOLD, 20));
            title.setForeground(Color.WHITE);
            JLabel subtitle = new JLabel("기념일과 받는 사람과 예산만 고르면 딱 맞는 선물을 골라 드려요");
            subtitle.setFont(new Font(FONT, Font.PLAIN, 13));
            subtitle.setForeground(Color.WHITE);

            add(new EmojiPicture("🎁", 30));
            add(title);
            add(subtitle);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(255, 150, 112)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
