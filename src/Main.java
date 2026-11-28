import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 프로그램 창과 버튼 처리
 *
 * [상속] JFrame을 상속해서 창이 됩니다.
 * [인터페이스] ActionListener를 구현(implements)해서 버튼 클릭을 처리합니다.
 * [다형성] Plant 변수 하나로 선인장 / 몬스테라 / 해바라기를 똑같이 다룹니다.
 */
public class Main extends JFrame implements ActionListener {

    private Plant plant;   // 어떤 식물이든 담을 수 있음 (다형성)
    private PlantView view = new PlantView();
    private RoundButton waterButton = new RoundButton("물 주기", new Color(66, 165, 245));
    private RoundButton sunButton = new RoundButton("햇빛 쬐기", new Color(255, 167, 38));
    private RoundButton feedButton = new RoundButton("영양제", new Color(102, 187, 106));
    private RoundButton dayButton = new RoundButton("하루 보내기", new Color(141, 110, 99));

    public Main() {
        setTitle("우리집 반려식물");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // 버튼을 누르면 이 클래스의 actionPerformed()가 실행되게 연결
        waterButton.addActionListener(this);
        sunButton.addActionListener(this);
        feedButton.addActionListener(this);
        dayButton.addActionListener(this);

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(255, 248, 231));
        buttons.add(waterButton);
        buttons.add(sunButton);
        buttons.add(feedButton);
        buttons.add(dayButton);

        add(view, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);   // 화면 가운데에
        setVisible(true);
    }

    // 시작: 저장된 식물이 있으면 이어서 키울지 묻기
    public void start() {
        plant = SaveManager.load();
        if (plant != null) {
            int answer = JOptionPane.showConfirmDialog(this, plant.getName() + "을(를) 이어서 키울까요?");
            if (answer != JOptionPane.YES_OPTION) {
                plant = null;
            }
        }
        if (plant == null) {
            newPlant();
        }
        view.setPlant(plant);
    }

    // 새 식물 고르고 이름 짓기
    private void newPlant() {
        String[] kinds = {"선인장", "몬스테라", "해바라기"};
        // 버튼 3개짜리 선택 창: 누른 버튼의 번호(0, 1, 2)가 돌아옴
        int choice = JOptionPane.showOptionDialog(this, "어떤 식물을 키울까요?", "식물 고르기",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, kinds, kinds[0]);
        String name = JOptionPane.showInputDialog(this, "이름을 지어 주세요");
        if (name == null || name.isEmpty()) {
            name = "초록이";
        }

        if (choice == 1) {
            plant = new Monstera(name);
        } else if (choice == 2) {
            plant = new Sunflower(name);
        } else {
            plant = new Cactus(name);
        }
        view.setPlant(plant);
    }

    // 버튼을 누르면 실행됨
    @Override
    public void actionPerformed(ActionEvent e) {
        int oldStage = plant.getStage();

        // 다형성: 어떤 식물이든 똑같이 plant.water() 처럼 부름
        if (e.getSource() == waterButton) {
            plant.water();
        } else if (e.getSource() == sunButton) {
            plant.sunbathe();
        } else if (e.getSource() == feedButton) {
            plant.feed();
        } else if (e.getSource() == dayButton) {
            plant.nextDay();
        }
        view.repaint();

        if (plant.getHealth() == 0) {
            JOptionPane.showMessageDialog(this, "시들었어요... 처음부터 다시 시작해요.");
            newPlant();
        } else if (plant.getStage() == 3 && oldStage < 3) {
            JOptionPane.showMessageDialog(this, "키우기 성공! 꽃이 피었어요!");
        } else if (plant.getStage() > oldStage) {
            JOptionPane.showMessageDialog(this, "쑥쑥! " + plant.getStageName() + " 단계가 되었어요.");
        }
        SaveManager.save(plant);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
}
