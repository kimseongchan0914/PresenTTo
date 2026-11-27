import javax.swing.*;
import java.awt.*;

/**
 * 제목 띠, 식물 그림, 말풍선, 상태 게이지를 그리는 패널
 *
 * [상속] JPanel을 상속하고 paintComponent()를 오버라이딩해서 직접 그립니다.
 * [다형성] plant.draw()를 부르면 식물 종류에 맞는 그림이 그려집니다.
 */
public class PlantView extends JPanel {
    private Plant plant;

    public PlantView() {
        setBackground(Main.CREAM);
        setPreferredSize(new Dimension(540, 640));
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        repaint();
    }

    // 한글 글꼴 만들기
    public static Font font(int size) {
        return new Font("Apple SD Gothic Neo", Font.BOLD, size);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();

        // 1. 초록색 그라데이션 제목 띠
        g.setPaint(new GradientPaint(0, 0, new Color(56, 142, 60), w, 0, new Color(156, 204, 101)));
        g.fillRect(0, 0, w, 76);
        g.setColor(Color.WHITE);
        g.setFont(font(26));
        g.drawString("우리집 반려식물", 26, 48);
        if (plant == null) {
            return;
        }
        g.setFont(font(16));
        String title = plant.getKind() + "  \"" + plant.getName() + "\"";
        g.drawString(title, w - 26 - g.getFontMetrics().stringWidth(title), 47);

        // 2. 식물 카드: 날짜와 단계, 화분, 식물, 말풍선
        drawCard(g, 20, 92, w - 40, 396);
        g.setColor(new Color(120, 100, 80));
        g.setFont(font(15));
        g.drawString(plant.getDay() + "일째  ·  " + plant.getStageName() + " 단계", 42, 122);
        if (!plant.isFlower()) {
            g.drawString("건강한 날 " + plant.getGoodDays() + " / 3", w - 150, 122);
        }

        int x = w / 2;
        int y = 390;   // 흙 높이
        drawPot(g, x, y);
        if (plant.getStage() == 0) {
            g.setColor(new Color(160, 115, 65));   // 씨앗
            g.fillOval(x - 12, y - 14, 24, 16);
        } else {
            plant.draw(g, x, y, leafColor());   // 다형성: 종류에 맞게 그려짐
        }
        drawBubble(g, plant.getSpeech(), x, 132);

        // 3. 게이지 카드
        drawCard(g, 20, 502, w - 40, 124);
        drawBar(g, "수분", plant.getWater(), 526, new Color(66, 165, 245));
        drawBar(g, "햇빛", plant.getSun(), 562, new Color(255, 193, 7));
        drawBar(g, "건강", plant.getHealth(), 598, new Color(102, 187, 106));
    }

    // 건강할수록 초록, 아플수록 누런색
    private Color leafColor() {
        if (plant.getHealth() >= 50) {
            return new Color(76, 160, 80);
        }
        if (plant.getHealth() >= 25) {
            return new Color(160, 170, 70);
        }
        return new Color(190, 160, 80);
    }

    // 그림자가 있는 둥근 흰 카드
    private void drawCard(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(232, 222, 200));
        g.fillRoundRect(x + 3, y + 4, w, h, 28, 28);
        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, w, h, 28, 28);
    }

    // 주황색 화분과 흙
    private void drawPot(Graphics2D g, int x, int y) {
        int[] px = {x - 64, x + 64, x + 48, x - 48};
        int[] py = {y + 10, y + 10, y + 88, y + 88};
        g.setColor(new Color(214, 120, 80));
        g.fillPolygon(px, py, 4);
        g.setColor(new Color(186, 96, 60));
        g.fillRoundRect(x - 74, y - 4, 148, 24, 10, 10);
        g.setColor(new Color(110, 80, 60));
        g.fillOval(x - 64, y - 8, 128, 14);
    }

    // 꼬리가 달린 말풍선
    private void drawBubble(Graphics2D g, String text, int x, int y) {
        g.setFont(font(17));
        int bw = g.getFontMetrics().stringWidth(text) + 44;
        g.setColor(new Color(232, 245, 233));
        g.fillRoundRect(x - bw / 2, y, bw, 46, 46, 46);
        int[] tx = {x - 10, x + 10, x};
        int[] ty = {y + 44, y + 44, y + 60};
        g.fillPolygon(tx, ty, 3);
        g.setColor(new Color(46, 125, 50));
        g.drawString(text, x - bw / 2 + 22, y + 30);
    }

    // 이름 + 둥근 게이지 바 + 숫자
    private void drawBar(Graphics2D g, String label, int value, int y, Color color) {
        int barW = getWidth() - 190;
        g.setFont(font(15));
        g.setColor(new Color(90, 80, 70));
        g.drawString(label, 44, y + 14);
        g.drawString("" + value, getWidth() - 78, y + 14);
        g.setColor(new Color(238, 238, 238));
        g.fillRoundRect(96, y, barW, 18, 18, 18);
        g.setColor(color);
        g.fillRoundRect(96, y, barW * value / 100, 18, 18, 18);
    }
}
