/**
 * 날짜가 사람마다 다른 기념일의 부모 클래스 (예: 생일, 커플 100일, 졸업·입학)
 *
 * [상속] Anniversary → FlexibleAnniversary → Birthday 처럼 3단계로 상속합니다.
 * [오버라이딩] 날짜를 계산할 수 없으니 getDate()는 안내 글자를, getDaysLeft()는 -1을 돌려줍니다.
 *             자식 클래스들은 이 두 메소드를 따로 오버라이딩하지 않아도 됩니다.
 */
public abstract class FlexibleAnniversary extends Anniversary {

    private String dateText;   // 칩 아래에 보여줄 안내 글자 (예: "사람마다 달라요")

    public FlexibleAnniversary(String name, String dateText) {
        super(name);
        this.dateText = dateText;
    }

    @Override
    public String getDate() {
        return dateText;
    }

    /** D-day를 계산할 수 없으므로 -1 */
    @Override
    public int getDaysLeft() {
        return -1;
    }
}
