import java.awt.*;

/**
 * 해바라기: 햇빛을 제일 좋아하고, 햇빛이 부족하면(30 미만) 크게 아픕니다.
 *
 * [상속] Plant를 상속(extends)해서 변수와 getter / setter를 물려받습니다.
 * [오버라이딩] sunbathe()를 바꿔서 햇빛을 한 번에 +30 받게 했습니다.
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

    // 하루가 지나면: 수분 -15, 햇빛 -20 (햇빛을 많이 씀)
    @Override
    public void nextDay() {
        setWater(getWater() - 15);
        setSun(getSun() - 20);
        if (getSun() < 30) {
            setHealth(getHealth() - 25);   // 햇빛이 부족하면 크게 아픔
        } else if (getWater() >= 30) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        super.nextDay();
    }

    @Override
    public String getSpeech() {
        if (getSun() < 30) {
            return "해님이 필요해요!";
        } else if (getWater() < 30) {
            return "목말라요!";
        } else if (getSun() >= 70) {
            return "햇빛 좋다~";
        } else {
            return "기분 최고!";
        }
    }

    // 긴 줄기 + 잎, 줄기 단계에 꽃봉오리, 꽃 단계에 노란 꽃
    @Override
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int h = getStage() * 45;   // 키: 45, 90, 135

        g.setColor(leaf.darker());
        g.fillRect(x - 3, y - h, 6, h);   // 줄기

        g.setColor(leaf);
        for (int i = 1; i <= getStage(); i++) {   // 잎이 단계마다 한 쌍씩
            g.fillOval(x - 40, y - i * 38, 38, 18);
            g.fillOval(x + 2, y - i * 38 - 8, 38, 18);
        }

        if (getStage() == 2) {
            g.fillOval(x - 14, y - h - 20, 28, 28);   // 꽃봉오리
        }

        if (getStage() == 3) {
            g.setColor(new Color(255, 200, 30));       // 노란 꽃잎
            g.fillOval(x - 50, y - h - 15, 100, 30);
            g.fillOval(x - 15, y - h - 50, 30, 100);
            g.fillOval(x - 36, y - h - 36, 72, 72);
            g.setColor(new Color(120, 75, 30));        // 갈색 가운데
            g.fillOval(x - 20, y - h - 20, 40, 40);
        }
    }
}
