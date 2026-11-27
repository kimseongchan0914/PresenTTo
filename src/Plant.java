import java.awt.Color;
import java.awt.Graphics2D;

// 모든 식물의 부모 클래스 (추상 클래스라서 직접 new 할 수 없음)
public abstract class Plant {
    public static final String[] STAGES = {"씨앗", "새싹", "줄기", "꽃"};

    // 캡슐화: 상태는 모두 private, getter/setter로만 접근
    private String name;
    private int water;      // 수분 0~100
    private int sun;        // 햇빛 0~100
    private int health;     // 건강 0~100
    private int day;        // 지난 날짜 (1일째부터)
    private int stage;      // 성장 단계 0~3
    private int goodDays;   // 이번 단계에서 건강하게 보낸 날
    private boolean fedToday; // 오늘 영양제를 줬는지

    public Plant(String name) {
        this.name = name;
        water = 50;
        sun = 50;
        health = 60;
        day = 1;
    }

    // ----- 자식마다 다르게 만드는 메소드 (다형성) -----
    public abstract String getKind();        // 종류 이름
    public abstract void nextDay();          // 하루 보내기
    public abstract String getSpeech();      // 말풍선 대사
    public abstract void draw(Graphics2D g, int x, int y, Color leaf); // 식물 그림

    // 물 주기, 햇빛 쬐기: 기본 동작 (자식이 바꿀 수 있음)
    public void water() {
        setWater(water + 20);
    }

    public void sunbathe() {
        setSun(sun + 20);
    }

    // 영양제: 모든 식물이 같음, 하루에 한 번만
    public boolean feed() {
        if (fedToday) {
            return false;
        }
        fedToday = true;
        setHealth(health + 10);
        return true;
    }

    // 하루 마무리: 날짜 +1, 건강하면(60 이상) 좋은 날 +1, 3일 모이면 성장
    protected void endDay() {
        day++;
        fedToday = false;
        if (health >= 60 && stage < 3) {
            goodDays++;
            if (goodDays >= 3) {
                stage++;
                goodDays = 0;
            }
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    public boolean isFlower() {
        return stage == 3;
    }

    public String getStageName() {
        return STAGES[stage];
    }

    // 0~100을 벗어나지 않게 막아 주는 도우미
    private int limit(int value) {
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return value;
    }

    // ----- getter / setter -----
    public String getName() { return name; }
    public int getWater() { return water; }
    public int getSun() { return sun; }
    public int getHealth() { return health; }
    public int getDay() { return day; }
    public int getStage() { return stage; }
    public int getGoodDays() { return goodDays; }

    public void setWater(int water) { this.water = limit(water); }
    public void setSun(int sun) { this.sun = limit(sun); }
    public void setHealth(int health) { this.health = limit(health); }

    public void setDay(int day) {
        if (day >= 1) {
            this.day = day;
        }
    }

    public void setStage(int stage) {
        if (stage >= 0 && stage <= 3) {
            this.stage = stage;
        }
    }

    public void setGoodDays(int goodDays) {
        if (goodDays >= 0 && goodDays <= 2) {
            this.goodDays = goodDays;
        }
    }
}
