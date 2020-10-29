package game.base.service.event;

import game.base.value.Game;

// 게임이벤트 객체, 상속받을 시 "public class ConcreteGameEvent extends GameEvent<ConcreteGame> {...}"(으)로 상속받음
// 게임이벤트엔 게임의 정보는 무조건 들어가야 작업이 가능하므로(어떤 게임을 어디서 하는지 모르는데 이벤트를 처리할 수는 없음)
// Game을 무조건 갖도록 강제하기 위해 클래스를 만듦
// Generic을 사용하여 해당 게임 이벤트와 연관된 게임 클래스를 형변환 없이 사용할 수 있도록 함
public abstract class GameEvent<G extends Game> implements Event {
	private G game;
	
	public GameEvent(G game) {
		this.game = game;
	}

	public final G getGame() {
		return this.game;
	}
}
