import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

// 몬스테라: 물을 좋아하고, 강한 햇빛(70 이상)은 싫어함
public class Monstera extends Plant {

    public Monstera(String name) {
        super(name);
    }

    public String getKind() {
        return "몬스테라";
    }

    // 물을 좋아해서 한 번에 많이 마심
    public void water() {
        setWater(getWater() + 30);
    }

    // 햇빛이 70 이상이면 잎이 타서 건강이 떨어짐
    public void sunbathe() {
        super.sunbathe();
        if (getSun() >= 70) {
            setHealth(getHealth() - 10);
        }
    }

    public void nextDay() {
        setWater(getWater() - 20);   // 물을 빨리 마심
        setSun(getSun() - 10);
        if (getWater() >= 40 && getSun() >= 20 && getSun() < 70) {
            setHealth(getHealth() + 10);
        } else {
            setHealth(getHealth() - 10);
        }
        endDay();
    }

    public String getSpeech() {
        if (getHealth() < 30) return "몸이 안 좋아요...";
        if (getSun() >= 70) return "앗 뜨거! 잎이 타요!";
        if (getWater() < 40) return "목말라요!";
        if (getSun() < 20) return "너무 어두워요...";
        if (isFlower()) return "하얀 꽃이 폈어요!";
        if (getHealth() >= 80) return "잎이 쑥쑥 커요!";
        return "촉촉해서 좋아~";
    }

    // 줄기 끝마다 구멍 뚫린 큰 잎, 꽃 단계에 하얀 꽃
    public void draw(Graphics2D g, int x, int y, Color leaf) {
        int[] lx;
        int[] ly;
        int size;
        if (getStage() == 1) {
            lx = new int[] {10};
            ly = new int[] {-40};
            size = 50;
        } else {
            lx = new int[] {-54, 56, -6};
            ly = new int[] {-66, -88, -124};
            size = 60 + 12 * getStage();   // 84, 96
        }

        g.setColor(leaf.darker());
        g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < lx.length; i++) {
            g.drawLine(x, y, x + lx[i], y + ly[i]);
        }
        for (int i = 0; i < lx.length; i++) {
            drawLeaf(g, x + lx[i], y + ly[i], size, leaf);
        }

        if (isFlower()) {
            g.setColor(leaf.darker());
            g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(x, y, x + 70, y - 150);              // 꽃대
            g.setColor(new Color(255, 250, 235));
            g.fillOval(x + 52, y - 210, 40, 64);             // 하얀 꽃잎
            g.setColor(new Color(200, 190, 160));
            g.setStroke(new BasicStroke(2));
            g.drawOval(x + 52, y - 210, 40, 64);
            g.setColor(new Color(240, 190, 60));
            g.fillRoundRect(x + 65, y - 198, 14, 40, 14, 14); // 노란 꽃술
        }
    }

    // 갈라진 틈이 있는 몬스테라 잎 하나
    private void drawLeaf(Graphics2D g, int cx, int cy, int size, Color leaf) {
        int h = size * 4 / 5;
        g.setColor(leaf);
        g.fillOval(cx - size / 2, cy - h / 2, size, h);
        g.setColor(Color.WHITE);   // 카드 색으로 틈을 그림
        g.setStroke(new BasicStroke(3));
        for (int i = -1; i <= 1; i++) {
            int dy = i * h / 4;
            g.drawLine(cx - size / 2, cy + dy, cx - size / 5, cy + dy / 2);
            g.drawLine(cx + size / 2, cy + dy, cx + size / 5, cy + dy / 2);
        }
        g.setColor(leaf.darker());
        g.drawLine(cx, cy - h / 2 + 4, cx, cy + h / 2 - 4);   // 잎맥
    }
}
