import java.util.ArrayList;

/**
 * 찜한 선물을 모아 두는 클래스 (장바구니)
 *
 * [캡슐화] 선물 목록(ArrayList)은 private으로 숨기고
 *         add / remove 메소드로만 바꿀 수 있게 했습니다.
 *         getGifts()는 복사본을 돌려줘서 밖에서 목록을 함부로 바꾸지 못합니다.
 */
public class Wishlist {

    private ArrayList<Gift> gifts = new ArrayList<>();

    /** 찜하기 (이미 있으면 다시 넣지 않음) */
    public void add(Gift gift) {
        if (!gifts.contains(gift)) {
            gifts.add(gift);
        }
    }

    /** 찜 취소 */
    public void remove(Gift gift) {
        gifts.remove(gift);
    }

    /** 이미 찜한 선물인지 확인 */
    public boolean contains(Gift gift) {
        return gifts.contains(gift);
    }

    /** 찜한 선물 개수 */
    public int size() {
        return gifts.size();
    }

    /** 찜한 선물 가격 합계 (마음 선물은 0원) */
    public int getTotalPrice() {
        int total = 0;
        for (Gift g : gifts) {
            total += g.getPrice();
        }
        return total;
    }

    /** 찜 목록 (복사본) */
    public ArrayList<Gift> getGifts() {
        return new ArrayList<>(gifts);
    }
}
