import java.awt.*;

/**
 * 해바라기: 햇빛을 제일 좋아하고, 햇빛이 부족하면(30 미만) 크게 아픕니다.
 *
 * [상속] Plant를 상속(extends)해서 상태 변수와 getter / setter를 물려받습니다.
 * [오버라이딩] 햇빛은 한 번에 +30, 햇빛이 부족하면 건강 -25로 바꿨습니다.
 */
public class Sunflower extends Plant {

    public Sunflower(String name) {
        super(name);
    }

    @Override
    public String getKind() {
        return "해바라기";
    }

    // 햇빛을 좋아해서 한 번에 많이 받음
    @Override
    public void sunbathe() {
        setSun(getSun() + 30);
    }

    @Override
    public void nextDay() {
        setWater(getWater() - 15);
        setSun(getSun() - 20);       // 햇빛을 많이 씀
        if (getSun() < 30) {
            setHealth(getHealth() - 25);   // 햇빛 부족은 크게 아픔
        } else if (getWater() >= 30) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        endDay();
    }

    @Override
    public String getSpeech() {
        if (getHealth() < 30) {
            return "몸이 안 좋아요...";
        }
        if (getSun() < 30) {
            return "해님... 해님이 필요해!";
        }
        if (getWater() < 30) {
            return "목말라요!";
        }
        if (isFlower()) {
            return "활짝 피었어요!";
        }
        if (getSun() >= 70) {
            return "햇빛 좋다~";
        }
        if (getHealth() >= 80) {
            return "기분 최고!";
        }
        return "해님 쪽을 보고 있어요";
    }

    // 긴 줄기 + 잎, 줄기 단계에 꽃봉오리, 꽃 단계에 노란 꽃
    @Override
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int h = 45 * getStage();   // 키: 45, 90, 135
        g.setColor(leaf.darker());
        g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x, y, x, y - h);

        g.setColor(leaf);   // 잎은 단계마다 한 쌍씩 늘어남
        for (int i = 1; i <= getStage(); i++) {
            int ly = y - i * 38 + 10;
            g.fillOval(x - 44, ly - 10, 40, 20);
            g.fillOval(x + 4, ly - 18, 40, 20);
        }
        if (getStage() == 2) {
            g.fillOval(x - 14, y - h - 22, 28, 28);   // 꽃봉오리
        }

        if (isFlower()) {
            g.setColor(new Color(255, 200, 30));   // 꽃잎 12장을 빙 둘러 그림
            for (int i = 0; i < 12; i++) {
                double angle = Math.PI * 2 * i / 12;
                int px = x + (int) (Math.cos(angle) * 28);
                int py = y - h + (int) (Math.sin(angle) * 28);
                g.fillOval(px - 12, py - 12, 24, 24);
            }
            g.setColor(new Color(120, 75, 30));
            g.fillOval(x - 22, y - h - 22, 44, 44);
        }
    }
}
