import java.io.*;

/**
 * plant.txt에 식물 상태를 저장하고 불러오는 클래스
 *
 * [파일 입출력] PrintWriter로 쓰고, BufferedReader로 읽습니다.
 * [예외 처리] 파일이 없거나 내용이 깨져도 프로그램이 멈추지 않게 try-catch로 잡습니다.
 */
public class SaveManager {
    private static final String FILE_NAME = "plant.txt";

    // 한 줄에 값 하나씩 저장
    public static void save(Plant p) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            out.println(p.getKind());
            out.println(p.getName());
            out.println(p.getWater());
            out.println(p.getSun());
            out.println(p.getHealth());
            out.println(p.getDay());
            out.println(p.getStage());
            out.println(p.getGoodDays());
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
            Plant p;
            if ("몬스테라".equals(kind)) {
                p = new Monstera(name);
            } else if ("해바라기".equals(kind)) {
                p = new Sunflower(name);
            } else {
                p = new Cactus(name);
            }
            p.setWater(Integer.parseInt(in.readLine()));
            p.setSun(Integer.parseInt(in.readLine()));
            p.setHealth(Integer.parseInt(in.readLine()));
            p.setDay(Integer.parseInt(in.readLine()));
            p.setStage(Integer.parseInt(in.readLine()));
            p.setGoodDays(Integer.parseInt(in.readLine()));
            in.close();
            return p;
        } catch (IOException e) {
            return null;   // 파일이 없음 (처음 실행)
        } catch (NumberFormatException e) {
            return null;   // 파일 내용이 깨져서 숫자로 못 바꿈
        }
    }

    // 시들었거나 새로 시작할 때 저장 파일 지우기
    public static void delete() {
        new File(FILE_NAME).delete();
    }
}
