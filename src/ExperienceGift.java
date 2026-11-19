import java.awt.Color;

/**
 * 물건 대신 함께하는 시간을 주는 "체험 선물" (예: 방탈출 카페, 공방 체험)
 *
 * [상속] Gift를 상속(extends)해서 이름 / 가격 / 설명 / 기념일 / 받는 사람 기능을 그대로 물려받습니다.
 * [오버라이딩] 태그 / 색을 바꿨습니다. (보라색 카드)
 *             가격 글자는 오버라이딩하지 않아서 부모(Gift)의 getPriceText()가 그대로 쓰입니다.
 */
public class ExperienceGift extends Gift {

    public ExperienceGift(String emoji, String name, int price, String description,
                          String[] occasions, String[] targets) {
        super(emoji, name, price, description, occasions, targets);   // 부모 생성자 호출
    }

    @Override
    public String getTag() {
        return "체험 선물";
    }

    @Override
    public Color getColor() {
        return Main.PURPLE;
    }

    @Override
    public Color getLightColor() {
        return Main.PURPLE_LIGHT;
    }
}
