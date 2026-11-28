import java.awt.*;

/**
 * 모든 식물의 부모 클래스
 *
 * [추상 클래스] abstract라서 new Plant()는 할 수 없고, 자식 클래스로만 만듭니다.
 * [캡슐화] 변수는 모두 private이고 getter / setter로만 접근합니다.
 *         setter 안에서 값이 0~100을 벗어나지 않게 막습니다.
 */
public abstract class Plant {

    private String name;        // 이름
    private int water = 50;     // 수분 (0~100)
    private int sun = 50;       // 햇빛 (0~100)
    private int health = 60;    // 건강 (0~100)
    private int day = 1;        // 며칠째인지
    private int goodDays = 0;   // 건강하게 보낸 날 수

    public Plant(String name) {
        this.name = name;
    }

    // 자식 클래스가 꼭 만들어야 하는 메소드 (추상 메소드)
    public abstract String getKind();      // 종류 이름
    public abstract String getSpeech();    // 말풍선 대사
    public abstract void draw(Graphics2D g, int x, int y, Color leaf);   // 식물 그림

    // 물 주기 (자식이 바꿀 수 있음)
    public void water() {
        setWater(water + 20);
    }

    // 햇빛 쬐기 (자식이 바꿀 수 있음)
    public void sunbathe() {
        setSun(sun + 20);
    }

    // 영양제 주기
    public void feed() {
        setHealth(health + 10);
    }

    // 하루 보내기: 날짜 +1, 건강이 60 이상이면 건강한 날 +1
    // (자식 클래스가 수분/햇빛/건강을 바꾼 뒤 super.nextDay()로 부름)
    public void nextDay() {
        day = day + 1;
        if (health >= 60) {
            goodDays = goodDays + 1;
        }
    }

    // 성장 단계: 건강한 날 3일마다 한 단계 (0 씨앗, 1 새싹, 2 줄기, 3 꽃)
    public int getStage() {
        int stage = goodDays / 3;
        if (stage > 3) {
            stage = 3;
        }
        return stage;
    }

    public String getStageName() {
        String[] names = {"씨앗", "새싹", "줄기", "꽃"};
        return names[getStage()];
    }

    // 값이 0~100을 벗어나지 않게 막기
    private int limit(int value) {
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return value;
    }

    // getter
    public String getName() { return name; }
    public int getWater() { return water; }
    public int getSun() { return sun; }
    public int getHealth() { return health; }
    public int getDay() { return day; }
    public int getGoodDays() { return goodDays; }

    // setter
    public void setWater(int water) { this.water = limit(water); }
    public void setSun(int sun) { this.sun = limit(sun); }
    public void setHealth(int health) { this.health = limit(health); }
    public void setDay(int day) { this.day = day; }
    public void setGoodDays(int goodDays) { this.goodDays = goodDays; }
}
