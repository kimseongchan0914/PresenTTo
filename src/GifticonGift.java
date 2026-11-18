import java.awt.Color;

/**
 * 메신저로 바로 보낼 수 있는 "모바일 선물" (예: 카페 쿠폰, 케이크 기프티콘)
 *
 * [상속] Gift를 상속(extends)해서 이름 / 가격 / 설명 / 기념일 / 받는 사람 기능을 그대로 물려받습니다.
 * [오버라이딩] 가격 글자 / 태그 / 색 / 구매 안내 / 준비 팁을 바꿨습니다. (파란색 카드)
 */
public class GifticonGift extends Gift {

    public GifticonGift(String emoji, String name, int price, String description, String where,
                        String[] occasions, String[] targets) {
        super(emoji, name, price, description, where, occasions, targets);   // 부모 생성자 호출
    }

    @Override
    public String getPriceText() {
        return super.getPriceText() + " · 바로 전송";   // 부모의 "9500원" 뒤에 글자를 덧붙임
    }

    @Override
    public String getBuyGuide() {
        return "휴대폰으로 사서 메신저로 바로 보낼 수 있어요.";
    }

    @Override
    public String getPrepTip() {
        return "당일에 보내도 바로 도착해요. 짧은 축하 메시지를 꼭 함께 보내세요!";
    }

    @Override
    public String[] getChecklist() {
        return new String[]{"받는 사람 연락처 확인", "앱에서 선물 고르기", "축하 메시지 쓰기", "보내기 버튼 누르기"};
    }

    @Override
    public String getTag() {
        return "모바일 선물";
    }

    @Override
    public Color getColor() {
        return Main.BLUE;
    }

    @Override
    public Color getLightColor() {
        return Main.BLUE_LIGHT;
    }
}
