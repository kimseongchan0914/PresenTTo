/**
 * 기념일 목록을 만들어 주는 클래스
 *
 * [다형성] 부모 타입(Anniversary) 배열 하나에 서로 다른 자식 객체들을 담습니다.
 *         getMessage()를 호출하면 각 객체에 맞는 문구가 자동으로 나옵니다.
 * [상속] 날짜가 정해진 기념일은 Anniversary를, 날짜가 사람마다 다른 기념일은 FlexibleAnniversary를 상속합니다.
 */
public class Anniversaries {

    public static Anniversary[] getAll() {
        return new Anniversary[] {
                new Birthday(),
                new Couple100Days(),
                new CoupleAnniversary(),
                new Graduation(),
                new Promotion(),
                new LunarNewYear(),
                new ValentinesDay(),
                new WhiteDay(),
                new ChildrensDay(),
                new ParentsDay(),
                new TeachersDay(),
                new ExamDay(),
                new PeperoDay(),
                new Christmas()
        };
    }
}

/* ===== 날짜가 사람마다 다른 기념일 (FlexibleAnniversary 상속) ===== */

/** 생일 */
class Birthday extends FlexibleAnniversary {
    public Birthday() {
        super("생일", "사람마다 달라요");
    }

    @Override
    public String getMessage() {
        return "1년에 한 번뿐인 날! 받는 사람의 취향을 떠올리며 골라 보세요.";
    }
}

/** 커플 100일 */
class Couple100Days extends FlexibleAnniversary {
    public Couple100Days() {
        super("커플 100일", "사귄 날 +100일");
    }

    @Override
    public String getMessage() {
        return "둘만의 추억이 담긴 선물로 100일을 기념해 보세요!";
    }
}

/** 커플 1주년 */
class CoupleAnniversary extends FlexibleAnniversary {
    public CoupleAnniversary() {
        super("커플 1주년", "사귄 날 +1년");
    }

    @Override
    public String getMessage() {
        return "함께한 1년을 돌아보며 오래 간직할 선물을 골라 보세요.";
    }
}

/** 졸업·입학 */
class Graduation extends FlexibleAnniversary {
    public Graduation() {
        super("졸업·입학", "학교마다 달라요");
    }

    @Override
    public String getMessage() {
        return "새로운 출발을 응원하는 마음을 담아 보세요!";
    }
}

/** 승진·이직 */
class Promotion extends FlexibleAnniversary {
    public Promotion() {
        super("승진·이직", "축하할 일 생기면");
    }

    @Override
    public String getMessage() {
        return "새 자리에서도 잘 해낼 거라는 응원을 전해 보세요.";
    }
}

/** 설날 (음력이라 해마다 날짜가 바뀜) */
class LunarNewYear extends FlexibleAnniversary {
    public LunarNewYear() {
        super("설날", "음력 1월 1일");
    }

    @Override
    public String getMessage() {
        return "새해 복 많이 받으시라는 마음을 담아 보세요!";
    }
}

/** 수능 응원 (해마다 날짜가 바뀜) */
class ExamDay extends FlexibleAnniversary {
    public ExamDay() {
        super("수능 응원", "11월 셋째 주 목");
    }

    @Override
    public String getMessage() {
        return "그동안의 노력이 빛나길! 든든한 응원을 보내 보세요.";
    }
}

/* ===== 날짜가 정해진 기념일 (Anniversary 상속) ===== */

/** 발렌타인데이 */
class ValentinesDay extends Anniversary {
    public ValentinesDay() {
        super("발렌타인데이", 2, 14);
    }

    @Override
    public String getMessage() {
        return "달콤한 초콜릿과 함께 좋아하는 마음을 전해 보세요!";
    }
}

/** 화이트데이 */
class WhiteDay extends Anniversary {
    public WhiteDay() {
        super("화이트데이", 3, 14);
    }

    @Override
    public String getMessage() {
        return "받은 마음에 사탕처럼 달콤하게 답해 보세요!";
    }
}

/** 어린이날 */
class ChildrensDay extends Anniversary {
    public ChildrensDay() {
        super("어린이날", 5, 5);
    }

    @Override
    public String getMessage() {
        return "아이들이 신나게 웃을 수 있는 선물을 골라 보세요!";
    }
}

/** 어버이날 */
class ParentsDay extends Anniversary {
    public ParentsDay() {
        super("어버이날", 5, 8);
    }

    @Override
    public String getMessage() {
        return "비싼 선물보다 \"감사합니다\" 한마디가 가장 큰 선물이에요.";
    }
}

/** 스승의날 */
class TeachersDay extends Anniversary {
    public TeachersDay() {
        super("스승의날", 5, 15);
    }

    @Override
    public String getMessage() {
        return "한 해 동안 가르쳐 주신 고마움을 정성으로 전해 보세요.";
    }
}

/** 빼빼로데이 */
class PeperoDay extends Anniversary {
    public PeperoDay() {
        super("빼빼로데이", 11, 11);
    }

    @Override
    public String getMessage() {
        return "부담 없는 간식 선물로 고마운 마음을 가볍게 전해 보세요!";
    }
}

/** 크리스마스 */
class Christmas extends Anniversary {
    public Christmas() {
        super("크리스마스", 12, 25);
    }

    @Override
    public String getMessage() {
        return "따뜻하고 포근한 선물로 연말 분위기를 함께 나눠 보세요!";
    }
}
