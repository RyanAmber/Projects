# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util
import math

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        score=successorGameState.getScore()*6
        if action=='Stop':
            score-=20
            #print('No stop')
            #util.pause()
        for action2 in successorGameState.getLegalActions():
            score+=5
            finalState=successorGameState.generatePacmanSuccessor(action2)
            for ghost in newGhostStates:
                if finalState.getPacmanPosition()==ghost.getPosition():
                    score-=1000
                if abs(finalState.getPacmanPosition()[0]-ghost.getPosition()[0])<3 and abs(finalState.getPacmanPosition()[1]-ghost.getPosition()[1])<3:
                    score-=10
        dist=10000
        for x in range(newFood.width-1):
            for y in range(newFood.height-1):
                if(newFood[x][y]):
                    dist=min(dist,abs(newPos[0]-x)+abs(newPos[1]-y))
        for time in newScaredTimes:
            score+=time/5
        if newFood.count()>0:
            score-=dist*2
        return score

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)
        self.count=0
        self.dist=0

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        best=-math.inf
        bestmove='Stop'
        self.count=0
        for action in gameState.getLegalActions(0):
            index=self.index
            successor=gameState.generateSuccessor(0,action)
            self.count+=1
            if index+1<gameState.getNumAgents():
                index+=1
            score=self.minimax(successor,index,self.depth)
            #print(score)
            if score>best:
                bestmove=action
                best=score
        #print(self.count+1)
        #util.pause()
        return bestmove

    def minimax(self, gameState:GameState, index,depth):
        if gameState.isWin() or gameState.isLose() or depth==0:
            return self.evaluationFunction(gameState)
        if index==0:
            best=-math.inf
            if index<gameState.getNumAgents():
                for action in gameState.getLegalActions(index):
                    successor=gameState.generateSuccessor(index,action)
                    self.count+=1
                    best=max(best,self.minimax(successor,index+1,depth))
                return best
            else:
                for action in gameState.getLegalActions(0):
                    successor=gameState.generateSuccessor(0,action)
                    self.count+=1
                    best=max(best,self.minimax(successor,1,depth-1))
                return best
        else:
            best=math.inf
            if index>=gameState.getNumAgents():
                for action in gameState.getLegalActions(0):
                    successor=gameState.generateSuccessor(0,action)
                    self.count+=1
                    best=min(best,self.minimax(successor,1,depth-1))
                return best
            else:
                for action in gameState.getLegalActions(index):
                    successor=gameState.generateSuccessor(index,action)
                    self.count+=1
                    if index+1>=successor.getNumAgents():
                        best=min(best,self.minimax(successor,0,depth-1))
                    else:
                        best=min(best,self.minimax(successor,index+1,depth))
                return best


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        util.raiseNotDefined()

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        
        "*** YOUR CODE HERE ***"
        best=-math.inf
        bestmove='Stop'
        self.count=0
        for action in gameState.getLegalActions(0):
            index=self.index
            successor=gameState.generateSuccessor(0,action)
            self.count+=1
            if index+1<gameState.getNumAgents():
                index+=1
            score=self.minimax(successor,action,index,self.depth)
            if score>best:
                bestmove=action
                best=score
        #print("Score: "+str(round(best/5.0)))
        return bestmove
    
    def minimax(self, gameState:GameState, action,index,depth):
        if gameState.isWin() or gameState.isLose() or depth==0:
            return self.betterEvaluationFunction(gameState,action)
        if index==0:
            best=-math.inf
            if index<gameState.getNumAgents():
                for action2 in gameState.getLegalActions(index):
                    successor=gameState.generateSuccessor(index,action2)
                    self.count+=1
                    best=max(best,self.minimax(successor,action,index+1,depth))
                return best
            else:
                for action2 in gameState.getLegalActions(0):
                    successor=gameState.generateSuccessor(0,action2)
                    self.count+=1
                    best=max(best,self.minimax(successor,action,1,depth-1))
                return best
        else:
            best=0
            if index>=gameState.getNumAgents():
                for action2 in gameState.getLegalActions(0):
                    successor=gameState.generateSuccessor(0,action2)
                    self.count+=1
                    best+=self.minimax(successor,action,1,depth-1)
                return best/len(gameState.getLegalActions(index))
            else:
                for action2 in gameState.getLegalActions(index):
                    successor=gameState.generateSuccessor(index,action2)
                    self.count+=1
                    if index+1>=successor.getNumAgents():
                        best+=self.minimax(successor,action,0,depth-1)
                    else:
                        best+=self.minimax(successor,action,index+1,depth)
                return best/len(gameState.getLegalActions(index))

    def betterEvaluationFunction(self, currentGameState: GameState,action):
        """
        Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
        evaluation function (question 5).

        DESCRIPTION: <write something here so we know what you did>
        """
        "*** YOUR CODE HERE ***"
        #successorGameState = currentGameState.generatePacmanSuccessor(action)
        successorGameState=currentGameState
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        score=successorGameState.getScore()*5
        if action=='Stop':
            score-=1
            #print('No stop')
            #util.pause()
        for action2 in successorGameState.getLegalActions():
            score+=5
            finalState=successorGameState.generatePacmanSuccessor(action2)
            for ghost in newGhostStates:
                if finalState.getPacmanPosition()==ghost.getPosition():
                    score-=1000
        for ghost in newGhostStates:
            if abs(successorGameState.getPacmanPosition()[0]-ghost.getPosition()[0])<3 and abs(successorGameState.getPacmanPosition()[1]-ghost.getPosition()[1])<3:
                score-=10
            else:
                score+=(abs(successorGameState.getPacmanPosition()[0]-ghost.getPosition()[0])+abs(successorGameState.getPacmanPosition()[1]-ghost.getPosition()[1]))/2
        dist=10000
        for x in range(newFood.width-1):
            for y in range(newFood.height-1):
                if(newFood[x][y]):
                    dx=newPos[0]-x
                    dy=newPos[1]-y
                    if dx<3 and dy<3:
                        score+=2
                    dist=min(dist,abs(dx)+abs(dy))
        for time in newScaredTimes:
            score+=time/5
        if newFood.count()>0:
            #print(dist)
            #util.pause()
            score-=dist*3
        #print(score)
        return score

# Abbreviation
    better = betterEvaluationFunction
