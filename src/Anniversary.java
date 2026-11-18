import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 기념일 부모 클래스 (추상 클래스)
 *
 * 모든 기념일은 "이름", "날짜", "응원 문구"를 가집니다.
 * 응원 문구는 기념일마다 다르기 때문에 추상 메소드로 만들어서
 * 자식 클래스가 꼭 오버라이딩하도록 했습니다.
 *
 * 날짜가 정해진 기념일(크리스마스 등)은 D-day를 계산하고,
 * 날짜가 사람마다 다른 기념일(생일 등)은 getDate()와 getDaysLeft()를 오버라이딩합니다.
 */
public abstract class Anniversary {

    protected String name;   // 기념일 이름
    protected int month;     // 월 (날짜가 정해지지 않았으면 0)
    protected int day;       // 일

    /** 날짜가 정해진 기념일 (예: 크리스마스 12월 25일) */
    public Anniversary(String name, int month, int day) {
        this.name = name;
        this.month = month;
        this.day = day;
    }

    /** 날짜가 사람마다 다른 기념일 (예: 생일) */
    public Anniversary(String name) {
        this(name, 0, 0);
    }

    public String getName() {
        return name;
    }

    /** 칩 아래에 보여줄 날짜 글자 */
    public String getDate() {
        return month + "월 " + day + "일";
    }

    /** 오늘부터 다음 기념일까지 남은 날 수 (오늘이면 0, 날짜가 없으면 -1) */
    public int getDaysLeft() {
        LocalDate today = LocalDate.now();
        LocalDate next = LocalDate.of(today.getYear(), month, day);
        if (next.isBefore(today)) {
            next = next.plusYears(1);   // 올해 날짜가 지났으면 내년 날짜로
        }
        return (int) ChronoUnit.DAYS.between(today, next);
    }

    /** "D-37", "D-DAY" 같은 글자 (D-day가 없으면 null) */
    public String getDdayText() {
        int left = getDaysLeft();
        if (left < 0) {
            return null;
        }
        if (left == 0) {
            return "D-DAY";
        }
        return "D-" + left;
    }

    /** 기념일마다 다른 응원 문구 (자식 클래스에서 구현) */
    public abstract String getMessage();

    /** 콤보박스에 기념일 이름이 보이도록 오버라이딩 */
    @Override
    public String toString() {
        return name;
    }
}
