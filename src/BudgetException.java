/**
 * 예산을 잘못 입력했을 때 던지는 예외 (직접 만든 예외 클래스)
 *
 * [상속] 자바의 Exception을 상속해서 만들었습니다.
 *       잘못 입력한 이유(메시지)를 담아서 던지면, 받는 쪽에서 getMessage()로 꺼내 화면에 보여줍니다.
 */
public class BudgetException extends Exception {

    public BudgetException(String message) {
        super(message);   // 부모(Exception)에 메시지 저장
    }
}
