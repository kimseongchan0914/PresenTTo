/**
 * 기념일 목록을 만들어 주는 클래스
 *
 * [다형성] 부모 타입(Anniversary) 배열 하나에 서로 다른 자식 객체들을 담습니다.
 *         getMessage()를 호출하면 각 객체에 맞는 문구가 자동으로 나옵니다.
 * [상속] 모든 기념일 클래스는 추상 클래스 Anniversary를 상속하고 getMessage()를 오버라이딩합니다.
 */
public class Anniversaries {

    public static Anniversary[] getAll() {
        return new Anniversary[] {
                new Birthday(),
                new Couple100Days(),
                new Graduation(),
                new ParentsDay(),
                new TeachersDay(),
                new PeperoDay(),
                new Christmas()
        };
    }
}


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

/** 커플 100일 */
class Couple100Days extends Anniversary {
    public Couple100Days() {
        super("커플 100일");
    }

    @Override
    public String getMessage() {
        return "둘만의 추억이 담긴 선물로 100일을 기념해 보세요!";
    }
}

/** 졸업·입학 */
class Graduation extends Anniversary {
    public Graduation() {
        super("졸업·입학");
    }

    @Override
    public String getMessage() {
        return "새로운 출발을 응원하는 마음을 담아 보세요!";
    }
}

/** 어버이날 */
class ParentsDay extends Anniversary {
    public ParentsDay() {
        super("어버이날");
    }

    @Override
    public String getMessage() {
        return "비싼 선물보다 \"감사합니다\" 한마디가 가장 큰 선물이에요.";
    }
}

/** 스승의날 */
class TeachersDay extends Anniversary {
    public TeachersDay() {
        super("스승의날");
    }

    @Override
    public String getMessage() {
        return "한 해 동안 가르쳐 주신 고마움을 정성으로 전해 보세요.";
    }
}

/** 빼빼로데이 */
class PeperoDay extends Anniversary {
    public PeperoDay() {
        super("빼빼로데이");
    }

    @Override
    public String getMessage() {
        return "부담 없는 간식 선물로 고마운 마음을 가볍게 전해 보세요!";
    }
}

/** 크리스마스 */
class Christmas extends Anniversary {
    public Christmas() {
        super("크리스마스");
    }

    @Override
    public String getMessage() {
        return "따뜻하고 포근한 선물로 연말 분위기를 함께 나눠 보세요!";
    }
}
