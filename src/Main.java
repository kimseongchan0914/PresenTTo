import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 프로그램 창: JFrame을 상속하고, 버튼 클릭은 ActionListener로 처리
public class Main extends JFrame implements ActionListener {
    public static final Color CREAM = new Color(255, 248, 231);

    private Plant plant;   // 다형성: 어떤 식물이든 Plant 변수 하나로 다룸
    private PlantView view = new PlantView();
    private RoundButton waterButton = new RoundButton("물 주기", new Color(66, 165, 245));
    private RoundButton sunButton = new RoundButton("햇빛 쬐기", new Color(255, 167, 38));
    private RoundButton feedButton = new RoundButton("영양제", new Color(102, 187, 106));
    private RoundButton dayButton = new RoundButton("하루 보내기", new Color(141, 110, 99));

    public Main() {
        setTitle("우리집 반려식물");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 14));
        buttons.setBackground(CREAM);
        RoundButton[] all = {waterButton, sunButton, feedButton, dayButton};
        for (int i = 0; i < all.length; i++) {
            all[i].addActionListener(this);
            buttons.add(all[i]);
        }
        add(view, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    // 시작: 저장 파일이 있으면 이어서 키울지 묻기
    public void start() {
        Plant saved = SaveManager.load();
        if (saved != null) {
            int answer = JOptionPane.showConfirmDialog(this, saved.getKind() + " \"" + saved.getName()
                    + "\" (" + saved.getDay() + "일째)\n이어서 키울까요?", "이어서 키우기", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                plant = saved;
                view.setPlant(plant);
                return;
            }
        }
        newPlant();
    }

    // 새 식물 고르고 이름 짓기
    private void newPlant() {
        String[] kinds = {"선인장", "몬스테라", "해바라기"};
        String info = "어떤 식물을 키울까요?\n\n선인장 : 물은 아주 조금만 좋아해요\n"
                + "몬스테라 : 물을 좋아하지만 강한 햇빛은 싫어해요\n해바라기 : 햇빛을 제일 좋아해요";
        int choice = JOptionPane.showOptionDialog(this, info, "식물 고르기",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, kinds, kinds[0]);
        String name = JOptionPane.showInputDialog(this, "이름을 지어 주세요");
        if (name == null || name.trim().isEmpty()) name = "초록이";

        if (choice == 1) plant = new Monstera(name.trim());
        else if (choice == 2) plant = new Sunflower(name.trim());
        else plant = new Cactus(name.trim());   // 창을 닫아도 선인장
        view.setPlant(plant);
        SaveManager.save(plant);
    }

    // 버튼을 누르면 실행됨 (ActionListener의 메소드)
    public void actionPerformed(ActionEvent e) {
        int oldStage = plant.getStage();
        if (e.getSource() == waterButton) plant.water();
        else if (e.getSource() == sunButton) plant.sunbathe();
        else if (e.getSource() == dayButton) plant.nextDay();
        else if (!plant.feed()) JOptionPane.showMessageDialog(this, "영양제는 하루에 한 번만 줄 수 있어요.");
        view.repaint();
        SaveManager.save(plant);

        if (plant.isDead()) {
            JOptionPane.showMessageDialog(this, plant.getName() + "이(가) 시들었어요...\n처음부터 다시 시작해요.");
            SaveManager.delete();
            newPlant();
        } else if (plant.getStage() > oldStage && plant.isFlower()) {
            int answer = JOptionPane.showConfirmDialog(this, "키우기 성공!\n" + plant.getName() + "이(가) "
                    + plant.getDay() + "일째에 꽃을 피웠어요.\n\n새 식물을 키워 볼까요?", "축하해요", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                SaveManager.delete();
                newPlant();
            }
        } else if (plant.getStage() > oldStage) {
            JOptionPane.showMessageDialog(this, "쑥쑥! " + plant.getStageName() + " 단계가 되었어요.");
        }
    }

    public static void main(String[] args) {
        Font font = new Font("Apple SD Gothic Neo", Font.PLAIN, 14);   // 대화상자도 한글 글꼴로
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("OptionPane.buttonFont", font);
        UIManager.put("TextField.font", font);
        Main main = new Main();
        main.setVisible(true);
        main.start();
    }
}
