
import java.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

//import StrategyManager.CombatState;
import bwapi.*;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Unitset;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

public class StrategyManager {


	
	int attack_cnt;
	public Position attackTargetPosition;

	UnitControl_MASTER UnitControl_MASTER;


	// 아군
	public Player myPlayer;
	public Race myRace;

	// 적군
	public Player enemyPlayer;
	public Race enemyRace;

	public int[] buildOrderArrayOfMyCombatUnitType; /// 아군 공격 유닛 생산 순서
	public int nextTargetIndexOfBuildOrderArray; /// buildOrderArrayMyCombatUnitType 에서 다음 생산대상 아군 공격 유닛

	// 아군 공격 유닛 종류(타입)
	public UnitType myZergling; /// 저글링
	public UnitType myMutalisk; /// 뮤탈리스크
	public UnitType myUltralisk; /// #미정
	public UnitType myCombatUnitType4; /// #미정
	public UnitType myCombatUnitType5; /// #미정

	// 아군 특수 유닛 첫번째, 두번째 타입
	public UnitType mySpecialUnitType1; /// 옵저버 사이언스베쓸 오버로드
	public UnitType mySpecialUnitType2; /// 하이템플러 배틀크루저 디파일러

	// 아군의 공격유닛 최소 필요 숫자
	public int necessaryNumberOfZergling; /// 공격을 시작하기위해 필요한 최소한의 유닛 숫자
	public int necessaryNumberOfMutalisk; /// 공격을 시작하기위해 필요한 최소한의 유닛 숫자
	public int necessaryNumberOfUltralisk; /// 공격을 시작하기위해 필요한 최소한의 유닛 숫자
	public int necessaryNumberOfCombatUnitType4; /// 공격을 시작하기위해 필요한 최소한의 유닛 숫자
	public int necessaryNumberOfCombatUnitType5;

	public int myKilledZerglings; /// 첫번째 유닛 타입의 사망자 숫자 누적값
	public int myKilledMutalisks; /// 두번째 유닛 타입의 사망자 숫자 누적값
	public int myKilledUltralisks;
	public int myKilledCombatUnitCount4;
	public int myKilledCombatUnitCount5;

	public int numberOfCompletedZerglings; /// 첫번째 유닛 타입의 현재 유닛 숫자
	public int numberOfCompletedMutalisks; /// 두번째 유닛 타입의 현재 유닛 숫자
	public int numberOfCompletedUltralisks; /// 세번째 유닛 타입의 현재 유닛 숫자
	public int numberOfCompletedCombatUnitType4; /// 네번째 유닛 타입의 현재 유닛 숫자
	public int numberOfCompletedCombatUnitType5;


	// 아군 공격 유닛 목록
	public ArrayList<Unit> myAllCombatUnitList = new ArrayList<Unit>();
	public ArrayList<Unit> myZerglingList = new ArrayList<Unit>(); // 저글링
	public ArrayList<Unit> myMutaliskList = new ArrayList<Unit>(); // 뮤탈리스크
	public ArrayList<Unit> myUltraliskList = new ArrayList<Unit>(); // 울트라리스크
	public ArrayList<Unit> myCombatUnitType4List = new ArrayList<Unit>(); // #미정
	public ArrayList<Unit> myCombatUnitType5List = new ArrayList<Unit>(); // #미정

	// 아군 방어 건물 첫번째 타입
	public UnitType myCreepColony; /// 파일런 벙커 크립콜로니

	// 아군 방어 건물 두번째 타입
	public UnitType mySunkenColony; /// 포톤 터렛 성큰콜로니

	// 아군 방어 건물 건설 숫자
	public int necessaryNumberOfCreepColony; /// 방어 건물 건설 갯수
	public int necessaryNumberOfSunkenColony; /// 방어 건물 건설 갯수

	// 아군 방어 건물 건설 위치
	public BuildOrderItem.SeedPositionStrategy seedPositionStrategyOfMyDefenseBuildingType; // 권순우 0617 현상태의 전략 거점이 어딘가를 판단하는것으로 공격 방어와 연계되어야 함																						
	public BuildOrderItem.SeedPositionStrategy seedPositionStrategyOfMyCombatUnitTrainingBuildingType;
	public BuildOrderItem.SeedPositionStrategy buildAtFirstChokePoint;
	public BuildOrderItem.SeedPositionStrategy SeedPositionSpecified;

	// 아군 방어 건물 목록
	public 	ArrayList<Unit> myCreepColonyList = new ArrayList<Unit>(); // 파일런 벙커 크립
	public ArrayList<Unit> mySunkenColonyList = new ArrayList<Unit>(); // 캐논 터렛 성큰

	// 업그레이드 / 리서치 할 것
	public UpgradeType necessaryUpgradeType1; ///
	public UpgradeType necessaryUpgradeType2; ///
	public UpgradeType necessaryUpgradeType3;
	public UpgradeType necessaryUpgradeType4;

	public TechType necessaryTechType1; ///
	public TechType necessaryTechType2; ///
	public TechType necessaryTechType3; ///

	// 적군 공격 유닛 숫자
	public int numberOfCompletedEnemyCombatUnit;
	public int numberOfCompletedEnemyWorkerUnit;

	// 적군 공격 유닛 사망자 수
	public int enemyKilledCombatUnitCount; /// 적군 공격유닛 사망자 숫자 누적값
	public int enemyKilledWorkerUnitCount; /// 적군 일꾼유닛 사망자 숫자 누적값

	// 아군 / 적군의 본진, 첫번째 길목, 두번째 길목
	public BaseLocation myMainBaseLocation;
	public BaseLocation myFirstExpansionLocation;

	public Chokepoint myFirstChokePoint;
	public Chokepoint mySecondChokePoint;

	public BaseLocation enemyMainBaseLocation;
	public BaseLocation enemyFirstExpansionLocation;

	public Chokepoint enemyFirstChokePoint;
	public Chokepoint enemySecondChokePoint;

	public boolean isInitialBuildOrderFinished; /// setInitialBuildOrder 에서 입력한 빌드오더가 다 끝나서 빌드오더큐가 empty 되었는지 여부

	public enum CombatState {
		defenseMode, // 아군 진지 방어
		attackStarted, // 적 공격 시작
		attackMySecondChokepoint, // 아군 두번째 길목까지 공격
		attackEnemySecondChokepoint, // 적진 두번째 길목까지 공격
		attackEnemyFirstExpansionLocation, // 적진 앞마당까지 공격
		attackEnemyFirstChokepoint, attackEnemyMainBaseLocation, // 적진 본진까지 공격
		eliminateEnemy // 적 Eliminate
	};

	public CombatState combatState; /// 전투 상황

	public boolean moveToFirstChokePoint = false;
	public boolean moveToEnemyBaseLocation = false;
	public boolean buildKey;

	public StrategyManager() {

	}

	/// 경기가 시작될 때 일회적으로 전략 초기 세팅 관련 로직을 실행합니다

	public void onStart() {

		// 과거 게임 기록을 로딩합니다
		// loadGameRecordList(); 자꾸 에러가 나서 주석처리함

		/// 변수 초기값을 설정합니다
		setVariables();
		
		UnitControl_MASTER = new UnitControl_MASTER();
		

		BuildOrder_Initial initialBuildOrder = new BuildOrder_Initial();
		initialBuildOrder.setInitialBuildOrder();
		
		

		
		
		


	}

	/// 변수 초기값을 설정합니다
	void setVariables() {
		
		attack_cnt = 0;
		attackTargetPosition = null;
		

		myPlayer = MyBotModule.Broodwar.self();
		myRace = MyBotModule.Broodwar.self().getRace();
		enemyPlayer = InformationManager.Instance().enemyPlayer;

		myKilledZerglings = 0; // 죽은 마린 숫자
		myKilledMutalisks = 0;
		myKilledUltralisks = 0;
		myKilledCombatUnitCount4 = 0;
		myKilledCombatUnitCount5 = 0;

		numberOfCompletedEnemyCombatUnit = 0;
		numberOfCompletedEnemyWorkerUnit = 0;

		enemyKilledCombatUnitCount = 0;
		enemyKilledWorkerUnitCount = 0;

		isInitialBuildOrderFinished = false;
		combatState = CombatState.defenseMode;

		if (myRace == Race.Zerg) {

			// 공격 유닛 종류 설정
			myZergling = UnitType.Zerg_Zergling;
			myMutalisk = UnitType.Zerg_Mutalisk;
			myUltralisk = UnitType.Zerg_Ultralisk;
			// myCombatUnitType4 = UnitType.Terran_Science_Vessel;
			// myCombatUnitType5 = UnitType.Terran_Wraith;

			// 특수 유닛 종류 설정
			mySpecialUnitType1 = UnitType.Zerg_Overlord;
			mySpecialUnitType2 = UnitType.Zerg_Defiler;

			// 공격 모드로 전환하기 위해 필요한 최소한의 유닛 숫자 설정
			necessaryNumberOfZergling = 18; // 공격을 시작하기위해 필요한 최소한의 마린 유닛 숫자
			necessaryNumberOfMutalisk = 5; // 공격을 시작하기위해 필요한 최소한의 메딕 유닛 숫자
			necessaryNumberOfUltralisk = 4; // 공격을 시작하기위해 필요한 최소한의 메딕 유닛 숫자
			// necessaryNumberOfCombatUnitType4 = 1; // 공격을 시작하기위해 필요한 최소한의 메딕 유닛 숫자
			// necessaryNumberOfCombatUnitType5 = 2;

			// MaxNumberOfCombatUnitType4 = 4 ;

			// 공격 유닛 생산 순서 설정
			buildOrderArrayOfMyCombatUnitType = new int[] {1,1,2,1,1,3}; // 마린 마린 마린 메딕 시즈 베슬
			nextTargetIndexOfBuildOrderArray = 0; // 다음 생산 순서 index

			// 방어 건물 종류 및 건설 갯수 설정
			myCreepColony = UnitType.Zerg_Creep_Colony;
			necessaryNumberOfCreepColony = 2;
			mySunkenColony = UnitType.Zerg_Sunken_Colony;
			necessaryNumberOfSunkenColony = 2;

			// 방어 건물 건설 위치 설정
			seedPositionStrategyOfMyDefenseBuildingType = BuildOrderItem.SeedPositionStrategy.FirstChokePoint; // 앞마당
			seedPositionStrategyOfMyCombatUnitTrainingBuildingType = BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation; // 앞마당
			buildAtFirstChokePoint = BuildOrderItem.SeedPositionStrategy.FirstChokePoint;
			SeedPositionSpecified = BuildOrderItem.SeedPositionStrategy.SeedPositionSpecified;

			// 업그레이드 및 리서치 대상 설정
			necessaryUpgradeType1 = UpgradeType.Metabolic_Boost;
			necessaryUpgradeType2 = UpgradeType.Pneumatized_Carapace;
			necessaryUpgradeType3 = UpgradeType.Zerg_Flyer_Attacks; // 공중공업
			necessaryUpgradeType4 = UpgradeType.Zerg_Flyer_Carapace; // 공중방업

			necessaryTechType1 = TechType.Lurker_Aspect;
			necessaryTechType2 = TechType.Consume;
			necessaryTechType3 = TechType.Plague;
		}
	}

	/// 경기 진행 중 매 프레임마다 경기 전략 관련 로직을 실행합니다
	public void update() {
		
		//drawStrategyManagerStatus(); 2017년 버전의 화면 정보 나타내기. 화면이 중첩되서 주석처리함

		//saveGameLog(); 자꾸 에러가 나서 주석처리함

		/// 변수 값을 업데이트 합니다
		updateVariables();
	
		/// 일꾼을 계속 추가 생산합니다
		executeWorkerTraining();
		
		/// Supply DeadLock 예방 및 SupplyProvider 가 부족해질 상황 에 대한 선제적 대응으로서 SupplyProvider를 추가 건설/생산합니다
		executeSupplyManagement();

		/// 방어건물 및 공격유닛 생산 건물을 건설합니다
		executeBuildingConstruction();
					
		/// 업그레이드 및 테크 리서치를 실행합니다
		executeUpgradeAndTechResearch();

		/// 공격유닛을 계속 추가 생산합니다
		executeCombatUnitTraining();

		/// 전반적인 전투 로직 을 갖고 전투를 수행합니다
		executeCombat();

		
		BuildOrder_Last.Instance().lastBuildOrder();
		






		UnitControl_MASTER.update();
		



	}

	
	
	// 권순우 0617 방어 <-> 공격 <-> 앨리(elimination) 3 단계로 구분
	public void executeCombat() {

		int y = 250;

		// 공격을 시작할만한 상황이 되기 전까지는 방어를 합니다
		if (combatState == CombatState.defenseMode) {

			/// 아군 공격유닛 들에게 방어를 지시합니다
			MyBotModule.Broodwar.drawTextScreen(100, y, "Defense Mode");

			commandMyCombatUnitToDefense();


			/// 공격 모드로 전환할 때인지 여부를 판단합니다
			if (isTimeToStartAttack()) {
				MyBotModule.Broodwar.drawTextScreen(100, y, "Attack Mode" + attack_cnt);
				combatState = CombatState.attackStarted;
			}
		}

		// 공격을 시작한 후에는 공격을 계속 실행하다가, 거의 적군 기지를 파괴하면 Eliminate 시키기를 합니다
		else if (combatState == CombatState.attackStarted) {

			/// 아군 공격유닛 들에게 공격을 지시합니다
			MyBotModule.Broodwar.drawTextScreen(100, y, "Attack Mode" + attack_cnt);
			commandMyCombatUnitToAttack();

			/// 방어 모드로 전환할 때인지 여부를 판단합니다
			if (isTimeToStartDefense()) {
				MyBotModule.Broodwar.drawTextScreen(100, y, "Defense Mode");
				combatState = CombatState.defenseMode;

			}

			/// 적군을 Eliminate 시키는 모드로 전환할지 여부를 판단합니다
			if (isTimeToStartElimination()) {
				MyBotModule.Broodwar.drawTextScreen(100, y, "Eliminate Mode");
				combatState = CombatState.eliminateEnemy;
			}
		} else if (combatState == CombatState.eliminateEnemy) {

			/// 적군을 Eliminate 시키도록 아군 공격 유닛들에게 지시합니다
			MyBotModule.Broodwar.drawTextScreen(100, y, "Eliminate Mode");
			commandMyCombatUnitToEliminate();
		}
	}

	///////

	/// 공격 모드로 전환할 때인지 여부를 리턴합니다
	

	boolean isTimeToStartAttack() {

		MyBotModule.Broodwar.drawTextScreen(100, 240, "Wave Count : " + attack_cnt);
		// 유닛 종류별로 최소 숫자 이상 있으면

		if (attack_cnt == 0) {
			if (myZerglingList.size() >= necessaryNumberOfZergling
					&& myMutaliskList.size() >= necessaryNumberOfMutalisk) {
				attack_cnt = attack_cnt + 1;
				return true; // first wave
			}
		}
		else if(myZerglingList.size() > 18)
		{
			attack_cnt = attack_cnt + 1;
			return true;
		}
		else if (myUltraliskList.size() >= necessaryNumberOfUltralisk) {
			attack_cnt = attack_cnt + 1;
			return true;

		}
		return false;
	}

	/// 방어 모드로 전환할 때인지 여부를 리턴합니다
	boolean isTimeToStartDefense() {

		if (myZerglingList.size() < 10) {
			return true;
		}

		int enemyUnitCountAroundMyMainBaseLocation = 0;

		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(myMainBaseLocation.getPosition(),
				8 * Config.TILE_SIZE)) {
			if (unit.getPlayer() == enemyPlayer) {
				enemyUnitCountAroundMyMainBaseLocation++;
			}
		}

		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(myFirstExpansionLocation.getPosition(),
				8 * Config.TILE_SIZE)) {
			if (unit.getPlayer() == enemyPlayer) {
				enemyUnitCountAroundMyMainBaseLocation++;
			}
		}

		if (enemyUnitCountAroundMyMainBaseLocation > 3) {
			return true;
		}

		return false;
	}

	/// 적군을 Eliminate 시키는 모드로 전환할지 여부를 리턴합니다
	boolean isTimeToStartElimination() {
		/* 스코어도 안되고 사망자도 안된다. 0701 그래도 이건 살려서 써볼만한 가치가 있다
		int myScore =0;
		int enemyScore =0;
		
		myScore = myPlayer.getBuildingScore() + myPlayer.getKillScore() + myPlayer.getRazingScore() + myPlayer.getUnitScore();
		enemyScore = enemyPlayer.getBuildingScore() + enemyPlayer.getKillScore() + enemyPlayer.getRazingScore() + enemyPlayer.getUnitScore();
		
		System.out.println("myPlayer.getBuildingScore() : " + myPlayer.getBuildingScore() + "/ enemyPlayer.getBuildingScore() : " + enemyPlayer.getBuildingScore());
		System.out.println("myPlayer.getKillScore() : " + myPlayer.getKillScore() + "/ enemyPlayer.getKillScore() : " + enemyPlayer.getKillScore());
		System.out.println("myPlayer.getRazingScore() : " + myPlayer.getRazingScore() + "/ enemyPlayer.getRazingScore() : " + enemyPlayer.getRazingScore());
		System.out.println("myPlayer.getUnitScore() : " + myPlayer.getUnitScore() + "/ enemyPlayer.getUnitScore() : " + enemyPlayer.getUnitScore());
		System.out.println("myPlayer.deadUnitCount() : " + myPlayer.deadUnitCount() + "/ enemyPlayer.deadUnitCount() : " + enemyPlayer.deadUnitCount());	
		
		if(enemyPlayer.deadUnitCount() > 2 * myPlayer.deadUnitCount())
		{
			System.out.println("eli case 3");
			return true;
		}
		*/
		
		
		
		// 적군 본진에 아군 유닛이 30 이상 도착했으면 거의 게임 끝난 것
		int myUnitCountAroundEnemyMainBaseLocation = 0;
		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(enemyMainBaseLocation.getPosition(), 8 * Config.TILE_SIZE)) 
		{
			if (unit.getPlayer() == myPlayer) {
				myUnitCountAroundEnemyMainBaseLocation++;
			}
		}
		
		if (myUnitCountAroundEnemyMainBaseLocation > 30) {
			System.out.println("eli case 1");
			return true;
		}

		// 30분이 경과했고 내 유닛이 80 이상이라면 
		if (MyBotModule.Broodwar.getFrameCount() > 24*60*30 && myPlayer.supplyUsed() > 80 * 2) {
			System.out.println("eli case 2");
			return true;
		}

		return false;
	}

	/// 권순우 0617 방어를 하다가 적군에 끌어 당겨지지 않게
	/// 일정 범위 이상 나가면 다시 원래 자리로 돌아오는 코드가 필요함
	/// 아군 공격유닛 들에게 방어를 지시합니다
	void commandMyCombatUnitToDefense() {

		// 아군 방어 건물이 세워져있는 위치
		Position myDefenseBuildingPosition = null;
		// 공격받는 건물의 위치가 저장될 변수 - 노승호 170807
		Position myAttackedBuildingPosition = null; 

		
		
		
		
		
		// seedPositionStrategyOfMyDefenseBuildingType 이것이 정하는 위치가 곧 집결지가 된다
		// 아래 사전 정의된 경우를 제외하고서는 specified를 쓰면 된다
		// 활용이 어렵다. 가령 공격을 동시에 여러 지점에서 받는다면 어디로 갈것인가?
		switch (seedPositionStrategyOfMyDefenseBuildingType) {
		case MainBaseLocation:
			myDefenseBuildingPosition = myMainBaseLocation.getPosition();
			break;
		case FirstChokePoint:
			myDefenseBuildingPosition = myFirstChokePoint.getCenter();
			break;
		case FirstExpansionLocation:
			myDefenseBuildingPosition = myFirstExpansionLocation.getPosition();
			break;
		case SecondChokePoint:
			myDefenseBuildingPosition = mySecondChokePoint.getCenter();
			break;

		case SeedPositionSpecified: // 샘플 코드이다. 이렇게 하면 다음 확장기지가 건설될 위치로 집결할 것이다.
			myDefenseBuildingPosition = bwta.BWTA.getNearestChokepoint(BuildOrder_Expansion.expansion().getPosition()).getCenter();
			break;
						
		default:
			myDefenseBuildingPosition = myMainBaseLocation.getPosition();
			break;
		}
		
		
		if (isInitialBuildOrderFinished == true) {
			myDefenseBuildingPosition = setTargetPositionTwo(myFirstExpansionLocation.getPosition(), mySecondChokePoint.getCenter(), 32);
		}
		else
		{
			myDefenseBuildingPosition = setTargetPositionTwo(myFirstExpansionLocation.getPosition(), myFirstChokePoint.getCenter(), 32);
		}
		
		
		
		
		
		
		
		
		
		

		// 아군 공격유닛을 방어 건물이 세워져있는 위치로 배치시킵니다
		// 아군 공격유닛을 아군 방어 건물 뒤쪽에 배치시켰다가 적들이 방어 건물을 공격하기 시작했을 때 다함께 싸우게하면 더 좋을 것입니다
		for (Unit unit : myAllCombatUnitList) {
			if (unit == null || unit.exists() == false)
				continue;

			// 0704 각 타입별 유닛 컨트롤 기능을 구현하면 굳이 현재 메서드에서 모든 유닛을 제어하지 않아도 된다
			// 이 부분에서 방어할 지역이나 콕 집어 공격할 유닛만 리턴해주고, 그걸 개별 유닛컨트롤 클래스에서 알아서 적절히 대응을 하는 방법도 고려
			if(unit.getType() == UnitType.Zerg_Mutalisk) // 0627 뮤탈코드 테스트를 위한 건너뛰기 조치
			{
				continue;
			}
			
			// 이건 기본코드에 있는데 도대체 어느짝에 써먹는지를 모르겠다
			boolean hasCommanded = false;

			for (Unit building : MyBotModule.Broodwar.self().getUnits()) {
				if ((building.getType().isBuilding() && building.isUnderAttack())
						|| (building.getType() == UnitType.Zerg_Drone && building.isUnderAttack()))																								
				{
					myAttackedBuildingPosition = building.getPosition();
					myAttackedBuildingPosition = setTargetPositionOne(myAttackedBuildingPosition, 80);

					List<Unit> nearbyunits = building.getUnitsInRadius(1000);

					ArrayList<Unit> nearbyenemy = new ArrayList<Unit>(); // 0622 여기서 초기화 안하면 에러남

					for (Unit unit222 : nearbyunits) {
						if (unit222.getPlayer() == enemyPlayer && unit222 != null && unit222.exists()) {
							nearbyenemy.add(unit222);
						}
					}

					double minDistance = 1000000000;
					double tempDistance = 0;
					Unit myAttackTarget = null;
					for (Unit targetENEMY : nearbyenemy) {

						if (targetENEMY == null || targetENEMY.exists() == false) {
							continue;
						}

						tempDistance = unit.getDistance(targetENEMY.getPosition());
						if (minDistance > tempDistance) {
							minDistance = tempDistance;
							myAttackTarget = targetENEMY;
						}

					}

					if (unit.canAttack() && myAttackTarget != null) {
						// commandUtil.attackMove(unit, myAttackedBuildingPosition);
						// commandUtil.attackUnit(unit, building);
						unit.attack(myAttackTarget);
					} else {
						commandUtil.attackMove(unit, myAttackedBuildingPosition);
					}

					// System.out.println("건물님 지키러 갑니다");
					hasCommanded = true;
				}
			} // 건물이 데미지를 입었으면 그쪽으로 이동한다 - 노승호 170807

			if (unit.getType() == UnitType.Zerg_Lurker) {
			//	hasCommanded = controlLurkerUnitType(unit);
			}
			if (unit.getType() == mySpecialUnitType1) {
				hasCommanded = controlSpecialUnitType1(unit);
			}
			if (unit.getType() == mySpecialUnitType2) {
				hasCommanded = controlSpecialUnitType2(unit);
			}

			// 따로 명령 내린 적이 없으면, 방어 건물 주위로 이동시킨다
			if (hasCommanded == false) {

				if (unit.isIdle()) {

					if (unit.canAttack()) {
						commandUtil.attackMove(unit, myDefenseBuildingPosition);
					} else {
						commandUtil.move(unit, myDefenseBuildingPosition);
					}
				}
			}
		}

	}



	/// 첫번째 특수 유닛 타입의 유닛에 대해 컨트롤 명령을 입력합니다
	boolean controlSpecialUnitType1(Unit unit) {

		///////////////////////////////////////////////////////////////////
		///////////////////////// 아래의 코드를 수정해보세요 ///////////////////////
		//
		// TODO 1. 아군 옵저버/사이언스베슬/오버로드를 공격 유닛들과 함께 이동하게 하는 로직 (예상 개발시간 10분)
		//
		// 목표 : 첫번째 특수유닛 타입은 적군 투명 유닛 탐지 능력이 있고 시야가 넓은 옵저버/사이언스베슬/오버로드 입니다.
		//
		// 현재는 아군 옵저버/사이언스베슬/오버로드에게 따로 컨트롤 명령을 입력하지 않으면
		// 다른 공격유닛들과 동일하게 적군 본진을 향해 이동하도록 되어있습니다.
		//
		// 그러나 이렇게하면 적군 유닛들이 있는데도 무시하고 계속 이동하다가 사망하게 됩니다
		//
		// 아군 공격 유닛들의 목록 myCombatUnitType1List, myCombatUnitType2List,
		/////////////////////////////////////////////////////////////////// myCombatUnitType3List
		// 을 사용해서, 다른 아군 공격 유닛들과 함께 다니도록 해보세요
		//
		// return false = 유닛에게 따로 컨트롤 명령을 입력하지 않음 -> 다른 공격유닛과 동일하게 이동하도록 합니다
		// return true = 유닛에게 따로 컨트롤 명령을 입력했음
		//
		// Hint : myCombatUnitType1List 에서 랜덤하게 한 유닛을 선택해서 그 유닛을 따라다니게 하면 어떨까요?
		//
		///////////////////////////////////////////////////////////////////

		boolean hasCommanded = false;
		if (unit.getType() == UnitType.Protoss_Observer) {

			Position targetPosition = null;

			// targetPosition 을 적절히 정해서 이동시켜보세요

		} else if (unit.getType() == UnitType.Terran_Science_Vessel) {

			Position targetPosition = null;

			// targetPosition 을 적절히 정해서 이동시켜보세요

			if (unit.getEnergy() >= TechType.Defensive_Matrix.energyCost()) {

				Unit targetMyUnit = null;

				// targetMyUnit 을 적절히 정해보세요

				if (targetMyUnit != null) {
					unit.useTech(TechType.Defensive_Matrix, targetMyUnit);
					hasCommanded = true;
				}
			}

			if (unit.getEnergy() >= TechType.Irradiate.energyCost() && myPlayer.hasResearched(TechType.Irradiate)) {

				Unit targetEnemyUnit = null;

				// targetEnemyUnit 을 적절히 정해보세요

				if (targetEnemyUnit != null) {
					unit.useTech(TechType.Irradiate, targetEnemyUnit);
					hasCommanded = true;
				}
			}

		} else if (unit.getType() == UnitType.Zerg_Overlord) {

			Position targetPosition = null;

			// targetPosition 을 적절히 정해서 이동시켜보세요
		}

		return hasCommanded;
	}

	/// 두번째 특수 유닛 타입의 유닛에 대해 컨트롤 명령을 내립니다
	boolean controlSpecialUnitType2(Unit unit) {

		///////////////////////////////////////////////////////////////////
		///////////////////////// 아래의 코드를 수정해보세요 ///////////////////////
		//
		// TODO 2. 아군 하이템플러/배틀크루저/디파일러가 특수 기술을 사용하게 하는 로직 (예상 개발시간 20분)
		//
		// 목표 : 두번째 특수유닛 타입은 특수 기술을 갖고있는 하이템플러/배틀크루저/디파일러 입니다.
		//
		// 현재는 특수기술 사용 대상을 정하는 로직이 구현 안되어있습니다.
		//
		// 적군 유닛들의 목록 MyBotModule.Broodwar.enemy().getUnits() 을 사용하여
		// 특수 기술 사용 대상을 적절히 정하도록 해보세요
		//
		// return false = 유닛에게 따로 컨트롤 명령을 입력하지 않음 -> 다른 공격유닛과 동일하게 이동하도록 합니다
		// return true = 유닛에게 따로 컨트롤 명령을 입력했음
		//
		// 추가 : 테란 종족 첫번째 특수유닛 타입 사이언스베슬에 대해서도 특수 기술을 사용하게 하려면
		// controlSpecialUnitType1 함수를 수정하시면 됩니다
		//
		///////////////////////////////////////////////////////////////////

		boolean hasCommanded = false;

		// 프로토스 종족 하이템플러의 경우
		if (unit.getType() == UnitType.Protoss_High_Templar) {

			if (unit.getEnergy() >= TechType.Psionic_Storm.energyCost()
					&& myPlayer.hasResearched(TechType.Psionic_Storm)) {

				Position targetPosition = null;

				// targetPosition 을 적절히 정해보세요

				if (targetPosition != null) {
					unit.useTech(TechType.Psionic_Storm, targetPosition);
					hasCommanded = true;
				}
			}
		} else if (unit.getType() == UnitType.Terran_Battlecruiser) {

			if (unit.getEnergy() >= TechType.Yamato_Gun.energyCost() && myPlayer.hasResearched(TechType.Yamato_Gun)) {

				Unit targetEnemyUnit = null;

				// targetEnemyUnit 을 적절히 정해보세요

				if (targetEnemyUnit != null) {
					unit.useTech(TechType.Yamato_Gun, targetEnemyUnit);
					hasCommanded = true;
				}
			}
		} else if (unit.getType() == UnitType.Zerg_Defiler) {

			if (unit.getEnergy() < 200 && myPlayer.hasResearched(TechType.Consume)) {

				Unit targetMyUnit = null;

				// 가장 가까운 저글링을 컨슘 한다
				double minDistance = 1000000000;
				double tempDistance = 0;
				for (Unit zerglingUnit : myZerglingList) {
					tempDistance = unit.getDistance(zerglingUnit.getPosition());
					if (minDistance > tempDistance) {
						minDistance = tempDistance;
						targetMyUnit = zerglingUnit;
					}
				}

				if (targetMyUnit != null) {
					unit.useTech(TechType.Consume, targetMyUnit);
					hasCommanded = true;
				}
			}

			if (unit.getEnergy() >= TechType.Plague.energyCost() && myPlayer.hasResearched(TechType.Plague)) {

				Unit targetEnemyUnit = null;

				// targetEnemyUnit 을 적절히 정해보세요

				if (targetEnemyUnit != null) {
					unit.useTech(TechType.Plague, targetEnemyUnit);
					hasCommanded = true;
				}
			} else if (unit.getEnergy() >= TechType.Dark_Swarm.energyCost()) {

				Position targetPosition = null;

				// targetPosition 을 적절히 정해보세요

				if (targetPosition != null) {
					unit.useTech(TechType.Dark_Swarm, targetPosition);
					hasCommanded = true;
				}
			}
		}

		return hasCommanded;
	}

	Position setTargetPositionOne(Position A, int C) {
		Random random = new Random();

		int x;
		int y;

		Position targetPosition;

		x = random.nextInt(C);
		x = x + random.nextInt(C * 2) * (-1);

		y = random.nextInt(C);
		y = y + random.nextInt(C * 2) * (-1);

		/*
		 * while(true) { x = random.nextInt(C); x = x + random.nextInt(C*2)*(-1);
		 * 
		 * y = random.nextInt(C); y = y + random.nextInt(C*2)*(-1);
		 * 
		 * break; }
		 */

		targetPosition = new Position(A.getX() + x, A.getY() + y);
		// System.out.println(7);
		return targetPosition;
	}

	Position setTargetPositionTwo(Position A, Position B, int C) {
		Random random = new Random();

		int x;
		int y;

		Position targetPosition;

		x = random.nextInt(C);
		x = x + random.nextInt(C * 2) * (-1);

		y = random.nextInt(C);
		y = y + random.nextInt(C * 2) * (-1);

		/*
		 * while(true) { x = random.nextInt(C); x = x + random.nextInt(C*2)*(-1);
		 * 
		 * y = random.nextInt(C); y = y + random.nextInt(C*2)*(-1);
		 * 
		 * break; }
		 */

		targetPosition = new Position((A.getX() + B.getX()) / 2 + x, (A.getY() + B.getY()) / 2 + y);
		// System.out.println(6);
		return targetPosition;
	}

	int countEnemyAround(Position A, int B) {
		int enemyNum = 0;

		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(A.getPoint(), B * Config.TILE_SIZE)) {
			if (unit.getPlayer() == enemyPlayer) {
				enemyNum = enemyNum + 1;
			}
		}

		// System.out.println("enemyNum :"+ enemyNum);

		return enemyNum;
	}

	int countMyAttackUnitAround(Position A, int B) {
		int myNum = 0;

		for (Unit unit : MyBotModule.Broodwar.getUnitsInRadius(A.getPoint(), B * Config.TILE_SIZE)) {
			if (unit.getPlayer() == myPlayer && unit.canAttack()) {
				myNum = myNum + 1;
			}
		}

		// System.out.println("enemyNum :"+ enemyNum);
		return myNum;
	}

	int countMyAttackUnitAll() {
		int myNum = 0;

		for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
			if (unit.canAttack()) {
				if (unit.getType().isBuilding() == false && unit.getType() != UnitType.Zerg_Drone
						&& unit.getType() != UnitType.Zerg_Larva && unit.getType() != UnitType.Zerg_Overlord) {
					myNum = myNum + 1;
				}
			}
		}

		return myNum;
	}

	/// 아군 공격 유닛들에게 공격을 지시합니다
	public void commandMyCombatUnitToAttack() {

		// 최종 타겟은 적군의 Main BaseLocation

		/////////////// 20170806 권순우 공격지점 설정

		BaseLocation targetEnemyBaseLocation = enemyMainBaseLocation;
		Chokepoint targetPoint = enemySecondChokePoint;
		


		if (targetPoint != null) {

			// 권순우 0617 두 지점사이의 위치를 구해서 이동목표로 삼는 것인데 그 지점을 여러개 설정하는 끔찍한 모습이다.
			Position position01 = setTargetPositionTwo(mySecondChokePoint.getPoint(), enemySecondChokePoint.getPoint(),	1);
			position01 = setTargetPositionTwo(position01, enemySecondChokePoint.getPoint(), 1);
			position01 = setTargetPositionTwo(position01, enemySecondChokePoint.getPoint(), 30);

			Position position02 = setTargetPositionTwo(position01, enemySecondChokePoint.getPoint(), 30);
			Position position03 = setTargetPositionOne(enemySecondChokePoint.getPoint(), 35);
			Position position04 = setTargetPositionTwo(position03, enemyFirstExpansionLocation.getPoint(), 45);
			Position position05 = setTargetPositionTwo(position04, enemyMainBaseLocation.getPoint(), 45);
			Position position06 = setTargetPositionOne(enemyMainBaseLocation.getPoint().getPoint(), 100);

			// Position position07 = setTargetPositionTwo(mySecondChokePoint.getPoint(),
			// enemySecondChokePoint.getPoint(), 30);

			// for (Unit unit : myAllCombatUnitList)

			attackTargetPosition = position01;
			if (myAllCombatUnitList.size() > 0.5 * myAllCombatUnitList.size()) {
				attackTargetPosition = position02;
				if (myAllCombatUnitList.size() > 0.5 * myAllCombatUnitList.size()) {
					attackTargetPosition = position03;
					if (myAllCombatUnitList.size() > 0.5 * myAllCombatUnitList.size()) {
						attackTargetPosition = position04;
						if (myAllCombatUnitList.size() > 0.5 * myAllCombatUnitList.size()) {
							attackTargetPosition = position05;
							if (myAllCombatUnitList.size() > 0.5 * myAllCombatUnitList.size()) {
								attackTargetPosition = position06;
							}
						}
					}
				}
			}

			/*
			 * 권순우 0617 노승호의 역작이다. 올해는 아래 코드를 변형해서 오버로드를 은신캐 근처로 이동시키는 코드를 짜면 되겠다.
			 * 
			 * 제일 좋은 것은 은신캐 근처에서 가장 가까운 오버로드를 이동시키는 것인데 적군 유닛의 위치를 중심으로 가장 가까운 아군 유닛중에서
			 * 오버로드면 와라!
			 * 
			 * 
			 * 
			 * for (Unit scanner : MyBotModule.Broodwar.self().getUnits()) {
			 * 
			 * if (scanner.getType() == UnitType.Terran_Comsat_Station) { for(Unit
			 * isInvisible : MyBotModule.Broodwar.getUnitsInRadius(scanner.getPoint(), 256 *
			 * Config.TILE_SIZE)) { if(isInvisible.isBurrowed() == true ||
			 * isInvisible.isCloaked() == true) { if(scanner.getEnergy() >=
			 * TechType.Scanner_Sweep.energyCost()) {
			 * scanner.useTech(TechType.Scanner_Sweep, isInvisible);
			 * System.out.println("스캔!"); } } } } }//20170808 스캐너 구현 완료.
			 */

			// 모든 아군 공격유닛들로 하여금 targetPosition 을 향해 공격하게 한다
			for (Unit unit : myAllCombatUnitList) {

				boolean hasCommanded = false;

				// 따로 명령 내린 적이 없으면, targetPosition 을 향해 공격 이동시킵니다
				if (hasCommanded == false) {

					if (unit.isIdle()) {

						if (unit.canAttack()) {
							// 권순우 0617 공격을 할 수는 있지만
							// 전략이나 업그레이드 등 전략적인 사유로 공격을 나가서는 안된다면
							// 이 부분에서 그런것을 체크해야 한다
							// 가령 아래 레이스는 은신이 개발 안되면 모두 공격나가도 공격 안따라가는 것이다.
							if (unit.getType() == UnitType.Zerg_Mutalisk)// && myCombatUnitType5List.size()<6 &&
																			// !myPlayer.hasResearched(TechType.Cloaking_Field))
							{							
								hasCommanded = true;
							} else {
								commandUtil.attackMove(unit, attackTargetPosition);
								hasCommanded = true;
							}
						}

						else {

							/*
							 * 권순우 0617 가장 까다로운 부분이다. 메딕, 사이언스 베슬 등 특수 유닛은 일단 전투지역으로 이동을 시키고 뭔가를 해야되는데 이 부분을
							 * 해결하는 것이 매우 까다롭다.
							 * 
							 * 올해는 각 유닛별로 고유번호가 있다는 것을 알았으니 단순히 가까운 유닛을 찾는 것이 아니라 전담 마크 개념을 구현해 볼 수 있을 것이다.
							 * 과연?
							 */

							// canAttack 기능이 없는 유닛타입 중 메딕은 마린 유닛에 대해 Heal 하러 가게 하고, 마린 유닛이 없으면 아군 지역으로 돌아오게
							// 합니다
							if (unit.getType() == UnitType.Terran_Medic) {

								Position targetMyUnitPosition = null;
								Random random = new Random();
								Unit randomMarine;

								int breakCondition = 0;
								int size = myZerglingList.size();
								int isNear = 100;

								while (true) {
									randomMarine = myZerglingList.get(random.nextInt(size));
									if (randomMarine == null || randomMarine.exists() == false
											|| randomMarine.getHitPoints() < 0) {
										// 일단 마린이 null이거나, 죽었거나, 풀피면 다른 마린을 찾는다
										continue;
									}
									if (randomMarine.getHitPoints() < randomMarine.getInitialHitPoints()) {
										// 맞긴 했는데 아직 살았다면 힐줘라
										unit.useTech(TechType.Healing, randomMarine);
										// hasCommanded = true;
										break;
									} else if (breakCondition == size) {
										// 가까운 마린이면 그냥 따라가고
										// 멀면 새로운 마린을 찾아서 따라가고
										// 루프 탈출

										if (unit.getDistance(randomMarine) < isNear) {
											unit.follow(randomMarine);
											// hasCommanded = true;
											break;
										} else {
											isNear = isNear + 10;
										}
									} else {
										breakCondition = breakCondition + 1;
									}
								}

								for (Unit myUnit : myZerglingList) {
									if (myUnit == null || myUnit.exists() == false || myUnit.getHitPoints() < 0) {
										continue;
									}
									if (myUnit.getHitPoints() < myUnit.getInitialHitPoints()
											|| random.nextInt() % 2 == 0) {
										targetMyUnitPosition = myUnit.getPosition();
										break;
									}
								}
								if (targetMyUnitPosition != null) {
									unit.useTech(TechType.Healing, targetMyUnitPosition);
									hasCommanded = true;
								} else {
									unit.useTech(TechType.Healing, mySecondChokePoint.getCenter());
									hasCommanded = true;
								}

							}

							// canAttack 기능이 없는 유닛타입 중 러커는 일반 공격유닛처럼 targetPosition 을 향해 이동시킵니다
							else if (unit.getType() == UnitType.Zerg_Lurker) {
								commandUtil.move(unit, attackTargetPosition);
								hasCommanded = true;
							} else if (unit.getType() == UnitType.Terran_Science_Vessel) {

								// Position targetMyUnitPosition = null;

								Random random = new Random();

								Unit randomMarine;

								int breakCondition = 0;
								int size = myZerglingList.size();
								int isNear = 100;

								while (true) {
									randomMarine = myZerglingList.get(random.nextInt(size));
									if (randomMarine.isStartingAttack() || randomMarine.isAttacking()
											|| randomMarine.isUnderAttack()) {
										unit.follow(randomMarine);
										hasCommanded = true;
										break;
									} else if (breakCondition == size) {
										// 가까운 마린이면 그냥 따라가고
										// 멀면 새로운 마린을 찾아서 따라가고
										// 루프 탈출
										if (unit.getDistance(randomMarine) < isNear) {
											unit.follow(randomMarine);
											hasCommanded = true;
											break;
										} else {
											isNear = isNear + 10;
										}
									} else {
										breakCondition = breakCondition + 1;
									}
								}

								for (Unit myUnit : myZerglingList) {
									unit.follow(myUnit);
									hasCommanded = true;

								}

							}

							// canAttack 기능이 없는 다른 유닛타입 (하이템플러, 옵저버, 사이언스베슬, 오버로드) 는
							// 따로 명령을 내린 적이 없으면 다른 공격유닛들과 동일하게 이동하도록 되어있습니다.
							else {
								commandUtil.move(unit, attackTargetPosition);
								hasCommanded = true;
							}
						}
					}
				}
			}
		}
	}

	/// 적군을 Eliminate 시키도록 아군 공격 유닛들에게 지시합니다
	void commandMyCombatUnitToEliminate() {

		if (enemyPlayer == null || enemyRace == Race.Unknown) {
			return;
		}

		Random random = new Random();
		int mapHeight = MyBotModule.Broodwar.mapHeight(); // 128
		int mapWidth = MyBotModule.Broodwar.mapWidth(); // 128

		// 아군 공격 유닛들로 하여금 적군의 남은 건물을 알고 있으면 그것을 공격하게 하고, 그렇지 않으면 맵 전체를 랜덤하게 돌아다니도록 합니다

		Unit targetEnemyBuilding = null;

		for (Unit enemyUnit : enemyPlayer.getUnits()) {

			if (enemyUnit == null || enemyUnit.exists() == false || enemyUnit.getHitPoints() < 0)
				continue;

			if (enemyUnit.getType().isBuilding()) {
				targetEnemyBuilding = enemyUnit;
				break;
			}
		}

		for (Unit unit : myAllCombatUnitList) {

			boolean hasCommanded = false;

			if (unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode
					|| unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
				//hasCommanded = controlSiegeTankUnitType(unit);
			}

			// 따로 명령 내린 적이 없으면, 적군의 남은 건물 혹은 랜덤 위치로 이동시킨다
			if (hasCommanded == false) {

				if (unit.isIdle()) {

					Position targetPosition = null;
					if (targetEnemyBuilding != null) {
						targetPosition = targetEnemyBuilding.getPosition();
					} else {
						targetPosition = new Position(random.nextInt(mapWidth * Config.TILE_SIZE),
								random.nextInt(mapHeight * Config.TILE_SIZE));
					}

					if (unit.canAttack()) {
						commandUtil.attackMove(unit, targetPosition);
						hasCommanded = true;
					} else {

						// canAttack 기능이 없는 유닛타입 중 메딕은 마린 유닛에 대해 Heal 하러 가게 하고, 마린 유닛이 없으면 아군 지역으로 돌아오게
						// 합니다
						if (unit.getType() == UnitType.Terran_Medic) {
							Position targetMyUnitPosition = null;

							for (Unit myUnit : myZerglingList) {

								if (myUnit == null || myUnit.exists() == false || myUnit.getHitPoints() < 0) {
									continue;
								}

								if (myUnit.getHitPoints() < myUnit.getInitialHitPoints() || random.nextInt() % 2 == 0) {
									targetMyUnitPosition = myUnit.getPosition();
									break;
								}
							}

							if (targetMyUnitPosition != null) {

								unit.useTech(TechType.Healing, targetMyUnitPosition);
								hasCommanded = true;
							} else {
								unit.useTech(TechType.Healing, mySecondChokePoint.getCenter());
								hasCommanded = true;
							}
						}

						// canAttack 기능이 없는 유닛타입 중 러커는 일반 공격유닛처럼 targetPosition 을 향해 이동시킵니다
						else if (unit.getType() == UnitType.Zerg_Lurker) {
							commandUtil.move(unit, targetPosition);
							hasCommanded = true;
						}

						// canAttack 기능이 없는 다른 유닛타입 (하이템플러, 옵저버, 사이언스베슬, 오버로드) 는
						// 따로 명령을 내린 적이 없으면 다른 공격유닛들과 동일하게 이동하도록 되어있습니다.

						else {
							commandUtil.move(unit, targetPosition);
							hasCommanded = true;
						}
					}
				}
			}
		}
	}


	// 일꾼 계속 추가 생산
	public void executeWorkerTraining() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		if (MyBotModule.Broodwar.self().minerals() >= 50) {
			// workerCount = 현재 일꾼 수 + 생산중인 일꾼 수
			//int workerCount = MyBotModule.Broodwar.self().allUnitCount(UnitType.Zerg_Drone);

			int workerCount = 0;
			
			
			for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
				
				if (unit.getType() == UnitType.Zerg_Drone)
				{
					workerCount++;
				}
				else if (unit.getType() == UnitType.Zerg_Egg) {
					// Zerg_Egg 에게 morph 명령을 내리면 isMorphing = true,
					// isBeingConstructed = true, isConstructing = true 가 된다
					// Zerg_Egg 가 다른 유닛으로 바뀌면서 새로 만들어진 유닛은 잠시
					// isBeingConstructed = true, isConstructing = true 가
					// 되었다가,
					if (unit.isMorphing() && unit.getBuildType() == UnitType.Zerg_Drone) {
						workerCount++;
					}
				}
			}	

			int numberOfMyCombatUnitTrainingBuilding = 0;
			numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Hatchery);
			numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Lair);
			numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Hive);

			numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery);
			numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Lair);
			numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hive);

			numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
					.getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null);
			numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
					.getConstructionQueueItemCount(UnitType.Zerg_Lair, null);
			numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
					.getConstructionQueueItemCount(UnitType.Zerg_Hive, null);
			
			
			
			if(workerCount>60)
			{
				return;
			}
			
			/* 0630 해처리 숫자 기반 최대치 설정
			if(numberOfMyCombatUnitTrainingBuilding>=8 && workerCount > 60)
			{
					return;
			}
			else if(numberOfMyCombatUnitTrainingBuilding>=6 && workerCount > 50)
			{
					return;
			}
			else if(numberOfMyCombatUnitTrainingBuilding>=4 && workerCount > 40)
			{
				return;
			}
			*/
			
			
			
			int optimalWorkerCount = 0;
			for (BaseLocation baseLocation : InformationManager.Instance()
					.getOccupiedBaseLocations(InformationManager.Instance().selfPlayer)) {
				optimalWorkerCount += baseLocation.getMinerals().size() * 1.5;
				optimalWorkerCount += baseLocation.getGeysers().size() * 3;
			}

			//System.out.println("현재 : " + workerCount + " / 최적 : " + optimalWorkerCount);
			
			
			
			if (workerCount < optimalWorkerCount) {
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
					if (unit.getType().equals(UnitType.Zerg_Hatchery) 
							|| unit.getType().equals(UnitType.Zerg_Lair) 
							|| unit.getType().equals(UnitType.Zerg_Hive)) 
					{
					//	if (unit.isTraining() == false || unit.getLarva().size() > 0) {
						if (unit.getTrainingQueue().contains(UnitType.Zerg_Drone) == false || unit.getLarva().size() > 0) {
							// 빌드큐에 일꾼 생산이 1개는 있도록 한다
							if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Drone, null) == 0) {

								BuildManager.Instance().buildQueue.queueAsLowestPriority(
										new MetaType(InformationManager.Instance().getWorkerType()), false);
							}
						}
					}
				}
			}
		}
	}

	/// Supply DeadLock 예방 및 SupplyProvider 가 부족해질 상황 에 대한 선제적 대응으로서
	/// SupplyProvider를 추가 건설/생산합니다
	public void executeSupplyManagement() {

		// BasicBot 1.1 Patch Start ////////////////////////////////////////////////
		// 가이드 추가 및 콘솔 출력 명령 주석 처리
		// InitialBuildOrder 진행중 혹은 그후라도 서플라이 건물이 파괴되어 데드락이 발생할 수 있는데, 이 상황에 대한 해결은
		// 참가자께서 해주셔야 합니다.
		// 오버로드가 학살당하거나, 서플라이 건물이 집중 파괴되는 상황에 대해 무조건적으로 서플라이 빌드 추가를 실행하기 보다 먼저 전략적 대책
		// 판단이 필요할 것입니다
		// BWAPI::Broodwar->self()->supplyUsed() >
		// BWAPI::Broodwar->self()->supplyTotal() 인 상황이거나
		// BWAPI::Broodwar->self()->supplyUsed() + 빌드매니저 최상단 훈련 대상 유닛의
		// unit->getType().supplyRequired() > BWAPI::Broodwar->self()->supplyTotal() 인
		// 경우
		// 서플라이 추가를 하지 않으면 더이상 유닛 훈련이 안되기 때문에 deadlock 상황이라고 볼 수도 있습니다.
		// 저그 종족의 경우 일꾼을 건물로 Morph 시킬 수 있기 때문에 고의적으로 이런 상황을 만들기도 하고,
		// 전투에 의해 유닛이 많이 죽을 것으로 예상되는 상황에서는 고의적으로 서플라이 추가를 하지 않을수도 있기 때문에
		// 참가자께서 잘 판단하셔서 개발하시기 바랍니다.

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		// InitialBuildOrder 진행중이라도 supplyUsed 가 supplyTotal 보다 커져버리면 실행하도록 합니다
		if (isInitialBuildOrderFinished == false
				&& MyBotModule.Broodwar.self().supplyUsed() <= MyBotModule.Broodwar.self().supplyTotal()) {
			return;
		}

		// 1초에 한번만 실행
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}

		// 게임에서는 서플라이 값이 200까지 있지만, BWAPI 에서는 서플라이 값이 400까지 있다
		// 저글링 1마리가 게임에서는 서플라이를 0.5 차지하지만, BWAPI 에서는 서플라이를 1 차지한다
		if (MyBotModule.Broodwar.self().supplyTotal() < 400) {
			// 서플라이가 다 꽉찼을때 새 서플라이를 지으면 지연이 많이 일어나므로, supplyMargin (게임에서의 서플라이 마진 값의 2배)만큼
			// 부족해지면 새 서플라이를 짓도록 한다
			// 이렇게 값을 정해놓으면, 게임 초반부에는 서플라이를 너무 일찍 짓고, 게임 후반부에는 서플라이를 너무 늦게 짓게 된다
			int supplyMargin = 12;
			
			
			if(myPlayer.completedUnitCount(UnitType.Zerg_Hatchery)>2)
			{
				supplyMargin = 36;
			}
			
			

			// currentSupplyShortage 를 계산한다
			int currentSupplyShortage = MyBotModule.Broodwar.self().supplyUsed() + supplyMargin
					- MyBotModule.Broodwar.self().supplyTotal();

			if (currentSupplyShortage > 0) {
				// 생산/건설 중인 Supply를 센다
				int onBuildingSupplyCount = 0;

				// 저그 종족인 경우, 생산중인 Zerg_Overlord (Zerg_Egg) 를 센다. Hatchery 등 건물은 세지 않는다
				if (MyBotModule.Broodwar.self().getRace() == Race.Zerg) {

					for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {

						if (unit.getType() == UnitType.Zerg_Egg && unit.getBuildType() == UnitType.Zerg_Overlord) {

							onBuildingSupplyCount += UnitType.Zerg_Overlord.supplyProvided();
						}

						// 갓태어난 Overlord 는 아직 SupplyTotal 에 반영안되어서, 추가 카운트를 해줘야함
						if (unit.getType() == UnitType.Zerg_Overlord && unit.isConstructing()) {

							onBuildingSupplyCount += UnitType.Zerg_Overlord.supplyProvided();
						}
					}
				}

				// 저그 종족이 아닌 경우, 건설중인 Protoss_Pylon, Terran_Supply_Depot 를 센다. Nexus, Command
				// Center 등 건물은 세지 않는다
				else {
					onBuildingSupplyCount += ConstructionManager.Instance().getConstructionQueueItemCount(
							InformationManager.Instance().getBasicSupplyProviderUnitType(), null)
							* InformationManager.Instance().getBasicSupplyProviderUnitType().supplyProvided();
				}

				// 주석처리
				// System.out.println("currentSupplyShortage : " + currentSupplyShortage + "
				// onBuildingSupplyCount : " + onBuildingSupplyCount);

				if (currentSupplyShortage > onBuildingSupplyCount) {
					// BuildQueue 최상단에 SupplyProvider 가 있지 않으면 enqueue 한다
					boolean isToEnqueue = true;

					if (!BuildManager.Instance().buildQueue.isEmpty()) {
						BuildOrderItem currentItem = BuildManager.Instance().buildQueue.getHighestPriorityItem();
						if (currentItem.metaType.isUnit() && currentItem.metaType.getUnitType() == InformationManager
								.Instance().getBasicSupplyProviderUnitType()) {
							isToEnqueue = false;
						}
					}

					if (isToEnqueue) {
						// 주석처리
						// System.out.println("enqueue supply provider "
						// + InformationManager.Instance().getBasicSupplyProviderUnitType());
						BuildManager.Instance().buildQueue.queueAsHighestPriority(
								new MetaType(InformationManager.Instance().getBasicSupplyProviderUnitType()), true);
					}

				}

			}

		}
		// BasicBot 1.1 Patch End ////////////////////////////////////////////////
	}

	/// 방어건물 및 공격유닛 생산 건물을 건설합니다

	public void executeBuildingConstruction() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}
		

		boolean isPossibleToConstructDefenseBuildingType1 = false;
		boolean isPossibleToConstructDefenseBuildingType2 = false;

		// 권순우 0617 방어 건물을 짓는데 흥미로운 것은
		// 이미 존재하거나, 짓고 있거나, 지을 예정인 것도 모두 "존재"한다고 해야만
		// 목표치 이상을 초과 건설하는 문제가 발생하지 않는다.
		// 방어 건물 증설을 우선적으로 실시한다
		// 현재 방어 건물 갯수

		int numberOfMyDefenseBuildingType1 = 0;
		int numberOfMyDefenseBuildingType2 = 0;

		// 저그의 경우 크립 콜로니 갯수를 셀 때 성큰 콜로니 갯수까지 포함해서 세어야, 크립 콜로니를 지정한 숫자까지만 만든다
		numberOfMyDefenseBuildingType1 += myPlayer.allUnitCount(myCreepColony);
		numberOfMyDefenseBuildingType1 += BuildManager.Instance().buildQueue.getItemCount(myCreepColony);
		numberOfMyDefenseBuildingType1 += ConstructionManager.Instance()
				.getConstructionQueueItemCount(myCreepColony, null);
		numberOfMyDefenseBuildingType1 += myPlayer.allUnitCount(mySunkenColony);
		numberOfMyDefenseBuildingType1 += BuildManager.Instance().buildQueue.getItemCount(mySunkenColony);
		
		////////////////////////////////////////////////////////////////////////////////
		
		numberOfMyDefenseBuildingType2 += myPlayer.allUnitCount(mySunkenColony);
		numberOfMyDefenseBuildingType2 += BuildManager.Instance().buildQueue.getItemCount(mySunkenColony);

		if (myPlayer.completedUnitCount(UnitType.Zerg_Spawning_Pool) > 0) {
			isPossibleToConstructDefenseBuildingType1 = true;
		}

		if (myPlayer.completedUnitCount(UnitType.Zerg_Creep_Colony) > 0) {
			isPossibleToConstructDefenseBuildingType2 = true;
		}

		if (isPossibleToConstructDefenseBuildingType1 == true
				&& numberOfMyDefenseBuildingType1 < necessaryNumberOfCreepColony) {

			if (BuildManager.Instance().buildQueue.getItemCount(myCreepColony) == 0) {

				if (BuildManager.Instance().getAvailableMinerals() >= myCreepColony.mineralPrice()) {

					BuildManager.Instance().buildQueue.queueAsLowestPriority(myCreepColony,
							BuildOrderItem.SeedPositionStrategy.SecondChokePoint, false);
				}
			}
		}

		if (isPossibleToConstructDefenseBuildingType2 == true
				&& numberOfMyDefenseBuildingType2 < necessaryNumberOfSunkenColony) {

			if (BuildManager.Instance().buildQueue.getItemCount(mySunkenColony) == 0) {

				if (BuildManager.Instance().getAvailableMinerals() >= mySunkenColony.mineralPrice()) {

					BuildManager.Instance().buildQueue.queueAsLowestPriority(mySunkenColony,
							BuildOrderItem.SeedPositionStrategy.SecondChokePoint, false);
				}
			}
		}

		// 현재 공격 유닛 생산 건물 갯수

		int numberOfMyCombatUnitTrainingBuilding = 0;
		numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Hatchery);
		numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Lair);
		numberOfMyCombatUnitTrainingBuilding += myPlayer.allUnitCount(UnitType.Zerg_Hive);

		numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery);
		numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Lair);
		numberOfMyCombatUnitTrainingBuilding += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hive);

		numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
				.getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null);
		numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
				.getConstructionQueueItemCount(UnitType.Zerg_Lair, null);
		numberOfMyCombatUnitTrainingBuilding += ConstructionManager.Instance()
				.getConstructionQueueItemCount(UnitType.Zerg_Hive, null);

		// 공격 유닛 생산 건물 증설 : 돈이 남아돌면 실시. 최대 6개 까지만
		
		BaseLocation nextExpansion = null;
		nextExpansion = BuildOrder_Expansion.expansion();
		
		
		if (myPlayer.completedUnitCount(UnitType.Zerg_Drone) > 19 && numberOfMyCombatUnitTrainingBuilding == 2 && nextExpansion!=null) 
		{
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery) == 0 
					&& ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null) == 0) 
			{
				BuildManager.Instance().buildQueue.queueAsHighestPriority(UnitType.Zerg_Hatchery,
						nextExpansion.getTilePosition(), true); /// 해처리 추가 확장 0622

				System.out.println("1");

				if (nextExpansion.getGeysers().size()>0) 
				{
					BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Extractor,
							nextExpansion.getTilePosition(), false); /// 해처리 추가 확장 0622

					System.out.println("2");
				}
			}
		}
		else if (BuildManager.Instance().getAvailableMinerals() > 300 && numberOfMyCombatUnitTrainingBuilding == 3) 
		{
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery) <2 
					&& ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null) <2) 
			{
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
						BuildOrderItem.SeedPositionStrategy.FirstChokePoint, false); /// 해처리 추가 확장 0622

				System.out.println("3");

			}
		}
		/*
		else if (BuildManager.Instance().getAvailableMinerals() > 300 && numberOfMyCombatUnitTrainingBuilding == 5) 
		{
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery) <2 
					&& ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null) <2) 
			{
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
						BuildOrderItem.SeedPositionStrategy.FirstChokePoint, true); /// 해처리 추가 확장 0622

				System.out.println("4");

			}
		}
		*/
		else if (BuildManager.Instance().getAvailableMinerals() > 350 && numberOfMyCombatUnitTrainingBuilding >= 5 && nextExpansion!=null) 
		{
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery) == 0 
					&& ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null) == 0) 
			{
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
						nextExpansion.getTilePosition(), true); /// 해처리 추가 확장 0622

				System.out.println("5");

				if (nextExpansion.getGeysers().size()>0) 
				{
					BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Extractor,
							nextExpansion.getTilePosition(), true); /// 해처리 추가 확장 0622

					System.out.println("6");
				}
			}

		} 
		else if (BuildManager.Instance().getAvailableMinerals() > 350 && numberOfMyCombatUnitTrainingBuilding < 13 && nextExpansion!=null) 
		{
			if (BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Hatchery) <2 
					&& ConstructionManager.Instance().getConstructionQueueItemCount(UnitType.Zerg_Hatchery, null) <2) 
			{
				BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
						nextExpansion.getTilePosition(), false); /// 해처리 추가 확장 0622

				System.out.println("7");

				if (nextExpansion.getGeysers().size()>0) 
				{
					BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Extractor,
							nextExpansion.getTilePosition(), false); /// 해처리 추가 확장 0622

					System.out.println("8");
				}
			}

		} 
		else 
		{

		}

	}

	/// 업그레이드 및 테크 리서치를 실행합니다
	void executeUpgradeAndTechResearch() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		// 1초에 한번만 실행
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}

		boolean isTimeToStartUpgradeType1 = false; /// 업그레이드할 타이밍인가
		boolean isTimeToStartUpgradeType2 = false; /// 업그레이드할 타이밍인가
		boolean isTimeToStartUpgradeType3 = false; /// 업그레이드할 타이밍인가
		boolean isTimeToStartUpgradeType4 = false; /// 업그레이드할 타이밍인가

		boolean isTimeToStartResearchTech1 = false; /// 리서치할 타이밍인가
		boolean isTimeToStartResearchTech2 = false; /// 리서치할 타이밍인가
		boolean isTimeToStartResearchTech3 = false; /// 리서치할 타이밍인가

		// 업그레이드 / 리서치할 타이밍인지 판단
		if (myRace == Race.Zerg) {
			// 업그레이드 / 리서치를 너무 성급하게 하다가 위험에 빠질 수 있으므로, 최소 저글링 6기 생산 후 업그레이드한다
			if (myPlayer.completedUnitCount(UnitType.Zerg_Spawning_Pool) > 0
					&& myPlayer.completedUnitCount(UnitType.Zerg_Zergling) >= 6) {
				isTimeToStartUpgradeType1 = true;
			}
			// 하이브 있으면 오버로드 이속 업
			if (myPlayer.completedUnitCount(UnitType.Zerg_Lair) > 0) {
				isTimeToStartUpgradeType2 = true;
			}
			// 가스 좀 남으면 하라고 넣음
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType4) == 3 && myPlayer.gas() > 100 && myPlayer.completedUnitCount(UnitType.Zerg_Mutalisk) >= 5){
				isTimeToStartUpgradeType3 = true;
			}
			if (myPlayer.completedUnitCount(UnitType.Zerg_Spire) > 0 && myPlayer.gas() > 100 && myPlayer.completedUnitCount(UnitType.Zerg_Mutalisk) >= 5){
				isTimeToStartUpgradeType4 = true;
			}
			// 러커는 최우선으로 리서치한다
			if (myPlayer.completedUnitCount(UnitType.Zerg_Hydralisk_Den) > 0
					&& myPlayer.completedUnitCount(UnitType.Zerg_Lair) > 0) {
				isTimeToStartResearchTech1 = true;
			}
			// 컨슘은 최우선으로 리서치한다
			if (myPlayer.completedUnitCount(UnitType.Zerg_Defiler_Mound) > 0) {
				isTimeToStartResearchTech2 = true;
			}
			// 업그레이드 / 리서치를 너무 성급하게 하다가 위험에 빠질 수 있으므로, 최소 컨슘 리서치 후 리서치한다
			if (myPlayer.completedUnitCount(UnitType.Zerg_Defiler_Mound) > 0
					&& myPlayer.hasResearched(necessaryTechType2) == true) {
				isTimeToStartResearchTech3 = true;
			}
			
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType3)==2 && myPlayer.completedUnitCount(UnitType.Zerg_Hive)==0)
			{
				isTimeToStartUpgradeType3 = false;
			}
			
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType4)==2 && myPlayer.completedUnitCount(UnitType.Zerg_Hive)==0)
			{
				isTimeToStartUpgradeType3 = false;
			}		
			
		}

		// 테크 리서치는 높은 우선순위로 우선적으로 실행
		if (isTimeToStartResearchTech1) {
			if (myPlayer.isResearching(necessaryTechType1) == false
					&& myPlayer.hasResearched(necessaryTechType1) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryTechType1) == 0) {
				BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryTechType1, true);
			}
		}

		if (isTimeToStartResearchTech2) {
			if (myPlayer.isResearching(necessaryTechType2) == false
					&& myPlayer.hasResearched(necessaryTechType2) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryTechType2) == 0) {
				BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryTechType2, true);
			}
		}

		if (isTimeToStartResearchTech3) {
			if (myPlayer.isResearching(necessaryTechType3) == false
					&& myPlayer.hasResearched(necessaryTechType3) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryTechType3) == 0) {
				BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryTechType3, true);
			}
		}

		// 업그레이드는 낮은 우선순위로 실행
		if (isTimeToStartUpgradeType1) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType1) == 0
					&& myPlayer.isUpgrading(necessaryUpgradeType1) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType1) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(necessaryUpgradeType1, false);
			}
		}

		if (isTimeToStartUpgradeType2) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType2) == 0
					&& myPlayer.isUpgrading(necessaryUpgradeType2) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType2) == 0) {
				BuildManager.Instance().buildQueue.queueAsLowestPriority(necessaryUpgradeType2, false);
			}
		}

		// <3 의 의미는 3레벨 업그레이드까지 계속 하라는 뜻이다. 그레이트 스파이어는 2단계 끝나고 지어주긴 해야함
		if (isTimeToStartUpgradeType3) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType3) < 2
					&& myPlayer.isUpgrading(necessaryUpgradeType3) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType3) == 0) {
				
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) 
				{
					if(unit.getType().equals(UnitType.Zerg_Spire) && unit.isUpgrading()==false && unit.isCompleted() && unit!=null)
					{
						BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryUpgradeType3, true, unit.getID());
						return;
					}
				}
				
			}
		}
		
		if (isTimeToStartUpgradeType3) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType3) == 2
					&& myPlayer.isUpgrading(necessaryUpgradeType3) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType3) == 0
					&& myPlayer.completedUnitCount(UnitType.Zerg_Hive)>0) {
				
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) 
				{
					if(unit.getType().equals(UnitType.Zerg_Spire) && unit.isUpgrading()==false && unit.isCompleted() && unit!=null)
					{
						BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryUpgradeType3, true, unit.getID());
						return;
					}
				}
				
			}
		}
		

		if (isTimeToStartUpgradeType4) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType4) < 2
					&& myPlayer.isUpgrading(necessaryUpgradeType4) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType4) == 0) {
				BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryUpgradeType4, true);
			}
		}
		
		if (isTimeToStartUpgradeType4) {
			if (myPlayer.getUpgradeLevel(necessaryUpgradeType4) == 2
					&& myPlayer.isUpgrading(necessaryUpgradeType4) == false
					&& BuildManager.Instance().buildQueue.getItemCount(necessaryUpgradeType4) == 0
					&& myPlayer.completedUnitCount(UnitType.Zerg_Hive)>0) {
				BuildManager.Instance().buildQueue.queueAsHighestPriority(necessaryUpgradeType4, true);
			}
		}
		
		
		
		
		
		
		
		

		// BWAPI 4.1.2 의 버그때문에, 오버로드 업그레이드를 위해서는 반드시 Zerg_Lair 가 있어야함
		// 이말인즉 해처리 잔뜩 + 하이브 1개 있으면 업그레이드 못하고 꼭 레어를 필요로 한다는 의미이다.
		if (myRace == Race.Zerg) {
			
			int numberOfLair = 0;
			numberOfLair += myPlayer.allUnitCount(UnitType.Zerg_Lair);
			numberOfLair += BuildManager.Instance().buildQueue.getItemCount(UnitType.Zerg_Lair);
			numberOfLair += ConstructionManager.Instance()
					.getConstructionQueueItemCount(UnitType.Zerg_Lair, null);
			
			
			
			
			if (BuildManager.Instance().buildQueue.getItemCount(UpgradeType.Pneumatized_Carapace) > 0) {
				if (numberOfLair == 0) 
				{
					BuildManager.Instance().buildQueue.queueAsHighestPriority(UnitType.Zerg_Lair, true);
				}
			}
		}
	}

	/// 공격유닛을 계속 추가 생산합니다
	public void executeCombatUnitTraining() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		// 1초에 4번만 실행
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}
		
		if (myPlayer.supplyUsed() <= 390) {

			for (Unit unit : myPlayer.getUnits()) {

				if (unit.getType() == UnitType.Zerg_Hatchery
						||unit.getType() == UnitType.Zerg_Lair
						||unit.getType() == UnitType.Zerg_Hive) {

					if (unit.isTraining() == false || unit.getLarva().size() > 0) {

						UnitType nextUnitTypeToTrain = getNextCombatUnitTypeToTrain();

						if (BuildManager.Instance().buildQueue.getItemCount(nextUnitTypeToTrain) < myPlayer.completedUnitCount(UnitType.Zerg_Hatchery)*2) {

							BuildManager.Instance().buildQueue.queueAsLowestPriority(nextUnitTypeToTrain, false);

							nextTargetIndexOfBuildOrderArray++;

							if (nextTargetIndexOfBuildOrderArray >= buildOrderArrayOfMyCombatUnitType.length) {
								nextTargetIndexOfBuildOrderArray = 0;
							}
						}
					}
				}
			}
		}
	}

	/// 다음에 생산할 공격유닛 UnitType 을 리턴합니다
	public UnitType getNextCombatUnitTypeToTrain() {

		UnitType nextUnitTypeToTrain = null;

		if (buildOrderArrayOfMyCombatUnitType[nextTargetIndexOfBuildOrderArray] == 1) {

			
			if(myPlayer.gas() > 350)
			{
				nextUnitTypeToTrain = myMutalisk;
			}
			else
			{
				nextUnitTypeToTrain = myZergling;
			}
			
				

		}

		else if (buildOrderArrayOfMyCombatUnitType[nextTargetIndexOfBuildOrderArray] == 2) {


				nextUnitTypeToTrain = myMutalisk;
			
			
		} else if (buildOrderArrayOfMyCombatUnitType[nextTargetIndexOfBuildOrderArray] == 3) {

			
			if(myPlayer.completedUnitCount(UnitType.Zerg_Ultralisk_Cavern)>0 ) {
				nextUnitTypeToTrain = myUltralisk;
			}
			else if (myZerglingList.size() < 2.0 * myMutaliskList.size()) 
			{
				nextUnitTypeToTrain = myZergling;
			} 
			else 
			{
				nextUnitTypeToTrain = myMutalisk;
			}
			
			
			
		} 
		else 
		{
			;
		}

		return nextUnitTypeToTrain;
	}

	// BasicBot 1.1 Patch Start ////////////////////////////////////////////////
	// 경기 결과 파일 Save / Load 및 로그파일 Save 예제 추가
	/// 과거 전체 게임 기록을 로딩합니다

	void loadGameRecordList() {

		// 과거의 게임에서 bwapi-data\write 폴더에 기록했던 파일은 대회 서버가 bwapi-data\read 폴더로 옮겨놓습니다
		// 따라서, 파일 로딩은 bwapi-data\read 폴더로부터 하시면 됩니다

		// TODO : 파일명은 각자 봇 명에 맞게 수정하시기 바랍니다

		String gameRecordFileName = "bwapi-data\\read\\NoNameBot_GameRecord.dat";

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(gameRecordFileName));

			System.out.println("loadGameRecord from file: " + gameRecordFileName);
			String currentLine;
			StringTokenizer st;
			GameRecord tempGameRecord;

			while ((currentLine = br.readLine()) != null) {

				st = new StringTokenizer(currentLine, " ");
				tempGameRecord = new GameRecord();
				if (st.hasMoreTokens()) {
					tempGameRecord.mapName = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.myName = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.myRace = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.myWinCount = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.myLoseCount = Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.enemyName = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.enemyRace = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.enemyRealRace = st.nextToken();
				}
				if (st.hasMoreTokens()) {
					tempGameRecord.gameFrameCount = Integer.parseInt(st.nextToken());
				}

				gameRecordList.add(tempGameRecord);
			}
		} catch (FileNotFoundException e) {
			System.out.println("loadGameRecord failed. Could not open file :" + gameRecordFileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/// 과거 전체 게임 기록 + 이번 게임 기록을 저장합니다
	void saveGameRecordList(boolean isWinner) {

		// 이번 게임의 파일 저장은 bwapi-data\write 폴더에 하시면 됩니다.
		// bwapi-data\write 폴더에 저장된 파일은 대회 서버가 다음 경기 때 bwapi-data\read 폴더로 옮겨놓습니다
		// TODO : 파일명은 각자 봇 명에 맞게 수정하시기 바랍니다

		String gameRecordFileName = "bwapi-data\\write\\NoNameBot_GameRecord.dat";

		System.out.println("saveGameRecord to file: " + gameRecordFileName);

		String mapName = MyBotModule.Broodwar.mapFileName();
		mapName = mapName.replace(' ', '_');
		String enemyName = MyBotModule.Broodwar.enemy().getName();
		enemyName = enemyName.replace(' ', '_');
		String myName = MyBotModule.Broodwar.self().getName();
		myName = myName.replace(' ', '_');

		/// 이번 게임에 대한 기록
		GameRecord thisGameRecord = new GameRecord();
		thisGameRecord.mapName = mapName;
		thisGameRecord.myName = myName;
		thisGameRecord.myRace = MyBotModule.Broodwar.self().getRace().toString();
		thisGameRecord.enemyName = enemyName;
		thisGameRecord.enemyRace = MyBotModule.Broodwar.enemy().getRace().toString();
		thisGameRecord.enemyRealRace = InformationManager.Instance().enemyRace.toString();
		thisGameRecord.gameFrameCount = MyBotModule.Broodwar.getFrameCount();

		if (isWinner) {
			thisGameRecord.myWinCount = 1;
			thisGameRecord.myLoseCount = 0;
		}

		else {
			thisGameRecord.myWinCount = 0;
			thisGameRecord.myLoseCount = 1;
		}

		// 이번 게임 기록을 전체 게임 기록에 추가

		gameRecordList.add(thisGameRecord);

		// 전체 게임 기록 write
		StringBuilder ss = new StringBuilder();

		for (GameRecord gameRecord : gameRecordList) {

			ss.append(gameRecord.mapName + " ");
			ss.append(gameRecord.myName + " ");
			ss.append(gameRecord.myRace + " ");
			ss.append(gameRecord.myWinCount + " ");
			ss.append(gameRecord.myLoseCount + " ");
			ss.append(gameRecord.enemyName + " ");
			ss.append(gameRecord.enemyRace + " ");
			ss.append(gameRecord.enemyRealRace + " ");
			ss.append(gameRecord.gameFrameCount + "\n");
		}

		Common.overwriteToFile(gameRecordFileName, ss.toString());
	}

	/// 이번 게임 중간에 상시적으로 로그를 저장합니다
	void saveGameLog() {

		// 100 프레임 (5초) 마다 1번씩 로그를 기록합니다
		// 참가팀 당 용량 제한이 있고, 타임아웃도 있기 때문에 자주 하지 않는 것이 좋습니다
		// 로그는 봇 개발 시 디버깅 용도로 사용하시는 것이 좋습니다

		if (MyBotModule.Broodwar.getFrameCount() % 100 != 0) {
			return;
		}

		// TODO : 파일명은 각자 봇 명에 맞게 수정하시기 바랍니다
		String gameLogFileName = "bwapi-data\\write\\NoNameBot_LastGameLog.dat";
		String mapName = MyBotModule.Broodwar.mapFileName();
		mapName = mapName.replace(' ', '_');
		String enemyName = MyBotModule.Broodwar.enemy().getName();
		enemyName = enemyName.replace(' ', '_');
		String myName = MyBotModule.Broodwar.self().getName();
		myName = myName.replace(' ', '_');
		StringBuilder ss = new StringBuilder();
		ss.append(mapName + " ");
		ss.append(myName + " ");
		ss.append(MyBotModule.Broodwar.self().getRace().toString() + " ");
		ss.append(enemyName + " ");
		ss.append(InformationManager.Instance().enemyRace.toString() + " ");
		ss.append(MyBotModule.Broodwar.getFrameCount() + " ");
		ss.append(MyBotModule.Broodwar.self().supplyUsed() + " ");
		ss.append(MyBotModule.Broodwar.self().supplyTotal() + "\n");

		Common.appendTextToFile(gameLogFileName, ss.toString());
	}

	// BasicBot 1.1 Patch End //////////////////////////////////////////////////

	private void drawStrategyManagerStatus() {

		int y = 250;

		// 아군 공격유닛 숫자 및 적군 공격유닛 숫자
		MyBotModule.Broodwar.drawTextScreen(200, y, "My " + myZergling.toString());
		MyBotModule.Broodwar.drawTextScreen(350, y, "alive " + myZerglingList.size());
		MyBotModule.Broodwar.drawTextScreen(400, y, "killed " + myKilledZerglings);
		y += 10;
		MyBotModule.Broodwar.drawTextScreen(200, y, "My " + myMutalisk.toString());
		MyBotModule.Broodwar.drawTextScreen(350, y, "alive " + myMutaliskList.size());
		MyBotModule.Broodwar.drawTextScreen(400, y, "killed " + myKilledMutalisks);
		y += 10;
		MyBotModule.Broodwar.drawTextScreen(200, y, "My " + myUltralisk.toString());
		MyBotModule.Broodwar.drawTextScreen(350, y, "alive " + myUltraliskList.size());
		MyBotModule.Broodwar.drawTextScreen(400, y, "killed " + myKilledUltralisks);
		y += 10;
		MyBotModule.Broodwar.drawTextScreen(200, y, "Enemy CombatUnit");
		MyBotModule.Broodwar.drawTextScreen(350, y, "alive " + numberOfCompletedEnemyCombatUnit);
		MyBotModule.Broodwar.drawTextScreen(400, y, "killed " + enemyKilledCombatUnitCount);
		y += 10;
		MyBotModule.Broodwar.drawTextScreen(200, y, "Enemy WorkerUnit");
		MyBotModule.Broodwar.drawTextScreen(350, y, "alive " + numberOfCompletedEnemyWorkerUnit);
		MyBotModule.Broodwar.drawTextScreen(400, y, "killed " + enemyKilledWorkerUnitCount);
		y += 20;

		// setInitialBuildOrder 에서 입력한 빌드오더가 다 끝나서 빌드오더큐가 empty 되었는지 여부
		MyBotModule.Broodwar.drawTextScreen(200, y, "isInitialBuildOrderFinished " + isInitialBuildOrderFinished);
		y += 10;
		// 전투 상황
		MyBotModule.Broodwar.drawTextScreen(200, y, "combatState " + combatState.ordinal());
	}

	private static StrategyManager instance = new StrategyManager();

	/// static singleton 객체를 리턴합니다
	public static StrategyManager Instance() {
		return instance;
	}

	private CommandUtil commandUtil = new CommandUtil();

	// BasicBot 1.1 Patch Start ////////////////////////////////////////////////
	// 경기 결과 파일 Save / Load 및 로그파일 Save 예제 추가를 위한 변수 및 메소드 선언
	/// 한 게임에 대한 기록을 저장하는 자료구조

	private class GameRecord {
		String mapName;
		String enemyName;
		String enemyRace;
		String enemyRealRace;
		String myName;
		String myRace;
		int gameFrameCount = 0;
		int myWinCount = 0;
		int myLoseCount = 0;
	}

	/// 과거 전체 게임들의 기록을 저장하는 자료구조
	ArrayList<GameRecord> gameRecordList = new ArrayList<GameRecord>();
	// BasicBot 1.1 Patch End //////////////////////////////////////////////////

	/// 경기가 종료될 때 일회적으로 전략 결과 정리 관련 로직을 실행합니다
	public void onEnd(boolean isWinner) {

		// BasicBot 1.1 Patch Start ////////////////////////////////////////////////
		// 경기 결과 파일 Save / Load 및 로그파일 Save 예제 추가
		// 과거 게임 기록 + 이번 게임 기록을 저장합니다
		//saveGameRecordList(isWinner);
		// BasicBot 1.1 Patch End //////////////////////////////////////////////////
	}

	/// 변수 값을 업데이트 합니다
	void updateVariables() {

		enemyRace = InformationManager.Instance().enemyRace;

		if (BuildManager.Instance().buildQueue.isEmpty()) {
			isInitialBuildOrderFinished = true;
		}

		// 적군의 공격유닛 숫자
		numberOfCompletedEnemyCombatUnit = 0;
		numberOfCompletedEnemyWorkerUnit = 0;

		for (Map.Entry<Integer, UnitInfo> unitInfoEntry : InformationManager.Instance()
				.getUnitAndUnitInfoMap(enemyPlayer).entrySet()) {

			UnitInfo enemyUnitInfo = unitInfoEntry.getValue();

			if (enemyUnitInfo.getType().isWorker() == false && enemyUnitInfo.getType().canAttack()
					&& enemyUnitInfo.getLastHealth() > 0) {

				numberOfCompletedEnemyCombatUnit++;
			}

			if (enemyUnitInfo.getType().isWorker() == true) {

				numberOfCompletedEnemyWorkerUnit++;
			}
		}

		// 아군 / 적군의 본진, 첫번째 길목, 두번째 길목

		myMainBaseLocation = InformationManager.Instance().getMainBaseLocation(myPlayer);
		myFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(myPlayer);
		myFirstChokePoint = InformationManager.Instance().getFirstChokePoint(myPlayer);
		mySecondChokePoint = InformationManager.Instance().getSecondChokePoint(myPlayer);
		enemyMainBaseLocation = InformationManager.Instance().getMainBaseLocation(enemyPlayer);
		enemyFirstExpansionLocation = InformationManager.Instance().getFirstExpansionLocation(enemyPlayer);
		enemyFirstChokePoint = InformationManager.Instance().getFirstChokePoint(enemyPlayer);
		enemySecondChokePoint = InformationManager.Instance().getSecondChokePoint(enemyPlayer);

		// 아군 방어 건물 목록, 공격 유닛 목록
		myCreepColonyList.clear();
		mySunkenColonyList.clear();

		myAllCombatUnitList.clear();
		myZerglingList.clear();
		myMutaliskList.clear();
		myUltraliskList.clear();

		for (Unit unit : myPlayer.getUnits()) {
			if (unit == null || unit.exists() == false || unit.getHitPoints() <= 0)
				continue;

			if (unit.getType() == myZergling) {
				myZerglingList.add(unit);
				myAllCombatUnitList.add(unit);
			} else if (unit.getType() == myMutalisk) {
				myMutaliskList.add(unit);
				myAllCombatUnitList.add(unit);
			} else if (unit.getType() == myUltralisk) {
				myUltraliskList.add(unit);
				myAllCombatUnitList.add(unit);
			} else if (unit.getType() == myCombatUnitType4) {
				myCombatUnitType4List.add(unit);
				myAllCombatUnitList.add(unit);
			}
			else if (unit.getType() == myCombatUnitType5) {
				myCombatUnitType5List.add(unit);
				myAllCombatUnitList.add(unit);
			} else if (unit.getType() == myCreepColony) {
				myCreepColonyList.add(unit);
			} else if (unit.getType() == mySunkenColony) {
				mySunkenColonyList.add(unit);
			}
		}
	}

	/// 권순우 0617 죽은 유닛을 세는 단순한 로직인데
	/// 시즈탱크가 탱크상태로 죽는것과 시즈상태로 죽은 것이 서로 다르게 분류되서
	/// 이런 것을 보정해줄 필요가 있다
	/// 저그에도 이런 것이 있으려나
	/// 아군 / 적군 공격 유닛 사망 유닛 숫자 누적값을 업데이트 합니다
	public void onUnitDestroy(Unit unit) {
		if (unit.getType().isNeutral()) {
			return;
		}

		if (unit.getPlayer() == myPlayer) {
			if (unit.getType() == myZergling) {
				myKilledZerglings++;
			} else if (unit.getType() == myMutalisk) {
				myKilledMutalisks++;
			} else if (unit.getType() == myUltralisk) {
				myKilledUltralisks++;
			} else if (myUltralisk == UnitType.Terran_Siege_Tank_Tank_Mode
					&& unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
				myKilledUltralisks++;
			}
		} else if (unit.getPlayer() == enemyPlayer) {

			/// 적군 공격 유닛타입의 사망 유닛 숫자 누적값
			if (unit.getType().isWorker() == false && unit.getType().isBuilding() == false) {
				enemyKilledCombatUnitCount++;
			}
			/// 적군 일꾼 유닛타입의 사망 유닛 숫자 누적값
			if (unit.getType().isWorker() == true) {
				enemyKilledWorkerUnitCount++;
			}
		}
	}

}