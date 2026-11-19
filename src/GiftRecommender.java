import java.util.ArrayList;
import java.util.Collections;

/**
 * 선물 목록을 가지고 있고 조건에 맞는 선물을 골라 주는 클래스
 */
public class GiftRecommender {

    public static final int COUNT = 5;   // 한 번에 추천하는 선물 개수

    private ArrayList<Gift> gifts = new ArrayList<>();

    public GiftRecommender() {
        // ===== 마음 선물 (HandmadeGift) - 0원, 초록 카드 =====
        gifts.add(new HandmadeGift("💌", "정성 가득 손편지", "평소 하지 못했던 고마운 말을 꾹꾹 눌러 써 보세요.",
                new String[]{"생일", "커플 100일", "졸업·입학", "어버이날", "스승의날", "빼빼로데이", "크리스마스"},
                new String[]{"친구", "연인", "썸 상대", "부모님", "조부모님", "형제자매", "조카", "선후배", "직장 동료", "선생님"}));
        gifts.add(new HandmadeGift("📜", "반 친구들과 롤링페이퍼", "여러 사람의 마음을 한 장에 모아 전해요.",
                new String[]{"스승의날", "졸업·입학", "생일", "크리스마스"},
                new String[]{"선생님", "친구", "선후배"}));
        gifts.add(new HandmadeGift("🎬", "감사 영상 편지", "사진과 영상을 모아 짧은 감사 영상을 만들어요.",
                new String[]{"어버이날", "스승의날", "졸업·입학", "생일"},
                new String[]{"부모님", "조부모님", "선생님"}));
        gifts.add(new HandmadeGift("🧹", "집안일 쿠폰북", "설거지 / 청소 / 안마 쿠폰을 직접 만들어 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new HandmadeGift("📸", "추억 사진 앨범", "함께 찍은 사진을 모아 직접 꾸민 앨범이에요.",
                new String[]{"커플 100일", "생일", "졸업·입학"},
                new String[]{"연인", "친구", "선후배"}));
        gifts.add(new HandmadeGift("🍱", "직접 만든 도시락", "좋아하는 반찬으로 정성껏 싼 한 끼",
                new String[]{"생일", "커플 100일", "어버이날"},
                new String[]{"연인", "부모님", "조부모님", "형제자매"}));
        gifts.add(new HandmadeGift("📞", "매주 안부 전화 약속", "\"자주 연락드릴게요\" 약속 카드를 드려요.",
                new String[]{"어버이날", "생일", "크리스마스"},
                new String[]{"조부모님", "부모님"}));
        gifts.add(new HandmadeGift("🎉", "깜짝 생일 파티", "풍선과 케이크로 몰래 파티를 준비해요.",
                new String[]{"생일"},
                new String[]{"친구", "형제자매", "연인", "부모님", "조카", "직장 동료"}));
        gifts.add(new HandmadeGift("🌺", "종이 카네이션 만들기", "색종이로 접은 카네이션에 감사 메시지를 달아요.",
                new String[]{"어버이날", "스승의날"},
                new String[]{"부모님", "조부모님", "선생님"}));
        gifts.add(new HandmadeGift("🖍️", "칠판 깜짝 축하 문구", "교실 칠판을 감사와 축하 문구로 꾸며요.",
                new String[]{"스승의날", "생일", "졸업·입학"},
                new String[]{"선생님", "선후배"}));
        gifts.add(new HandmadeGift("🎤", "반 단체 감사 노래", "반 친구들이 다 같이 감사 노래를 불러 드려요.",
                new String[]{"스승의날", "졸업·입학"},
                new String[]{"선생님"}));
        gifts.add(new HandmadeGift("🎨", "직접 그린 캐리커처", "받는 사람의 얼굴을 귀엽게 그려 선물해요.",
                new String[]{"생일", "커플 100일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "조카", "선생님"}));
        gifts.add(new HandmadeGift("🎧", "추억 노래 플레이리스트", "함께 들었던 노래를 모아 사연과 함께 보내요.",
                new String[]{"커플 100일", "생일", "크리스마스"},
                new String[]{"연인", "썸 상대", "친구"}));
        gifts.add(new HandmadeGift("🎈", "함께 놀아 주기 쿠폰", "\"하루 종일 놀아 줄게!\" 쿠폰을 만들어 줘요.",
                new String[]{"생일"},
                new String[]{"조카", "형제자매"}));
        gifts.add(new HandmadeGift("📹", "합격 기원 응원 영상", "친구들의 응원 메시지를 모아 영상으로 보내요.",
                new String[]{"졸업·입학"},
                new String[]{"선후배", "친구", "형제자매"}));

        // ===== 모바일 선물 (GifticonGift) - 파란 카드 =====
        gifts.add(new GifticonGift("🏪", "편의점 기프티콘", 5000, "메신저로 바로 보낼 수 있는 부담 없는 선물",
                new String[]{"생일", "빼빼로데이"},
                new String[]{"친구", "형제자매", "선후배", "조카"}));
        gifts.add(new GifticonGift("🧋", "버블티 기프티콘", 6500, "달달한 버블티 한 잔으로 기분 전환!",
                new String[]{"생일", "빼빼로데이"},
                new String[]{"친구", "형제자매", "연인", "썸 상대", "선후배", "조카"}));
        gifts.add(new GifticonGift("🍦", "아이스크림 기프티콘", 7000, "골라 먹는 재미가 있는 아이스크림",
                new String[]{"생일", "빼빼로데이"},
                new String[]{"친구", "형제자매", "연인", "썸 상대", "조카"}));
        gifts.add(new GifticonGift("☕", "카페 음료 쿠폰", 9500, "좋아하는 카페에서 쉬어 가라는 의미를 담아서",
                new String[]{"생일", "빼빼로데이"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "선후배", "직장 동료"}));
        gifts.add(new GifticonGift("📚", "문화상품권", 10000, "책이나 영화 등 원하는 걸 직접 고를 수 있어요.",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "형제자매", "조카", "선후배"}));
        gifts.add(new GifticonGift("🍬", "마카롱 세트 기프티콘", 15000, "알록달록 예쁜 마카롱으로 달콤하게",
                new String[]{"생일", "커플 100일"},
                new String[]{"연인", "썸 상대", "친구", "직장 동료"}));
        gifts.add(new GifticonGift("🍗", "치킨 기프티콘", 20000, "기분 좋은 날엔 역시 치킨!",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "연인", "형제자매", "선후배"}));
        gifts.add(new GifticonGift("🎂", "케이크 기프티콘", 25000, "축하하는 날에는 역시 케이크!",
                new String[]{"생일", "커플 100일"},
                new String[]{"친구", "연인", "부모님", "조부모님", "형제자매", "조카", "직장 동료"}));
        gifts.add(new GifticonGift("🎬", "영화 관람권 2매", 28000, "함께 영화 보며 데이트하라는 의미를 담아서",
                new String[]{"커플 100일", "크리스마스", "생일"},
                new String[]{"연인", "썸 상대", "친구"}));
        gifts.add(new GifticonGift("🛵", "배달 음식 상품권", 30000, "오늘 저녁은 요리하지 말고 편하게 드세요.",
                new String[]{"생일", "어버이날", "크리스마스"},
                new String[]{"부모님", "조부모님", "친구", "형제자매"}));
        gifts.add(new GifticonGift("🛍️", "백화점 상품권", 50000, "필요한 걸 직접 고르실 수 있게",
                new String[]{"어버이날", "생일", "졸업·입학"},
                new String[]{"부모님", "조부모님"}));

        // ===== 체험 선물 (ExperienceGift) - 보라 카드 =====
        gifts.add(new ExperienceGift("🎳", "볼링 2게임 이용권", 20000, "함께 신나게 볼링 한 판!",
                new String[]{"생일", "빼빼로데이"},
                new String[]{"친구", "형제자매", "선후배"}));
        gifts.add(new ExperienceGift("🧸", "키즈카페 이용권", 20000, "마음껏 뛰어놀 수 있는 하루",
                new String[]{"생일"},
                new String[]{"조카"}));
        gifts.add(new ExperienceGift("♨️", "찜질방 이용권 2인", 30000, "뜨끈한 찜질방에서 피로를 풀어요.",
                new String[]{"어버이날", "생일", "크리스마스"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new ExperienceGift("🔐", "방탈출 카페 2인", 44000, "둘이 머리를 맞대고 탈출하는 특별한 경험",
                new String[]{"생일", "크리스마스", "커플 100일"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "선후배"}));
        gifts.add(new ExperienceGift("🧁", "원데이 베이킹 클래스", 50000, "함께 케이크를 만들며 추억을 쌓아요.",
                new String[]{"크리스마스", "생일"},
                new String[]{"연인", "썸 상대", "친구", "형제자매"}));
        gifts.add(new ExperienceGift("🧪", "향수 만들기 공방 2인", 60000, "서로에게 어울리는 향을 직접 만들어요.",
                new String[]{"커플 100일", "생일"},
                new String[]{"연인", "친구"}));
        gifts.add(new ExperienceGift("💍", "반지 만들기 공방 2인", 70000, "직접 만든 커플 반지라 더 특별해요.",
                new String[]{"커플 100일"},
                new String[]{"연인"}));
        gifts.add(new ExperienceGift("📷", "가족 사진 촬영권", 90000, "온 가족이 함께 남기는 소중한 사진",
                new String[]{"어버이날"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new ExperienceGift("🎡", "놀이공원 자유이용권 2매", 100000, "하루 종일 신나게 노는 날!",
                new String[]{"생일", "크리스마스", "커플 100일"},
                new String[]{"연인", "친구", "형제자매", "조카"}));
        gifts.add(new ExperienceGift("🎶", "효도 콘서트 관람권 2매", 120000, "좋아하시는 가수 공연을 보여 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님"}));

        // ===== 일반 선물 (Gift) - 빨간 카드 =====
        gifts.add(new Gift("🍫", "빼빼로 여러 맛 세트", 6000, "여러 맛을 골라 담은 기본 선물",
                new String[]{"빼빼로데이"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "조카", "선후배", "직장 동료"}));
        gifts.add(new Gift("🍫", "빼빼로 만들기 키트", 9000, "직접 만들어 주면 정성이 두 배!",
                new String[]{"빼빼로데이"},
                new String[]{"친구", "연인", "썸 상대", "형제자매"}));
        gifts.add(new Gift("🧦", "양말 세트", 9000, "귀여운 무늬 양말로 가볍게 마음 전하기",
                new String[]{"크리스마스", "생일"},
                new String[]{"친구", "형제자매", "부모님", "조부모님", "조카"}));
        gifts.add(new Gift("🍪", "수제 쿠키 상자", 10000, "바삭하고 달콤한 쿠키 한 상자",
                new String[]{"빼빼로데이", "크리스마스"},
                new String[]{"친구", "형제자매", "연인", "썸 상대", "부모님", "조부모님", "직장 동료"}));
        gifts.add(new Gift("✏️", "캐릭터 문구 세트", 12000, "공부할 때마다 생각나는 귀여운 문구",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "형제자매", "조카", "선후배"}));
        gifts.add(new Gift("💝", "빼빼로 하트 박스", 15000, "하트 상자에 담아 마음까지 전해요.",
                new String[]{"빼빼로데이"},
                new String[]{"연인", "썸 상대", "친구"}));
        gifts.add(new Gift("🔑", "커플 키링", 15000, "가방에 달고 다니는 둘만의 표시",
                new String[]{"커플 100일"},
                new String[]{"연인"}));
        gifts.add(new Gift("🧴", "핸드크림 세트", 15000, "향 좋은 핸드크림은 누구에게나 쓸모 있어요.",
                new String[]{"생일", "크리스마스", "어버이날"},
                new String[]{"친구", "연인", "썸 상대", "부모님", "조부모님", "형제자매", "직장 동료", "선후배"}));
        gifts.add(new Gift("📔", "탁상 다이어리", 15000, "새해 계획을 차곡차곡 적어 보세요.",
                new String[]{"크리스마스", "졸업·입학"},
                new String[]{"직장 동료", "선후배", "친구", "형제자매"}));
        gifts.add(new Gift("🎄", "미니 크리스마스 트리", 15000, "책상 위에 올려 두는 작은 트리",
                new String[]{"크리스마스"},
                new String[]{"친구", "형제자매", "연인", "썸 상대", "부모님", "조부모님", "조카", "직장 동료"}));
        gifts.add(new Gift("💡", "무드등", 18000, "방 분위기를 바꿔 주는 은은한 조명",
                new String[]{"크리스마스", "생일", "커플 100일"},
                new String[]{"친구", "연인", "썸 상대", "형제자매"}));
        gifts.add(new Gift("🧣", "무릎 담요", 18000, "쌀쌀한 날 포근하게 덮는 담요",
                new String[]{"크리스마스", "생일", "어버이날"},
                new String[]{"조부모님", "부모님", "친구", "형제자매", "선후배", "직장 동료"}));
        gifts.add(new Gift("🪴", "공기정화 화분", 20000, "초록 식물로 공기도 기분도 상쾌하게",
                new String[]{"어버이날", "생일", "크리스마스"},
                new String[]{"부모님", "조부모님", "친구", "직장 동료"}));
        gifts.add(new Gift("🧸", "캐릭터 인형", 20000, "꼭 껴안고 싶은 폭신한 인형",
                new String[]{"생일", "크리스마스", "커플 100일"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "조카"}));
        gifts.add(new Gift("⛄", "스노우볼 오르골", 22000, "흔들면 눈이 내리는 감성 선물",
                new String[]{"크리스마스"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "조카"}));
        gifts.add(new Gift("🥤", "텀블러", 22000, "매일 들고 다니며 쓰는 실용 선물",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "연인", "부모님", "형제자매", "직장 동료", "선후배"}));
        gifts.add(new Gift("📖", "동화책 세트", 25000, "잠들기 전에 읽어 주기 좋은 그림 동화",
                new String[]{"생일", "크리스마스"},
                new String[]{"조카"}));
        gifts.add(new Gift("👓", "돋보기 안경", 25000, "작은 글씨도 잘 보이게 도와 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"조부모님"}));
        gifts.add(new Gift("🕯️", "캔들 디퓨저 세트", 25000, "좋은 향으로 방 안을 가득 채워요.",
                new String[]{"크리스마스", "생일"},
                new String[]{"친구", "연인", "썸 상대", "형제자매", "부모님", "직장 동료"}));
        gifts.add(new Gift("🥜", "견과류 선물 세트", 25000, "매일 한 줌, 건강을 챙기는 간식",
                new String[]{"어버이날", "생일", "크리스마스", "빼빼로데이"},
                new String[]{"부모님", "조부모님", "직장 동료"}));
        gifts.add(new Gift("💧", "데스크 가습기", 25000, "건조한 사무실 책상 위 필수품",
                new String[]{"생일", "크리스마스"},
                new String[]{"직장 동료", "선후배"}));
        gifts.add(new Gift("🧤", "목도리·장갑 세트", 28000, "추운 겨울을 따뜻하게 지켜 주는 선물",
                new String[]{"크리스마스", "생일"},
                new String[]{"친구", "연인", "썸 상대", "부모님", "조부모님", "형제자매", "조카"}));
        gifts.add(new Gift("🎲", "보드게임", 30000, "모여서 함께 즐길 수 있는 선물",
                new String[]{"크리스마스", "생일"},
                new String[]{"친구", "형제자매", "조카"}));
        gifts.add(new Gift("🌷", "카네이션 꽃바구니", 30000, "어버이날 대표 선물",
                new String[]{"어버이날"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new Gift("💼", "명함 지갑", 30000, "새 명함을 멋지게 꺼낼 수 있게",
                new String[]{"졸업·입학"},
                new String[]{"직장 동료", "선후배", "부모님"}));
        gifts.add(new Gift("💐", "꽃다발", 35000, "말없이 마음을 전하는 가장 확실한 방법",
                new String[]{"커플 100일", "생일", "졸업·입학"},
                new String[]{"연인", "친구", "선후배", "형제자매", "조카"}));
        gifts.add(new Gift("👕", "포근한 내복 세트", 35000, "어르신 겨울 필수품, 따뜻하게 입으세요.",
                new String[]{"크리스마스", "어버이날", "생일"},
                new String[]{"조부모님"}));
        gifts.add(new Gift("🌙", "잠옷 세트", 35000, "편안한 밤을 위한 보들보들 잠옷",
                new String[]{"크리스마스", "생일"},
                new String[]{"연인", "형제자매", "부모님", "조카"}));
        gifts.add(new Gift("🧱", "블록 장난감 세트", 35000, "만들고 부수며 노는 창의력 장난감",
                new String[]{"생일", "크리스마스"},
                new String[]{"조카"}));
        gifts.add(new Gift("🖋️", "고급 볼펜", 40000, "중요한 서명을 할 때 생각나는 펜",
                new String[]{"졸업·입학"},
                new String[]{"직장 동료", "선후배", "부모님", "형제자매"}));
        gifts.add(new Gift("🎒", "새 학기 책가방", 40000, "새 출발을 함께할 튼튼한 가방",
                new String[]{"졸업·입학"},
                new String[]{"조카", "형제자매"}));
        gifts.add(new Gift("🔊", "블루투스 스피커", 45000, "좋아하는 노래를 더 크게 들을 수 있어요.",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "연인", "형제자매", "선후배"}));
        gifts.add(new Gift("🌿", "홍삼 세트", 45000, "건강을 챙겨 드리는 든든한 선물",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님", "직장 동료"}));
        gifts.add(new Gift("🌸", "향수", 48000, "은은한 향으로 오래 기억되는 선물",
                new String[]{"커플 100일", "생일", "크리스마스"},
                new String[]{"연인"}));
        gifts.add(new Gift("💍", "커플 반지", 49000, "기념일을 오래 기억할 둘만의 반지",
                new String[]{"커플 100일"},
                new String[]{"연인"}));
        gifts.add(new Gift("🍎", "과일 선물 세트", 50000, "제철 과일을 정성껏 골라 담았어요.",
                new String[]{"크리스마스", "어버이날"},
                new String[]{"부모님", "조부모님", "직장 동료"}));
        gifts.add(new Gift("🌻", "축하 꽃바구니", 50000, "좋은 소식을 화사하게 축하해요.",
                new String[]{"졸업·입학", "생일"},
                new String[]{"직장 동료", "부모님", "친구", "선후배", "형제자매"}));
        gifts.add(new Gift("💵", "용돈 꽃다발", 55000, "꽃 사이에 용돈을 넣어 웃음까지 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new Gift("💆", "목·어깨 안마기", 59000, "고생하신 피로를 시원하게 풀어 드려요.",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new Gift("👛", "가죽 지갑", 60000, "오래 쓰는 물건이라 볼 때마다 생각나요.",
                new String[]{"생일", "크리스마스", "어버이날", "커플 100일", "졸업·입학"},
                new String[]{"연인", "부모님", "형제자매"}));
        gifts.add(new Gift("🎧", "무선 이어폰", 70000, "선 없이 편하게 음악을 들어요.",
                new String[]{"생일", "크리스마스", "졸업·입학"},
                new String[]{"친구", "연인", "형제자매"}));
        gifts.add(new Gift("👟", "발 편한 운동화", 70000, "매일 산책길이 가벼워지는 운동화",
                new String[]{"어버이날", "생일"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new Gift("🍽️", "가족 외식", 80000, "온 가족이 함께하는 맛있는 한 끼",
                new String[]{"어버이날"},
                new String[]{"부모님", "조부모님"}));
        gifts.add(new Gift("⌚", "손목시계", 90000, "함께하는 시간을 기억하는 시계",
                new String[]{"커플 100일", "생일", "크리스마스", "졸업·입학"},
                new String[]{"연인", "부모님"}));
        gifts.add(new Gift("🥩", "한우 선물 세트", 120000, "명절에 드리는 든든한 고기 선물",
                new String[]{"어버이날"},
                new String[]{"부모님", "조부모님"}));
    }

    /**
     * 조건에 맞는 선물을 최대 COUNT(5)개 골라 줍니다.
     * 1순위: 기념일 + 받는 사람 + 예산이 모두 맞는 선물
     * 2순위: 1순위가 COUNT개보다 적으면 받는 사람 + 예산만 맞는 선물로 채움
     * 같은 순위 안에서는 섞어서, 누를 때마다 다른 선물이 나옵니다.
     */
    public ArrayList<Gift> recommend(Anniversary anniversary, String target, int maxPrice) {
        ArrayList<Gift> best = new ArrayList<>();
        ArrayList<Gift> others = new ArrayList<>();

        for (Gift g : gifts) {
            if (g.getPrice() > maxPrice || !g.fitsTarget(target)) {
                continue;   // 예산 초과이거나 받는 사람이 안 맞으면 건너뜀
            }
            if (g.fitsOccasion(anniversary.getName())) {
                best.add(g);
            } else {
                others.add(g);
            }
        }

        // 순위 안에서 섞기 (1순위가 항상 2순위보다 앞)
        Collections.shuffle(best);
        Collections.shuffle(others);

        ArrayList<Gift> result = new ArrayList<>(best);
        result.addAll(others);

        // 앞에서부터 COUNT개만 남기기
        while (result.size() > COUNT) {
            result.remove(result.size() - 1);
        }
        return result;
    }
}
