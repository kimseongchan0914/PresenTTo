import java.awt.*;

/**
 * 몬스테라: 물을 좋아하고, 강한 햇빛(70 이상)은 싫어합니다.
 *
 * [상속] Plant를 상속(extends)해서 변수와 getter / setter를 물려받습니다.
 * [오버라이딩] water()는 +30, sunbathe()는 햇빛이 70 이상이면 잎이 타게 했습니다.
 */
public class Monstera extends Plant {

    public Monstera(String name) {
        super(name);
    }

    @Override
    public String getKind() {
        return "몬스테라";
    }

    // 물을 좋아해서 한 번에 많이 마심
    @Override
    public void water() {
        setWater(getWater() + 30);
    }

    // 햇빛이 70 이상이면 잎이 타서 건강이 떨어짐
    @Override
    public void sunbathe() {
        super.sunbathe();
        if (getSun() >= 70) {
            setHealth(getHealth() - 10);
        }
    }

    // 하루가 지나면: 수분 -20 (빨리 마심), 햇빛 -10
    @Override
    public void nextDay() {
        setWater(getWater() - 20);
        setSun(getSun() - 10);
        if (getWater() >= 40 && getSun() >= 20 && getSun() < 70) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        super.nextDay();
    }

    @Override
    public String getSpeech() {
        if (getSun() >= 70) {
            return "앗 뜨거! 잎이 타요!";
        } else if (getWater() < 40) {
            return "목말라요!";
        } else if (getHealth() >= 80) {
            return "기분 최고!";
        } else {
            return "촉촉해서 좋아~";
        }
    }

    // 줄기 + 구멍 뚫린 큰 잎, 꽃 단계에 하얀 꽃
    @Override
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int h = getStage() * 45;   // 키: 45, 90, 135

        g.setColor(leaf.darker());
        g.fillRect(x - 3, y - h, 6, h);   // 줄기

        drawLeaf(g, x, y - h, leaf);   // 맨 위 잎
        if (getStage() >= 2) {
            drawLeaf(g, x - 38, y - h + 60, leaf);   // 왼쪽 잎
            drawLeaf(g, x + 38, y - h + 35, leaf);   // 오른쪽 잎
        }

        if (getStage() == 3) {
            g.setColor(new Color(245, 228, 190));   // 하얀 꽃
            g.fillOval(x + 30, y - h - 45, 30, 50);
            g.setColor(new Color(240, 190, 60));    // 노란 꽃술
            g.fillOval(x + 39, y - h - 35, 12, 30);
        }
    }

    // 잎 하나: 초록 타원에 하얀 구멍 두 개
    private void drawLeaf(Graphics2D g, int x, int y, Color leaf) {
        g.setColor(leaf);
        g.fillOval(x - 35, y - 25, 70, 50);
        g.setColor(Color.WHITE);
        g.fillOval(x - 22, y - 6, 10, 12);
        g.fillOval(x + 12, y - 6, 10, 12);
    }
}
