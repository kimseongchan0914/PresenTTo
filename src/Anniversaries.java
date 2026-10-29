/**
 * 기념일 목록을 만들어 주는 클래스
 *
 * [다형성] 부모 타입(Anniversary) 배열 하나에 서로 다른 자식 객체들을 담습니다.
 *         getMessage()를 호출하면 각 객체에 맞는 문구가 자동으로 나옵니다.
 */
public class Anniversaries {

    public static Anniversary[] getAll() {
        return new Anniversary[] {
                new Birthday(),
                new Christmas(),
                new ParentsDay(),
                new PeperoDay()
        };
    }
}

/* ===== 아래는 Anniversary를 상속한 자식 클래스들 ===== */

/** 생일 */
class Birthday extends Anniversary {
    public Birthday() {
        super("생일");
    }

    @Override
    public String getMessage() {
        return "1년에 한 번뿐인 날! 받는 사람의 취향을 떠올리며 골라 보세요.";
    }
}

