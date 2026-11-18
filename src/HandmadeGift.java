import java.awt.Color;

/**
 * 돈이 들지 않는 "마음 선물" (예: 손편지)
 *
 * [상속] Gift를 상속(extends)해서 이름 / 설명 / 기념일 / 받는 사람 기능을 그대로 물려받습니다.
 * [오버라이딩] 가격 글자 / 태그 / 색 / 준비물 안내 / 준비 팁을 바꿨습니다. (초록색 카드)
 */
public class HandmadeGift extends Gift {

    public HandmadeGift(String emoji, String name, String description, String where,
                        String[] occasions, String[] targets) {
        super(emoji, name, 0, description, where, occasions, targets);   // 부모 생성자 호출 (가격은 0원)
    }

    @Override
    public String getPriceText() {
        return "0원 (정성 100%)";
    }

    @Override
    public String getBuyGuide() {
        return "사지 않고 직접 만들어요. 아래 준비물만 있으면 돼요.";
    }

    @Override
    public String getWhereTitle() {
        return "무엇이 필요할까요?";
    }

    @Override
    public String getPrepTip() {
        return "만드는 데 시간이 걸리니 며칠 전부터 넉넉하게 시작하세요.";
    }

    @Override
    public String[] getChecklist() {
        return new String[]{"준비물 챙기기", "정성껏 만들기", "예쁘게 포장하기", "직접 건네며 축하하기"};
    }

    @Override
    public String getTag() {
        return "마음 선물";
    }

    @Override
    public Color getColor() {
        return Main.GREEN;
    }

    @Override
    public Color getLightColor() {
        return Main.GREEN_LIGHT;
    }
}