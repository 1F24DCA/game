package game.base.value;

// 해당 게임의 정보들이 담기는 클래스
// 구현한 클래스에서 게임의 정보들을 기입하고, getter로 가져올 수 있도록 함
public abstract class Game {
	// 클라이언트를 구별 가능한 게임의 고유 id
	private String gameId;
	
	// 해당 게임이 생성될때만 id를 지정할 수 있음
	public Game(String gameId) {
		this.gameId = gameId;
	}
	
	// Game ID는 바뀌면 안되는 존재이므로, getter만 노출시킴
	public final String getGameId() {
		return this.gameId;
	}
}
