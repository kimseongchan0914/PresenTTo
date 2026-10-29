/**
 * 돈이 들지 않는 "마음 선물" (예: 손편지)
 *
 * [상속] Gift를 상속(extends)해서 이름 / 설명 / 기념일 / 받는 사람 기능을 그대로 물려받습니다.
 * [오버라이딩] 가격 표시 방법만 다르게 바꿨습니다.
 */
public class HandmadeGift extends Gift {

    public HandmadeGift(String name, String description, String[] occasions, String[] targets) {
        super(name, 0, description, occasions, targets);   // 부모 생성자 호출 (가격은 0원)
    }

    @Override
    public String getPriceText() {
        return "0원 (정성 100%)";
    }
}