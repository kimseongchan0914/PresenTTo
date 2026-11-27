import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// plant.txt에 식물 상태를 저장하고 불러오는 클래스
public class SaveManager {
    private static final String FILE_NAME = "plant.txt";

    // 한 줄에 값 하나씩 저장
    public static void save(Plant plant) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            out.println(plant.getKind());
            out.println(plant.getName());
            out.println(plant.getWater());
            out.println(plant.getSun());
            out.println(plant.getHealth());
            out.println(plant.getDay());
            out.println(plant.getStage());
            out.println(plant.getGoodDays());
            out.close();
        } catch (IOException e) {
            System.out.println("저장 실패: " + e.getMessage());
        }
    }

    // 저장한 순서대로 읽기. 파일이 없거나 깨졌으면 null
    public static Plant load() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            String kind = in.readLine();
            String name = in.readLine();
            Plant plant;
            if ("몬스테라".equals(kind)) {
                plant = new Monstera(name);
            } else if ("해바라기".equals(kind)) {
                plant = new Sunflower(name);
            } else {
                plant = new Cactus(name);
            }
            plant.setWater(Integer.parseInt(in.readLine()));
            plant.setSun(Integer.parseInt(in.readLine()));
            plant.setHealth(Integer.parseInt(in.readLine()));
            plant.setDay(Integer.parseInt(in.readLine()));
            plant.setStage(Integer.parseInt(in.readLine()));
            plant.setGoodDays(Integer.parseInt(in.readLine()));
            in.close();
            return plant;
        } catch (IOException e) {
            return null;   // 파일이 없음 (처음 실행)
        } catch (NumberFormatException e) {
            return null;   // 파일 내용이 깨짐
        }
    }

    // 시들었거나 새로 시작할 때 저장 파일 지우기
    public static void delete() {
        new File(FILE_NAME).delete();
    }
}
