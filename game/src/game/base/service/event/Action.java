package game.base.service.event;

import game.base.common.Generator;

// Generic Type에 기재된 이벤트(E)가 raise되었을 때 실행할 액션
// Observer 패턴이 적용됨, Service에서 Action들을 관리하고 Event가 raise됬을 때 perform()메서드 일괄호출
public abstract class Action<E extends Event> {
	// perform() 메서드는 Service 객체에서 호출함
	// 호출하는 Service 쪽에선 E타입을 알 수 없으므로, Event 객체로 받아옴
	// 대신 Service 쪽에서 호출할 때 Class.isAssignedFrom() 메서드로 타입 체크를 하므로, 여기에서 형변환 체크를 하지 않음
	// P.S. 여기에서 E타입으로 체크하면 되지않냐고 한다면, E타입은 getClass()를 사용할 수도 없고, instanceof 연산자도 사용할 수 없음
	// 다음 글을 참조바람: https://stackoverflow.com/questions/3437897/how-do-i-get-a-class-instance-of-generic-type-t
	@SuppressWarnings("unchecked")
	public final boolean perform(Event eventToRaise) {
		return onPerform((E) eventToRaise);
	}
	
	// 위의 사유때문에 perform() 메서드와 onPerform() 메서드를 나눔
	// onPerform() 메서드는 Action을 상속받아서 작업하는 익명 클래스나, 등등의 클래스에서 Generic Type에 기재된 이벤트가 raise 시 실행할 액션을 오버라이드함
	protected abstract boolean onPerform(E linkedEvent);
	
	// 액션을 구현한 클래스가 있다면 그 클래스의 정보를 반환함
	@Override
	public String toString() {
		return Generator.generateToString(this);
	}
}
