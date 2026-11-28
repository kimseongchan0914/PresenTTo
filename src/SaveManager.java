import java.io.*;

/**
 * plant.txt에 식물 상태를 저장하고 불러오는 클래스
 *
 * [파일 입출력] PrintWriter로 쓰고, BufferedReader로 읽습니다.
 * [예외 처리] 파일이 없거나 내용이 깨져도 프로그램이 멈추지 않게 try-catch로 잡습니다.
 */
public class SaveManager {

    // 한 줄에 값 하나씩 저장
    public static void save(Plant plant) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("plant.txt"));
            out.println(plant.getKind());
            out.println(plant.getName());
            out.println(plant.getWater());
            out.println(plant.getSun());
            out.println(plant.getHealth());
            out.println(plant.getDay());
            out.println(plant.getGoodDays());
            out.close();
        } catch (IOException e) {
            System.out.println("저장 실패");
        }
    }

    // 저장한 순서대로 한 줄씩 읽기 (파일이 없거나 깨졌으면 null)
    public static Plant load() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("plant.txt"));
            String kind = in.readLine();
            String name = in.readLine();

            Plant plant;
            if (kind.equals("몬스테라")) {
                plant = new Monstera(name);
            } else if (kind.equals("해바라기")) {
                plant = new Sunflower(name);
            } else {
                plant = new Cactus(name);
            }

            plant.setWater(Integer.parseInt(in.readLine()));
            plant.setSun(Integer.parseInt(in.readLine()));
            plant.setHealth(Integer.parseInt(in.readLine()));
            plant.setDay(Integer.parseInt(in.readLine()));
            plant.setGoodDays(Integer.parseInt(in.readLine()));
            in.close();
            return plant;
        } catch (Exception e) {
            return null;
        }
    }
}
