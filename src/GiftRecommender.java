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

    }
}