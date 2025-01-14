package egovframework.com.cmm;

public enum ResponseCode {

	SUCCESS(200, "성공했습니다."),
	AUTH_ERROR(403, "인가된 사용자가 아닙니다."),
	NOT_EQ_PASSWORD(500, "비밀번호가 일치하지 않습니다."),
	NOT_USER(404, "일치하는 사용자가 없습니다."),
	SELECT_ERROR(600, "조회시 내부 오류가 발생했습니다."),
	SELECT_REQUIRE_ERROR(601, "조회 필수 조건이 누락되었습니다."),
	DELETE_ERROR(700, "삭제 중 내부 오류가 발생했습니다."),
	SAVE_ERROR(800, "저장시 내부 오류가 발생했습니다."),
	INPUT_CHECK_ERROR(900, "입력값 무결성 오류 입니다."),
	NOT_JOIN_USER(999, "회원가입이 필요한 사용자입니다.");

	private int code;
	private String message;

	private ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
