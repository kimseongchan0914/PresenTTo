import java.awt.Color;

/**
 * 선물 한 개의 정보를 담는 클래스 (일반 선물)
 *
 * [캡슐화] 모든 변수를 private으로 숨기고 getter로만 값을 꺼낼 수 있게 했습니다.
 *         선물 정보는 실행 중에 바뀌면 안 되므로 setter는 만들지 않았습니다.
 * [오버라이딩] 태그 / 색 / 가격 글자 / 구매 안내 / 준비 팁은 자식 클래스가 바꿔서 씁니다.
 * [인터페이스] Comparable을 구현해서 Collections.sort()로 가격순 정렬을 할 수 있습니다.
 */
public class Gift implements Comparable<Gift> {

    private String emoji;         // 카드에 보여줄 그림 (이모지)
    private String name;          // 선물 이름
    private int price;            // 가격 (원)
    private String description;   // 한 줄 설명
    private String where;         // 어디서 살 수 있는지 (마음 선물은 준비물)
    private String[] occasions;   // 어울리는 기념일들
    private String[] targets;     // 어울리는 받는 사람들

    public Gift(String emoji, String name, int price, String description, String where,
                String[] occasions, String[] targets) {
        this.emoji = emoji;
        this.name = name;
        this.price = price;
        this.description = description;
        this.where = where;
        this.occasions = occasions;
        this.targets = targets;
    }

    /** 이 선물이 해당 기념일에 어울리는지 확인 */
    public boolean fitsOccasion(String occasion) {
        for (String o : occasions) {
            if (o.equals(occasion)) {
                return true;
            }
        }
        return false;
    }

    /** 이 선물이 해당 받는 사람에게 어울리는지 확인 */
    public boolean fitsTarget(String target) {
        for (String t : targets) {
            if (t.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 추천 이유 한 줄 (예: "연인 · 생일에 딱 맞아요")
     * 받는 사람은 추천할 때 이미 걸러졌으니, 기념일이 맞는지에 따라 문장이 달라짐
     */
    public String getReason(String occasion, String target) {
        if (fitsOccasion(occasion)) {
            return target + " · " + occasion + "에 딱 맞아요";
        }
        return occasion + " 선물은 아니지만 " + target + "에게 잘 어울려요";
    }

    /** 화면에 보여줄 가격 글자 (자식 클래스에서 오버라이딩 가능) */
    public String getPriceText() {
        return String.format("%,d원", price);   // 25000 → "25,000원"
    }

    /** 가격 비교 (Comparable 인터페이스의 메소드) - 싼 선물이 앞으로 */
    @Override
    public int compareTo(Gift other) {
        return Integer.compare(price, other.price);
    }

    /** 최종 선택 화면: 어떻게 구하면 되는지 한 줄 안내 (자식 클래스에서 오버라이딩) */
    public String getBuyGuide() {
        return "가까운 매장이나 온라인 쇼핑몰에서 살 수 있어요.";
    }

    /** 최종 선택 화면: "어디서 살까요?" 상자의 제목 */
    public String getWhereTitle() {
        return "어디서 살까요?";
    }

    /** 최종 선택 화면: 준비 일정 팁 (자식 클래스에서 오버라이딩) */
    public String getPrepTip() {
        return "택배는 2~3일 걸릴 수 있으니 미리 주문하세요.";
    }

    /** 최종 선택 화면: 준비 체크리스트 (자식 클래스에서 오버라이딩) */
    public String[] getChecklist() {
        return new String[]{"선물 사기", "포장하기", "손글씨 카드 쓰기", "직접 건네며 축하하기"};
    }

    /** 카드 왼쪽 위 태그 글자 (선물 종류 이름) */
    public String getTag() {
        return "일반 선물";
    }

    /** 카드 진한 색 (태그 글자 / 가격 / 아이콘) */
    public Color getColor() {
        return Main.ACCENT;
    }

    /** 카드 연한 색 (태그 배경) */
    public Color getLightColor() {
        return Main.ACCENT_LIGHT;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getWhere() {
        return where;
    }

    public String getDescription() {
        return description;
    }
}