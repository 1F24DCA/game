package game.base.service;

import java.util.*;

import game.base.value.*;

// 게임 서비스 객체, 상속받을 시 "public class ConcreteGameService extends GameService<ConcreteGame> {...}"(으)로 상속받음
// 게임 서비스엔 클라이언트에게 서비스중인 게임들의 정보가 저장된 배열이 기본적으로 필요하므로, 해당 배열(gameList)을 등록함
// 또한, 클라이언트가 해당 게임을 플레이중인지 여부를 물어보고, 중간에 해당 게임id로 생성된 게임을 찾을 수 있도록 메서드를 제공함
// (동적인 저장이 불가능한 웹 등에서 사용하기 위해 만들어둠)
// 또한, 게임 서비스를 구현한 클래스에서 해당 게임을 생성할 수 있도록 강제함
// (이 추상 클래스에서 new G(); 를 이용한 생성은 불가능하므로, 구현하는 클래스에서 생성자를 맡김)
// Generic을 사용하여 해당 게임 서비스와 연관된 게임 클래스를 형변환 없이 사용할 수 있도록 함
public abstract class GameService<G extends Game> extends Service {
	// 클라이언트들의 모든 게임 정보들을 저장함
	private List<G> gameList = new ArrayList<G>();
	
	// 이 추상클래스를 구현한 클래스에서 해당 게임을 생성하는 코드를 작성하게끔 강제함
	protected abstract G newGame(String gameId);
	
	// 웹 등에서 동적인 로딩이 불가능한 경우, 처리를 하기 위해 현재 해당 게임이 진행 중인지 여부를 물음
	public final boolean hasGame(String gameId) {
		return this.findGame(gameId) != null;
	}
	
	// 구현한 클래스의 newGame()을 이용해, 게임을 생성함
	public final G createGame(String gameId) {
		G newGame = null;
//		if (super.raiseEvent(new CheckGameCreateEvent(gameId))) {
//			super.raiseEvent(new PreGameCreateEvent(gameId));
			newGame = this.newGame(gameId);
//			super.raiseEvent(new PostGameCreateEvent(game));
//		}
		
		return newGame;
	}

	// 웹 등에서 동적인 로딩이 불가능한 경우, 처리를 하기 위해 현재 진행 중인 게임의 정보를 가져옴
	public final G findGame(String gameId) {
		for (G foundGame : gameList) {
			if (gameId.equals(foundGame.getGameId())) {
				return foundGame;
			}
		}
		return null;
	}
}
