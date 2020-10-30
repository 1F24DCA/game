package game.base.service.event;

import game.base.common.Generator;

// Event 타입을 체크하는 용도로 사용하는 빈껍데기 클래스
// 하지만 toString() 메서드 호출 시 구현한 클래스의 모든 멤버변수를 표시하기 위해 Generator를 사용해 toString()을 오버라이드함
public abstract class Event {
	@Override
	public String toString() {
		return Generator.generateToString(this);
	}
}
