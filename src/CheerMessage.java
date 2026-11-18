import java.util.ArrayList;
import java.util.Random;

/**
 * 선물을 최종 선택했을 때 보여주는 응원 문구를 만들어 주는 클래스
 *
 * 누구에게나 어울리는 문구 + 받는 사람에게 맞는 문구 중에서 하나를 무작위로 골라 줍니다.
 * 문구 안의 {받는사람}, {선물}은 실제 이름으로 바뀝니다.
 */
public class CheerMessage {

    private static final Random random = new Random();

    // 누구에게나 어울리는 문구
    private static final String[] COMMON = {
            "고민한 시간만큼 더 따뜻한 선물이 될 거예요. 파이팅!",
            "{받는사람}의 웃는 얼굴이 벌써 보이는 것 같아요!",
            "선물보다 오래 남는 건 건네는 순간의 한마디예요. 짧게라도 꼭 적어 보세요!",
            "{선물}, 정말 좋은 선택이에요. 마음이 꼭 닿을 거예요!"
    };

    /** 받는 사람에 맞는 문구 */
    private static String[] forTarget(String target) {
        switch (target) {
            case "연인":
            case "썸 상대":
                return new String[]{
                        "두근두근! 설레는 마음까지 같이 포장해서 전해 보세요.",
                        "선물과 함께 \"늘 고마워\" 한마디면 완벽해요!"
                };
            case "부모님":
            case "조부모님":
                return new String[]{
                        "\"고맙습니다, 사랑해요\" 한마디를 꼭 같이 전해 보세요. 그게 제일 큰 선물이에요.",
                        "{받는사람}께서 오래오래 자랑하실 거예요!"
                };
            case "선생님":
                return new String[]{
                        "정성 가득한 마음은 어떤 선물보다 오래 기억에 남아요.",
                        "감사한 마음이 고스란히 전해질 거예요!"
                };
            case "직장 동료":
            case "선후배":
                return new String[]{
                        "작은 선물 하나가 하루를 환하게 만들어요.",
                        "센스 있는 선택이에요. 분명 기억해 줄 거예요!"
                };
            case "조카":
            case "형제자매":
                return new String[]{
                        "받자마자 신나서 뛰어다닐 모습이 그려져요!",
                        "티격태격해도 이럴 땐 최고의 가족이죠!"
                };
            default:   // 친구
                return new String[]{
                        "역시 {받는사람}를 제일 잘 아는 사람다운 선택이에요!",
                        "우정 지수가 한 칸 더 올라가겠는데요?"
                };
        }
    }

    /** 응원 문구 하나 만들기 */
    public static String make(String target, Gift gift) {
        ArrayList<String> all = new ArrayList<>();
        for (String s : COMMON) {
            all.add(s);
        }
        for (String s : forTarget(target)) {
            all.add(s);
        }
        String picked = all.get(random.nextInt(all.size()));
        return picked.replace("{받는사람}", target).replace("{선물}", gift.getName());
    }
}
