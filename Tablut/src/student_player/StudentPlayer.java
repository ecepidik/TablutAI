package student_player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

public class StudentPlayer2 extends TablutPlayer{

	private Random rand = new Random(1848);

	int OPPONENT_ID;
	int OPPONENT_INITIAL_PIECE_COUNT;

	int PLAYER_ID;
	int PLAYER_INITIAL_PIECE_COUNT;

	int MAX_DEPTH = 4;
	
	static long TIME_OUT_SUBTREE = 1000000*900;

	public StudentPlayer2() {
		super("260620324");
	}
	
	public static double getSomething() {
		return Math.random();
	}

	@Override
	public Move chooseMove(TablutBoardState bs) {
		
		List<TablutMove> options = bs.getAllLegalMoves();

		// Set an initial move as some random one.
		TablutMove bestMove = options.get(rand.nextInt(options.size()));
		Double bestMoveValue = Double.NEGATIVE_INFINITY;

		for (TablutMove move : options) {
			System.out.println("HERE1");
			Double moveValue = calculateFirstMoveValue(move, bs);
			if(bestMoveValue <= moveValue) {
				bestMoveValue = Math.max(moveValue, bestMoveValue);
				bestMove = move;
			}
			
			if(bestMoveValue == Double.POSITIVE_INFINITY) {
				break;
			}
		}

		return bestMove;
	}

	private Double calculateFirstMoveValue(TablutMove move, TablutBoardState bs) {
		long subTreeStartTime = System.nanoTime();
		OPPONENT_ID = bs.getOpponent();
		PLAYER_ID = player_id;

		OPPONENT_INITIAL_PIECE_COUNT = bs.getNumberPlayerPieces(OPPONENT_ID);	
		PLAYER_INITIAL_PIECE_COUNT = bs.getNumberPlayerPieces(PLAYER_ID);;

		System.out.println("HERE");

		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
		cloneBS.processMove(move);
		int newNumberOfOpponentPieces = cloneBS.getNumberPlayerPieces(OPPONENT_ID);

		if (cloneBS.getWinner() == PLAYER_ID) {
			return Double.POSITIVE_INFINITY;
		}
		else if (newNumberOfOpponentPieces < OPPONENT_INITIAL_PIECE_COUNT) {
			return 10.0;
		}
		else {
			double maxMoveValue = Double.NEGATIVE_INFINITY;
			return Math.max(maxMoveValue, minimax(cloneBS, MAX_DEPTH-1, OPPONENT_ID, subTreeStartTime));	
		}
	}

	public double minimax(TablutBoardState boardState, int depth, int id, long subTreeStartTime) {
		if (depth == 0 || boardState.gameOver() || timeOutForSubTree(System.nanoTime() - subTreeStartTime)) {
			return calculateHeuristic(boardState, id);
		}

		//my agent is always the maximizing player
		if (id == PLAYER_ID) {
			double bestValue = Double.NEGATIVE_INFINITY;

			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
				nextBoard.processMove(legalMove);
				double value = minimax(nextBoard, depth-1, OPPONENT_ID, subTreeStartTime);	
				bestValue = Math.max(value, bestValue);
			}
			return bestValue;

		} else {
			double bestValue = Double.POSITIVE_INFINITY;

			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
				nextBoard.processMove(legalMove);
				double value = minimax(nextBoard, depth-1, PLAYER_ID, subTreeStartTime);	
				bestValue = Math.min(value, bestValue);
			}
			return bestValue;
		}
	}

	private boolean timeOutForSubTree(long timeElapsedSubtree) {
		if(timeElapsedSubtree >= TIME_OUT_SUBTREE)
			return true;
		return false;
	}

	public double calculateHeuristic(TablutBoardState boardState, int id) {

		int kingDistance = Coordinates.distanceToClosestCorner(boardState.getKingPosition());

		int swedesCount = boardState.getNumberPlayerPieces(TablutBoardState.SWEDE);
		int muscovitesCount = boardState.getNumberPlayerPieces(TablutBoardState.MUSCOVITE);

		double heuristic = 0;

		if (boardState.getWinner() == PLAYER_ID) {
			heuristic = Double.POSITIVE_INFINITY;
		} else if (boardState.getWinner() == OPPONENT_ID) {
			heuristic = Double.NEGATIVE_INFINITY;
		} else {
			if (id == PLAYER_ID) {
				//when it's my turn, it will try to maximize the score
				if(PLAYER_ID == TablutBoardState.SWEDE) { //more swedes & close king -> high positive more score
					heuristic += PLAYER_INITIAL_PIECE_COUNT - swedesCount;
					heuristic -= OPPONENT_INITIAL_PIECE_COUNT - muscovitesCount;
					heuristic += 0.5*(8-kingDistance);
				} else if(PLAYER_ID == TablutBoardState.MUSCOVITE) { //more muscovites & further king -> high positive score
					heuristic += PLAYER_INITIAL_PIECE_COUNT - muscovitesCount;
					heuristic -= OPPONENT_INITIAL_PIECE_COUNT - swedesCount;
					heuristic += 0.1*(kingDistance);
				}
			} else if (id == OPPONENT_ID){
				//when it's opponents turn, it will try to minimize the score
				if(OPPONENT_ID == TablutBoardState.SWEDE) { //more swedes & close king -> high negative score
					heuristic += PLAYER_INITIAL_PIECE_COUNT - muscovitesCount;
					heuristic -= OPPONENT_INITIAL_PIECE_COUNT - swedesCount;
					heuristic -= 0.5*(8-kingDistance);
				} else if(OPPONENT_ID == TablutBoardState.MUSCOVITE) { //more muscovites & far king -> high negative score
					heuristic += PLAYER_INITIAL_PIECE_COUNT - swedesCount;
					heuristic -= OPPONENT_INITIAL_PIECE_COUNT - muscovitesCount;
					heuristic -= 0.1*(kingDistance);
				}
			}
		}
		return heuristic;
	}
}
