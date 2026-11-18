import java.awt.Color;

/**
 * 물건 대신 함께하는 시간을 주는 "체험 선물" (예: 방탈출 카페, 공방 체험)
 *
 * [상속] Gift를 상속(extends)해서 이름 / 가격 / 설명 / 기념일 / 받는 사람 기능을 그대로 물려받습니다.
 * [오버라이딩] 태그 / 색 / 구매 안내 / 준비 팁을 바꿨습니다. (보라색 카드)
 *             가격 글자는 오버라이딩하지 않아서 부모(Gift)의 getPriceText()가 그대로 쓰입니다.
 */
public class ExperienceGift extends Gift {

    public ExperienceGift(String emoji, String name, int price, String description, String where,
                          String[] occasions, String[] targets) {
        super(emoji, name, price, description, where, occasions, targets);   // 부모 생성자 호출
    }

    @Override
    public String getBuyGuide() {
        return "이용권을 사거나 예약해서 준비해요. 함께 갈 날짜를 먼저 정해 보세요.";
    }

    @Override
    public String getWhereTitle() {
        return "어디서 예약할까요?";
    }

    @Override
    public String getPrepTip() {
        return "인기 있는 날짜와 시간은 금방 차요. 미리 예약하세요.";
    }

    @Override
    public String[] getChecklist() {
        return new String[]{"함께 갈 날짜 정하기", "예약하기", "예약 내용 캡처해 두기", "당일에 같이 즐기기"};
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
