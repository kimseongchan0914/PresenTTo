import java.awt.*;

/**
 * 선인장: 물을 싫어하고, 물이 천천히 줄어듭니다.
 *
 * [상속] Plant를 상속(extends)해서 상태 변수와 getter / setter를 물려받습니다.
 * [오버라이딩] 물을 많이 주면(60 이상) 건강이 떨어지도록 water()를 바꿨습니다.
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
        if (getWater() >= 60) setHealth(getHealth() - 10);
    }

    @Override
    public void nextDay() {
        setWater(getWater() - 5);    // 물이 천천히 줄어듦
        setSun(getSun() - 15);
        if (getWater() >= 60) {
            setHealth(getHealth() - 15);   // 뿌리가 썩음
        } else if (getWater() >= 10 && getSun() >= 30) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        endDay();
    }

    @Override
    public String getSpeech() {
        if (getHealth() < 30) return "몸이 안 좋아요...";
        if (getWater() >= 60) return "물 그만 줘...!";
        if (getWater() < 10) return "물 아주 조금만...";
        if (getSun() < 30) return "햇빛이 그리워...";
        if (isFlower()) return "가시 사이로 꽃이 폈어!";
        if (getHealth() >= 80) return "기분 최고!";
        return "난 물 없이도 튼튼해~";
    }

    // 둥근 몸통 + 가시, 줄기 단계부터 팔, 꽃 단계에 분홍 꽃
    @Override
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int h = 40 * getStage();        // 키: 40, 80, 120
        int w = 34 + 8 * getStage();    // 굵기: 42, 50, 58
        g.setColor(leaf);
        if (getStage() >= 2) {
            g.fillRoundRect(x - w / 2 - 26, y - h / 2, 34, 16, 16, 16);       // 왼팔
            g.fillRoundRect(x - w / 2 - 26, y - h / 2 - 34, 16, 50, 16, 16);
            g.fillRoundRect(x + w / 2 - 8, y - h / 2 - 16, 34, 16, 16, 16);   // 오른팔
            g.fillRoundRect(x + w / 2 + 10, y - h / 2 - 46, 16, 46, 16, 16);
        }
        g.fillRoundRect(x - w / 2, y - h, w, h + 4, w, w);   // 몸통

        g.setColor(new Color(255, 250, 220));   // 가시
        g.setStroke(new BasicStroke(2));
        for (int i = y - h + w / 2; i < y - 4; i += 16) {
            g.drawLine(x - w / 2, i, x - w / 2 - 6, i - 4);
            g.drawLine(x + w / 2, i, x + w / 2 + 6, i - 4);
            g.drawLine(x, i + 6, x, i + 1);
        }

        if (isFlower()) {
            g.setColor(new Color(255, 105, 150));
            g.fillOval(x - 18, y - h - 14, 36, 24);
            g.setColor(new Color(255, 220, 90));
            g.fillOval(x - 6, y - h - 8, 12, 12);
        }
    }
}
