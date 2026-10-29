/**
 * 선물 한 개의 정보를 담는 클래스
 *
 * [캡슐화] 모든 변수를 private으로 숨기고 getter로만 값을 꺼낼 수 있게 했습니다.
 *         선물 정보는 실행 중에 바뀌면 안 되므로 setter는 만들지 않았습니다.
 */
public class Gift {

    private String name;          // 선물 이름
    private int price;            // 가격 (원)
    private String description;   // 한 줄 설명
    private String[] occasions;   // 어울리는 기념일들
    private String[] targets;     // 어울리는 받는 사람들

    public Gift(String name, int price, String description, String[] occasions, String[] targets) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.occasions = occasions;
        this.targets = targets;
    }

    /** 이 선물이 해당 기념일에 어울리는지 확인 */
    public boolean fitsOccasion(String occasion) {
        for (String o : occasions) {
            if (o.equals(occasion)) {
                return true;
            }
        }
        return false;
    }

    /** 이 선물이 해당 받는 사람에게 어울리는지 확인 */
    public boolean fitsTarget(String target) {
        for (String t : targets) {
            if (t.equals(target)) {
                return true;
            }
        }
        return false;
    }

}