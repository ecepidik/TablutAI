//package student_player;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import boardgame.Move;
//import coordinates.Coord;
//import coordinates.Coordinates;
//import tablut.TablutBoardState;
//import tablut.TablutMove;
//import tablut.TablutPlayer;
//
///** A player file submitted by a student. */
//public class StudentPlayer extends TablutPlayer {
//
//	private static int MAX_DEPTH = 4;
//	private int INITIAL_SWEDE_COUNT;
//	private int INITIAL_MUSCOVITE_COUNT;
//	
//	static long TIME_OUT = 1000000*1200;
//
//	
//	private Move BEST_MOVE;
//	/**
//	 * You must modify this constructor to return your student number. This is
//	 * important, because this is what the code that runs the competition uses to
//	 * associate you with your agent. The constructor should do nothing else.
//	 */
//	public StudentPlayer() {
//		super("260620324");
//	}
//
//	/**
//	 * This is the primary method that you need to implement. The ``boardState``
//	 * object contains the current state of the game, which your agent must use to
//	 * make decisions.
//	 */
//	
//	public static boolean timeOut(long elapsedTimePerSubTree) {
//	    	return elapsedTimePerSubTree >= TIME_OUT;
//	}
//	
//	public float minimax(TablutBoardState boardState, int depth, boolean maximizingPlayer) {
//		if (depth == 0 || boardState.gameOver()) {
//			return (float) calculateHeuristic(boardState);
//		}
//		
//		if (maximizingPlayer) {
//			float bestValue = Float.NEGATIVE_INFINITY;
//		
//			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				float value = minimax(nextBoard, depth-1, false);	
//				bestValue = Math.max(value, bestValue);
//			}
//			return bestValue;
//			
//		} else {
//			float bestValue = Float.POSITIVE_INFINITY;
//			
//			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				float value = minimax(nextBoard, depth-1, true);	
//				bestValue = Math.min(value, bestValue);
//			}
//			return bestValue;
//		}
//	}
//
//	public double alphabeta(TablutBoardState boardState, int depth, double alpha, double beta, boolean maximizingPlayer, long elapsedTimePerSubTree) {
//		if (depth == 0 || boardState.gameOver() || timeOut(elapsedTimePerSubTree)) {
//			return calculateHeuristic(boardState);
//		}
//
//		List<TablutMove> legalMoves = boardState.getAllLegalMoves();	
//		if (legalMoves.size() == 0) {
//			return calculateHeuristic(boardState);
//		}
//		
//		if (maximizingPlayer) {
//			double bestValue = Double.NEGATIVE_INFINITY;
//			
//			for(TablutMove legalMove: legalMoves) {		
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				
//				
//				bestValue = Math.max(bestValue, alphabeta(nextBoard, depth-1, alpha, beta, false, System.nanoTime() - elapsedTimePerSubTree));
//				alpha = Math.max(alpha, bestValue);	
//				
//				if(beta <= alpha) {
//					break;
//				}
//			}
//			return alpha;
//
//		} else {
//			double bestValue = Double.POSITIVE_INFINITY;
//			
//			for(TablutMove legalMove: legalMoves) {	
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				//int newNumberOfOpponentPieces = boardState.getNumberPlayerPieces(boardState.getOpponent());
//
//				bestValue = Math.min(bestValue, alphabeta(nextBoard, depth-1, alpha, beta, true, System.nanoTime() - elapsedTimePerSubTree));
//				beta = Math.min(beta, bestValue);
//
//				if(beta <= alpha) {
//					break;
//				}	
//			}
//			return beta;
//		}
//	}
//	
//	@Override
//	public Move chooseMove(TablutBoardState boardState) {
//		
//		double maxAlpha = Double.NEGATIVE_INFINITY;
//		double minBeta = Double.POSITIVE_INFINITY;
//
//		double alpha = Double.NEGATIVE_INFINITY;
//		double beta = Double.POSITIVE_INFINITY;
//
//		int depth = MAX_DEPTH;
//
//		List<TablutMove> legalMoves = boardState.getAllLegalMoves();
//		INITIAL_SWEDE_COUNT = boardState.getNumberPlayerPieces(TablutBoardState.SWEDE);
//		INITIAL_MUSCOVITE_COUNT = boardState.getNumberPlayerPieces(TablutBoardState.MUSCOVITE);
//
//		if (player_id == TablutBoardState.MUSCOVITE) {
//			for(TablutMove legalMove: legalMoves) {
//				long treestartTime = System.nanoTime();
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				alpha = Math.max(alpha, alphabeta(nextBoard, depth-1, alpha, beta, false, System.nanoTime() - treestartTime));	
//				
//				if(maxAlpha <= alpha) {
//					maxAlpha = alpha;
//					BEST_MOVE = legalMove;			
//				}
//			}
//		}
//
//		if (player_id == TablutBoardState.SWEDE) {
//			Coord kingPos = boardState.getKingPosition();
//
//			for (TablutMove kingLegalMove : boardState.getLegalMovesForPosition(kingPos)) {
//				if (Coordinates.distanceToClosestCorner(kingLegalMove.getEndPosition()) == 0) {
//					BEST_MOVE = kingLegalMove;
//					return BEST_MOVE;
//				}
//			}
//			for(TablutMove legalMove: legalMoves) {
//				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
//				nextBoard.processMove(legalMove);
//				long treestartTime = System.nanoTime();
//				beta = Math.min(beta, alphabeta(nextBoard, depth-1, alpha, beta, true, System.nanoTime() - treestartTime));	
//				if(minBeta >= beta) {
//					minBeta = beta;
//					BEST_MOVE = legalMove;			
//				}
//			}	
//		}
//		return BEST_MOVE;
//	}
//
//	public double calculateHeuristic(TablutBoardState boardState) {
//
//		int kingDistance = Coordinates.distanceToClosestCorner(boardState.getKingPosition());
//
//		int swedesCount = boardState.getNumberPlayerPieces(TablutBoardState.SWEDE);
//		int muscovitesCount = boardState.getNumberPlayerPieces(TablutBoardState.MUSCOVITE);
//
//		double heuristic = 0;
//		
//		if (boardState.getTurnPlayer() == TablutBoardState.MUSCOVITE) {
//			
//			if (boardState.getWinner() == TablutBoardState.MUSCOVITE) {
//				heuristic = Double.POSITIVE_INFINITY;
//			} else if (boardState.getWinner() == TablutBoardState.SWEDE) {
//				heuristic = Double.NEGATIVE_INFINITY;
//			} else {
//				heuristic += INITIAL_SWEDE_COUNT - swedesCount;
//				heuristic -= INITIAL_MUSCOVITE_COUNT - muscovitesCount;
//				heuristic += 0.1*(kingDistance);
//			}
//		} else {
//			if (boardState.getWinner() == TablutBoardState.SWEDE) {
//				heuristic = Double.NEGATIVE_INFINITY;
//			} else if (boardState.getWinner() == TablutBoardState.MUSCOVITE) {
//				heuristic = Double.POSITIVE_INFINITY;			
//			} else {
//				heuristic -= INITIAL_SWEDE_COUNT - swedesCount;
//				heuristic += INITIAL_MUSCOVITE_COUNT - muscovitesCount;
//				heuristic -= 0.5*(8-kingDistance);
//			}
//		}
//		return heuristic;
//	}
//}

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

public class StudentPlayer extends TablutPlayer{

	private Random rand = new Random(1848);

	int OPPONENT_ID;
	int OPPONENT_INITIAL_PIECE_COUNT;

	int PLAYER_ID;
	int PLAYER_INITIAL_PIECE_COUNT;

	int MAX_DEPTH = 4;
	
	static long TIME_OUT_SUBTREE = 1000000*500;
	long SUBTREE_START_TIME;

	public StudentPlayer() {
		super("260620324");
	}
	
	public static double getSomething() {
		return Math.random();
	}

	@Override
	public Move chooseMove(TablutBoardState bs) {
		List<TablutMove> options = bs.getAllLegalMoves();

		Map<TablutMove, Double> moves = new HashMap<TablutMove, Double>();

		// Set an initial move as some random one.
		TablutMove bestMove = options.get(rand.nextInt(options.size()));
		Double bestMoveValue = Double.NEGATIVE_INFINITY;

		for (TablutMove move : options) {
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
		OPPONENT_ID = bs.getOpponent();
		PLAYER_ID = player_id;

		OPPONENT_INITIAL_PIECE_COUNT = bs.getNumberPlayerPieces(OPPONENT_ID);	
		PLAYER_INITIAL_PIECE_COUNT = bs.getNumberPlayerPieces(PLAYER_ID);


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
			return Math.max(maxMoveValue, minimax(cloneBS, MAX_DEPTH-1, OPPONENT_ID));	
		}
	}

	public double minimax(TablutBoardState boardState, int depth, int id) {
		if (depth == 0 || boardState.gameOver() || timeOutForSubTree()) {
			return calculateHeuristic(boardState, id);
		}

		//my agent is always the maximizing player
		if (id == PLAYER_ID) {
			double bestValue = Double.NEGATIVE_INFINITY;

			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
				nextBoard.processMove(legalMove);
				double value = minimax(nextBoard, depth-1, OPPONENT_ID);	
				bestValue = Math.max(value, bestValue);
			}
			return bestValue;

		} else {
			double bestValue = Double.POSITIVE_INFINITY;
			for(TablutMove legalMove: boardState.getAllLegalMoves()) {	
				TablutBoardState nextBoard = (TablutBoardState) boardState.clone();
				nextBoard.processMove(legalMove);
				double value = minimax(nextBoard, depth-1, PLAYER_ID);	
				bestValue = Math.min(value, bestValue);
			}
			return bestValue;
		}
	}

	private boolean timeOutForSubTree() {
		if(System.nanoTime() - SUBTREE_START_TIME >= TIME_OUT_SUBTREE)
			return true;
		return false;
	}

	public double calculateHeuristic(TablutBoardState boardState, int id) {

		System.out.println("CALCULATE");
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
