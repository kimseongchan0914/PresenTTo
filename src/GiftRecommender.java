import java.util.ArrayList;
import java.util.Collections;

/**
 * 선물 목록을 가지고 있고 조건에 맞는 선물을 골라 주는 클래스
 */
public class GiftRecommender {

    private ArrayList<Gift> gifts = new ArrayList<>();

    public GiftRecommender() {
        // ===== 마음 선물 (HandmadeGift) =====
        gifts.add(new HandmadeGift("정성 가득 손편지", "평소 하지 못했던 고마운 말을 꾹꾹 눌러 써 보세요.",
                new String[]{"생일", "크리스마스", "어버이날", "빼빼로데이"},
                new String[]{"친구", "부모님", "선생님", "형제자매"}));
        gifts.add(new HandmadeGift("반 친구들과 롤링페이퍼", "여러 사람의 마음을 한 장에 모아 전해요.",
                new String[]{"생일", "크리스마스"},
                new String[]{"선생님", "친구"}));
        gifts.add(new HandmadeGift("감사 영상 편지", "사진과 영상을 모아 짧은 감사 영상을 만들어요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "선생님"}));
        gifts.add(new HandmadeGift("집안일 쿠폰북", "설거지 / 청소 / 안마 쿠폰을 직접 만들어 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님"}));

        // ===== 일반 선물 (Gift) =====
        gifts.add(new Gift("빼빼로 여러 맛 세트", 6000, "여러 맛을 골라 담은 기본 선물",
                new String[]{"빼빼로데이"}, new String[]{"친구", "형제자매"}));
        gifts.add(new Gift("빼빼로 만들기 키트", 9000, "직접 만들어 주면 정성이 두 배!",
                new String[]{"빼빼로데이"}, new String[]{"친구", "형제자매"}));
        gifts.add(new Gift("편의점 기프티콘", 5000, "메신저로 바로 보낼 수 있는 부담 없는 선물",
                new String[]{"생일", "빼빼로데이"}, new String[]{"친구", "형제자매"}));
        gifts.add(new Gift("카페 음료 쿠폰", 9500, "좋아하는 카페에서 쉬어 가라는 의미를 담아서",
                new String[]{"생일", "빼빼로데이"}, new String[]{"친구", "형제자매"}));
    }
}