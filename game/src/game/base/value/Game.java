package game.base.value;

import game.base.common.Generator;

// 해당 게임의 정보들이 담기는 클래스
// 구현한 클래스에서 게임의 정보들을 기입하고, getter로 가져올 수 있도록 함
public abstract class Game<I> {
	// gameId: 클라이언트를 구별 가능한 게임의 고유 id
	private I gameId;
	
	// Game(): 해당 게임이 생성될때만 id를 지정할 수 있음
	public Game(I gameId) {
		this.gameId = gameId;
	}
	
	// getGameId(): Game ID는 바뀌면 안되는 존재이므로, getter만 노출시킴
	public final I getGameId() {
		return this.gameId;
	}
	
	// Game의 정보를 출력함
	@Override
	public String toString() {
		return Generator.generateToString(this);
	}
}
