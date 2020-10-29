package game.base.service;

import java.lang.reflect.*;
import java.util.*;

import game.base.service.event.*;

// 모든 서비스는 Service 클래스를 상속받음
// 게임 서비스들에는 특정 이벤트에 대한 동작이 필요하므로
// 추상화된 이벤트(Event 인터페이스)와 이벤트가 발생했을 때 처리할 액션(Action 추상클래스)를 이용해 이벤트 처리를 보다 간편하게 하기 위한 메서드들을 제공함
public abstract class Service {
	// 액션들을 List에 담아뒀다가 raiseEvent() 메서드 호출시 필요한 액션들을 perform하기 위해 모아둠
	List<Action<?>> actionList = new ArrayList<Action<?>>();
	
	// addAction(): Generic Type으로 기재된 Event가 raiseEvent()로 실행되었을 때 Action을 실행하도록 서비스에 등록함
	public final boolean addAction(Action<?> actionToAdd) {
		return actionList.add(actionToAdd);
	}

	// removeAction(): 서비스에 등록된 해당 Action을 서비스에서 등록 해지함 (제거함)
	public final boolean removeAction(Action<?> actionToRemove) {
		return actionList.remove(actionToRemove);
	}
	
	// raiseEvent(): Event 정보가 담긴 객체를 넣어, 이벤트를 raise함 (이벤트와 연관된 Action 모두 실행)
	public final boolean raiseEvent(Event eventToRaise) {
		boolean passEvent = true;
		for (Action<?> foundAction : actionList) {
			// 해당 액션이 eventToRaise 이벤트 객체와 연관이 있다면 액션을 실행함 (부모, 조상 이벤트인 경우 포함)
			if (eventToRaise.getClass().isAssignableFrom(this.getLinkedEventClass(foundAction))) {
				// 모든 액션의 perform() 메서드를 실행하고, perform 메서드가 모두 성공했다면 passEvent가 true가 되고, 하나라도 실패했다면 false가 됨
				passEvent = passEvent && foundAction.perform(eventToRaise);
			}
		}
		return passEvent;
	}
	
	// getLinkedEventClass() : Action의 Generic Type이 Action과 연결된 이벤트 타입임
	// 따라서, 이벤트를 호출하고자 할 때 이 Generic Type을 가져와야 함
	@SuppressWarnings("rawtypes") // 사용처인 raiseEvent() 메서드에서 타입 검사용으로만 Class를 써주므로 Class<~~~>같은 타입 기재를 하지 않는다고 명시함
	private Class getLinkedEventClass(Action<?> action) {
		// Action을 상속받은 클래스에선 Generic Type을 찾기 힘드므로(ex. 익명클래스, Generic Type 미기재 등...) 부모 클래스의 타입을 받아옴
		// 부모 클래스엔 Generic Type이 기재되어있지만, 기재된 Generic Type을 가져오려면 ParameterizedType 클래스로 형변환해야함!
		ParameterizedType type = (ParameterizedType)action.getClass().getGenericSuperclass();
		// 현재 부모 클래스의 Generic Type을 가져옴
		// 배열로 받아오는 이유는, Map<K, V>처럼 Generic Type이 두개 이상인 경우도 있기 때문
		// 현재는 action 파라미터에서 받아오므로 무조건 1개의 Generic Type이 있다고 확신가능하므로, 배열의 0번째 요소를 취함
		Type genericType = type.getActualTypeArguments()[0];
		
		// Class의 부모가 Type이므로, 이 메서드를 사용하는 곳에서 편하게 하기 위해 Class 타입으로 형변환해서 return함
		return (Class)genericType;
	}
}
