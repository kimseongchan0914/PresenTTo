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
                new Couple100Days(),
                new ValentinesDay(),
                new WhiteDay(),
                new ParentsDay(),
                new TeachersDay(),
                new PeperoDay(),
                new Christmas()
        };
    }
}

/* ===== 아래는 Anniversary를 상속한 자식 클래스들 ===== */

/** 생일 */
class Birthday extends Anniversary {
    public Birthday() {
        super("생일", "사람마다 달라요");
    }

    @Override
    public String getMessage() {
        return "1년에 한 번뿐인 날! 받는 사람의 취향을 떠올리며 골라 보세요.";
    }
}

/** 커플 100일 */
class Couple100Days extends Anniversary {
    public Couple100Days() {
        super("커플 100일", "사귄 날 +100일");
    }

    @Override
    public String getMessage() {
        return "둘만의 추억이 담긴 선물로 100일을 기념해 보세요!";
    }
}

/** 발렌타인데이 */
class ValentinesDay extends Anniversary {
    public ValentinesDay() {
        super("발렌타인데이", "2월 14일");
    }

    @Override
    public String getMessage() {
        return "달콤한 초콜릿과 함께 좋아하는 마음을 전해 보세요!";
    }
}

/** 화이트데이 */
class WhiteDay extends Anniversary {
    public WhiteDay() {
        super("화이트데이", "3월 14일");
    }

    @Override
    public String getMessage() {
        return "받은 마음에 사탕처럼 달콤하게 답해 보세요!";
    }
}

/** 어버이날 */
class ParentsDay extends Anniversary {
    public ParentsDay() {
        super("어버이날", "5월 8일");
    }

    @Override
    public String getMessage() {
        return "비싼 선물보다 \"감사합니다\" 한마디가 가장 큰 선물이에요.";
    }
}

/** 스승의날 */
class TeachersDay extends Anniversary {
    public TeachersDay() {
        super("스승의날", "5월 15일");
    }

    @Override
    public String getMessage() {
        return "한 해 동안 가르쳐 주신 고마움을 정성으로 전해 보세요.";
    }
}

/** 빼빼로데이 */
class PeperoDay extends Anniversary {
    public PeperoDay() {
        super("빼빼로데이", "11월 11일");
    }

    @Override
    public String getMessage() {
        return "부담 없는 간식 선물로 고마운 마음을 가볍게 전해 보세요!";
    }
}

/** 크리스마스 */
class Christmas extends Anniversary {
    public Christmas() {
        super("크리스마스", "12월 25일");
    }

    @Override
    public String getMessage() {
        return "따뜻하고 포근한 선물로 연말 분위기를 함께 나눠 보세요!";
    }
}
