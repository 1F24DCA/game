package game.base.service;

import java.lang.reflect.*;
import java.util.*;

import game.base.value.*;

// 게임 서비스 객체, 상속받을 시 "public class ConcreteGameService extends GameService<ConcreteGame> {...}"(으)로 상속받음
// 게임 서비스엔 클라이언트에게 서비스중인 게임들의 정보가 저장된 배열이 기본적으로 필요하므로, 해당 배열(gameList)을 등록함
// 또한, 클라이언트가 해당 게임을 플레이중인지 여부를 물어보고, 중간에 해당 게임id로 생성된 게임을 찾을 수 있도록 메서드를 제공함
// (동적인 저장이 불가능한 웹 등에서 사용하기 위해 만들어둠)
// 또한, 게임 서비스를 구현한 클래스에서 해당 게임을 생성할 수 있도록 강제함
// (이 추상 클래스에서 new G(); 를 이용한 생성은 불가능하므로, 구현하는 클래스에서 생성자를 맡김)
// Generic을 사용하여 해당 게임 서비스와 연관된 게임 클래스를 형변환 없이 사용할 수 있도록 함
public abstract class GameService<G extends Game<?>> extends Service {
	// gameList: 클라이언트들의 모든 게임 정보들을 저장함
	private List<G> gameList = new ArrayList<G>();
	
	// hasGame(): 웹 등에서 동적인 로딩이 불가능한 경우, 처리를 하기 위해 현재 해당 게임이 진행 중인지 여부를 물음
	public final boolean hasGame(String gameId) {
		return this.findGame(gameId) != null;
	}

	// findGame(): 웹 등에서 동적인 로딩이 불가능한 경우, 처리를 하기 위해 현재 진행 중인 게임의 정보를 가져옴
	public final G findGame(String gameId) {
		for (G foundGame : gameList) {
			if (gameId.equals(foundGame.getGameId())) {
				return foundGame;
			}
		}
		return null;
	}
	
	// createGame(): 게임서비스를 구현한 클래스에 Generic Type에 해당하는 게임을 생성함
	public final G createGame(String gameId) {
		G game = null;
//		if (super.raiseEvent(new CheckGameCreateEvent(gameId))) {
//			super.raiseEvent(new PreGameCreateEvent(gameId));
			game = createGameInstance(gameId);
//			super.raiseEvent(new PostGameCreateEvent(game));
//		}
		
		System.out.println("Game created: "+game.toString());
		
		return game;
	}
	
	// createGameInstance(): 구현한 클래스에서 사용한 Generic Type을 이용해 해당 게임 객체를 생성하는 메서드
	@SuppressWarnings({"rawtypes", "unchecked"})
	private final G createGameInstance(String gameId) {
		// Service 객체에서 사용한 방법과 같으므로, 이부분의 설명은 생략함
		ParameterizedType gameService = (ParameterizedType)this.getClass().getGenericSuperclass();
		Type gameType = gameService.getActualTypeArguments()[0];
		
		// 해당 게임 타입의 생성자 메서드를 받아와서 새 객체를 만들어서 G타입을 할당하고 return함
		// 만약 생성자 메서드가 없거나, 모종의 이유로 문제가 발생했다면 런타임 예외를 던짐
		try {
			Constructor gameConstructor = ((Class)gameType).getConstructor(String.class);
			
			return (G)(gameConstructor.newInstance(gameId));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
