import java.io.*;

// plant.txt에 식물 상태를 저장하고 불러오는 클래스
public class SaveManager {
    private static final String FILE_NAME = "plant.txt";

    // 한 줄에 값 하나씩 저장
    public static void save(Plant p) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            out.println(p.getKind());
            out.println(p.getName());
            out.println(p.getWater() + "," + p.getSun() + "," + p.getHealth() + ","
                    + p.getDay() + "," + p.getStage() + "," + p.getGoodDays());
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
            String[] v = in.readLine().split(",");   // 숫자 6개
            in.close();

            Plant p;
            if ("몬스테라".equals(kind)) p = new Monstera(name);
            else if ("해바라기".equals(kind)) p = new Sunflower(name);
            else p = new Cactus(name);
            p.setWater(Integer.parseInt(v[0]));
            p.setSun(Integer.parseInt(v[1]));
            p.setHealth(Integer.parseInt(v[2]));
            p.setDay(Integer.parseInt(v[3]));
            p.setStage(Integer.parseInt(v[4]));
            p.setGoodDays(Integer.parseInt(v[5]));
            return p;
        } catch (IOException e) {
            return null;   // 파일이 없음 (처음 실행)
        } catch (Exception e) {
            return null;   // 파일 내용이 깨짐
        }
    }

    // 시들었거나 새로 시작할 때 저장 파일 지우기
    public static void delete() {
        new File(FILE_NAME).delete();
    }
}
