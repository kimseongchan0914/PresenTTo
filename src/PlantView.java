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
    private Font titleFont = new Font("Apple SD Gothic Neo", Font.BOLD, 26);
    private Font textFont = new Font("Apple SD Gothic Neo", Font.BOLD, 15);
    private Font speechFont = new Font("Apple SD Gothic Neo", Font.BOLD, 17);

    public PlantView() {
        setBackground(new Color(255, 248, 231));   // 크림색 배경
        setPreferredSize(new Dimension(540, 640));
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        repaint();   // 다시 그리기
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);   // 테두리를 부드럽게

        // 1. 제목 띠 (진한 초록 → 연한 초록 그라데이션)
        g.setPaint(new GradientPaint(0, 0, new Color(56, 142, 60), 540, 0, new Color(156, 204, 101)));
        g.fillRect(0, 0, 540, 76);
        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        g.drawString("우리집 반려식물", 26, 48);

        if (plant == null) {
            return;   // 아직 식물을 고르지 않았으면 여기까지만
        }

        // 2. 식물 카드 (흰색 둥근 사각형)
        g.setColor(Color.WHITE);
        g.fillRoundRect(20, 92, 500, 396, 28, 28);
        g.setColor(new Color(120, 100, 80));
        g.setFont(textFont);
        g.drawString(plant.getKind() + " \"" + plant.getName() + "\"   " + plant.getDay() + "일째   "
                + plant.getStageName() + " 단계", 42, 122);

        // 3. 말풍선
        g.setColor(new Color(232, 245, 233));
        g.fillRoundRect(150, 140, 240, 46, 46, 46);
        g.fillOval(262, 192, 14, 10);
        g.setColor(new Color(46, 125, 50));
        g.setFont(speechFont);
        String speech = plant.getSpeech();
        g.drawString(speech, 270 - speech.length() * 8, 170);   // 글자 수만큼 왼쪽으로 옮겨서 가운데쯤에

        // 4. 화분과 흙
        g.setColor(new Color(214, 120, 80));
        g.fillRoundRect(210, 400, 120, 80, 20, 20);
        g.setColor(new Color(186, 96, 60));
        g.fillRoundRect(200, 388, 140, 24, 10, 10);
        g.setColor(new Color(110, 80, 60));
        g.fillOval(214, 382, 112, 14);

        // 5. 식물 (건강이 50 아래면 누런색)
        Color leaf = new Color(76, 160, 80);
        if (plant.getHealth() < 50) {
            leaf = new Color(180, 165, 75);
        }
        if (plant.getStage() == 0) {
            g.setColor(new Color(160, 115, 65));   // 씨앗
            g.fillOval(258, 376, 24, 16);
        } else {
            plant.draw(g, 270, 390, leaf);   // 다형성: 종류에 맞는 그림이 그려짐
        }

        // 6. 상태 게이지
        g.setColor(Color.WHITE);
        g.fillRoundRect(20, 502, 500, 124, 28, 28);
        drawBar(g, "수분", plant.getWater(), 526, new Color(66, 165, 245));
        drawBar(g, "햇빛", plant.getSun(), 562, new Color(255, 193, 7));
        drawBar(g, "건강", plant.getHealth(), 598, new Color(102, 187, 106));
    }

    // 게이지 한 줄: 이름 + 회색 막대 + 값만큼 색칠한 막대 + 숫자
    private void drawBar(Graphics2D g, String label, int value, int y, Color color) {
        g.setColor(new Color(90, 80, 70));
        g.setFont(textFont);
        g.drawString(label, 44, y + 14);
        g.drawString("" + value, 464, y + 14);
        g.setColor(new Color(238, 238, 238));
        g.fillRoundRect(96, y, 350, 18, 18, 18);
        g.setColor(color);
        g.fillRoundRect(96, y, value * 350 / 100, 18, 18, 18);
    }
}
