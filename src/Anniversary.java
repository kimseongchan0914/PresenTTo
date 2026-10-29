/**
 * 기념일 부모 클래스 (추상 클래스)
 *
 * 모든 기념일은 "이름"과 "응원 문구"를 가집니다.
 * 응원 문구는 기념일마다 다르기 때문에 추상 메소드로 만들어서
 * 자식 클래스가 꼭 오버라이딩하도록 했습니다.
 */
public abstract class Anniversary {

    protected String name;   // 기념일 이름

    public Anniversary(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** 기념일마다 다른 응원 문구 (자식 클래스에서 구현) */
    public abstract String getMessage();

    /** 콤보박스에 기념일 이름이 보이도록 오버라이딩 */
    @Override
    public String toString() {
        return name;
    }
}