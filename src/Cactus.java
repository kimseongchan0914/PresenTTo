import java.awt.*;

/**
 * 선인장: 물을 싫어하고, 물이 천천히 줄어듭니다.
 *
 * [상속] Plant를 상속(extends)해서 변수와 getter / setter를 물려받습니다.
 * [오버라이딩] water()를 바꿔서 물을 많이 주면 건강이 떨어지게 했습니다.
 */
public class Cactus extends Plant {

    public Cactus(String name) {
        super(name);
    }

    @Override
    public String getKind() {
        return "선인장";
    }

    // 물을 너무 많이 주면(60 이상) 오히려 건강이 떨어짐
    @Override
    public void water() {
        super.water();
        if (getWater() >= 60) {
            setHealth(getHealth() - 10);
        }
    }

    // 하루가 지나면: 수분 -5 (천천히 줄어듦), 햇빛 -15
    @Override
    public void nextDay() {
        setWater(getWater() - 5);
        setSun(getSun() - 15);
        if (getWater() >= 10 && getWater() < 60 && getSun() >= 30) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        super.nextDay();
    }

    @Override
    public String getSpeech() {
        if (getWater() >= 60) {
            return "물 그만 줘...!";
        } else if (getSun() < 30) {
            return "햇빛이 그리워...";
        } else if (getHealth() >= 80) {
            return "기분 최고!";
        } else {
            return "난 물 없이도 튼튼해~";
        }
    }

    // 둥근 몸통 + 하얀 가시, 줄기 단계부터 팔, 꽃 단계에 분홍 꽃
    @Override
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int h = getStage() * 40;   // 키: 40, 80, 120

        g.setColor(leaf);
        g.fillRoundRect(x - 25, y - h, 50, h + 5, 40, 40);   // 몸통
        if (getStage() >= 2) {
            g.fillRoundRect(x - 50, y - h + 25, 18, 40, 18, 18);   // 왼팔
            g.fillRect(x - 40, y - h + 50, 20, 14);
            g.fillRoundRect(x + 32, y - h + 15, 18, 40, 18, 18);   // 오른팔
            g.fillRect(x + 20, y - h + 40, 20, 14);
        }

        g.setColor(Color.WHITE);   // 가시
        for (int i = 15; i < h; i = i + 20) {
            g.fillOval(x - 15, y - i, 4, 4);
            g.fillOval(x + 11, y - i, 4, 4);
        }

        if (getStage() == 3) {
            g.setColor(new Color(255, 105, 150));   // 분홍 꽃
            g.fillOval(x - 15, y - h - 12, 30, 20);
        }
    }
}
