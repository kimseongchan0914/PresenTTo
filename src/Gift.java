import java.awt.Color;

/**
 * 선물 한 개의 정보를 담는 클래스 (일반 선물)
 *
 * [캡슐화] 모든 변수를 private으로 숨기고 getter로만 값을 꺼낼 수 있게 했습니다.
 *         선물 정보는 실행 중에 바뀌면 안 되므로 setter는 만들지 않았습니다.
 * [오버라이딩] 태그 / 색 / 가격 글자는 자식 클래스(HandmadeGift, GifticonGift)가 바꿔서 씁니다.
 */
public class Gift {

    private String name;          // 선물 이름
    private int price;            // 가격 (원)
    private String description;   // 한 줄 설명
    private String[] occasions;   // 어울리는 기념일들
    private String[] targets;     // 어울리는 받는 사람들

    public Gift(String name, int price, String description, String[] occasions, String[] targets) {
        this.name = name;
        this.price = price;
        this.description = description;
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
        return price + "원";
    }

    /** 카드 왼쪽 위 태그 글자 (일반 선물은 "추천 1" 같은 번호) */
    public String getTag(int number) {
        return "추천 " + number;
    }

    /** 카드 진한 색 (태그 글자 / 가격 / 아이콘) */
    public Color getColor() {
        return Main.ACCENT;
    }

    /** 카드 연한 색 (태그 배경) */
    public Color getLightColor() {
        return Main.ACCENT_LIGHT;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}