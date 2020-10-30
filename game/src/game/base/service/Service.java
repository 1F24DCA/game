package game.base.service;

import java.lang.reflect.*;
import java.util.*;

import game.base.common.Generator;
import game.base.service.event.*;

// 모든 서비스는 Service 클래스를 상속받음
// 게임 서비스들에는 특정 이벤트에 대한 동작이 필요하므로
// 추상화된 이벤트(Event 인터페이스)와 이벤트가 발생했을 때 처리할 액션(Action 추상클래스)를 이용해 이벤트 처리를 보다 간편하게 하기 위한 메서드들을 제공함
public abstract class Service<E extends Event> {
	// 액션들을 List에 담아뒀다가 raiseEvent() 메서드 호출시 필요한 액션들을 perform하기 위해 모아둠
	List<Action<? extends E>> actionList = Collections.synchronizedList(new ArrayList<Action<? extends E>>());
	
	// addAction(): Generic Type으로 기재된 Event가 raiseEvent()로 실행되었을 때 Action을 실행하도록 서비스에 등록함
	public final boolean addAction(Action<? extends E> actionToAdd) {
		return actionList.add(actionToAdd);
	}

	// removeAction(): 서비스에 등록된 해당 Action을 서비스에서 등록 해지함 (제거함)
	public final boolean removeAction(Action<? extends E> actionToRemove) {
		return actionList.remove(actionToRemove);
	}
	
	// raiseEvent(): Event 정보가 담긴 객체를 넣어, 이벤트를 raise함 (이벤트와 연관된 Action 모두 실행)
	public final boolean raiseEvent(E eventToRaise) {
		for (Action<? extends E> foundAction : actionList) {
			// 해당 액션이 eventToRaise 이벤트 객체와 연관이 있다면 액션을 실행함 (부모, 조상 이벤트인 경우 포함)
			if (eventToRaise.getClass().isAssignableFrom(this.getActionEventClass(foundAction))) {
				// 모든 액션의 perform() 메서드를 실행하고, perform 메서드가 모두 성공했다면 true를 반환, 하나라도 실패했다면 false을 반환
				if (!foundAction.perform(eventToRaise)) {
					return false;
				}
			}
		}
		return true;
	}
	
	// getActionEventClass() : Action의 Generic Type, 즉 Action과 연결된 이벤트의 클래스를 찾아냄
	// Action과 연결된 이벤트는 액션마다 다르므로 이벤트를 호출하고자 할 때 이 Generic Type을 가져와야 함
	private Class<? extends E> getActionEventClass(Action<? extends E> action) {
		// Action을 상속받은 클래스에선 Generic Type을 찾기 힘드므로(ex. 익명클래스, Generic Type 미기재 등...) 부모 클래스의 타입을 받아옴
		// 부모 클래스엔 Generic Type이 기재되어있지만, 기재된 Generic Type을 가져오려면 ParameterizedType 클래스로 형변환해야함!
		ParameterizedType superClass = (ParameterizedType)action.getClass().getGenericSuperclass();
		// 현재 부모 클래스의 Generic Type을 가져옴
		// 배열로 받아오는 이유는, Map<K, V>처럼 Generic Type이 두개 이상인 경우도 있기 때문
		// 현재는 action 파라미터에서 받아오므로 무조건 1개의 Generic Type이 있다고 확신가능하므로, 배열의 0번째 요소를 취함
		Type eventType = superClass.getActualTypeArguments()[0];
		
		// Class의 부모가 Type이므로, 이 메서드를 사용하는 곳에서 편하게 하기 위해 Class 타입으로 형변환해서 return함
		@SuppressWarnings("unchecked")
		Class<? extends E> eventClass = (Class<? extends E>)eventType;
		
		return eventClass;
	}
	
	// Service의 정보를 출력함
	@Override
	public String toString() {
		return Generator.generateToString(this);
	}
}
