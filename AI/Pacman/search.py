# search.py
# ---------
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


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util
from game import Directions
from typing import List

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()




def tinyMazeSearch(problem: SearchProblem) -> List[Directions]:
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:", problem.getSuccessors(problem.getStartState()))
    """
    "*** YOUR CODE HERE ***"
    start=(problem.getStartState(),None,0,None)
    frontier=util.Stack()
    frontier.push(start)
    explored=set()
    while True:
        #print(frontier.list)
        if frontier.isEmpty():
            raise Exception("no solution")
        node=frontier.pop()
        if problem.isGoalState(node[0]):
            actions=[]
            while node!=start:
                actions.append(node[1])
                node=node[3]
            actions.reverse()
            return actions
        explored.add(node[0])
        for successor in problem.getSuccessors(node[0]):
            #in_frontier = lambda state: any((item[0] == state) if isinstance(item, (list, tuple)) else (item == state) for item in frontier.list)
            if successor[0] not in explored and not successor in frontier.list:
                successor=(successor[0], successor[1], successor[2], node)
                frontier.push(successor)
        
        

def breadthFirstSearch(problem: SearchProblem) -> List[Directions]:
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"
    start=(problem.getStartState(),None,0,None)
    frontier=util.Queue()
    frontier.push(start)
    explored=set()
    while True:
        if frontier.isEmpty():
            raise Exception("no solution")
        node=frontier.pop()
        if problem.isGoalState(node[0]):
            actions=[]
            while node!=start:
                actions.append(node[1])
                node=node[3]
            actions.reverse()
            return actions
        explored.add(node[0])
        for successor in problem.getSuccessors(node[0]):
            in_frontier = lambda state: any((item[0] == state) if isinstance(item, (list, tuple)) else (item == state) for item in frontier.list)
            if successor[0] not in explored and not in_frontier(successor[0]):
                successor=(successor[0], successor[1], successor[2], node)
                frontier.push(successor)

def uniformCostSearch(problem: SearchProblem) -> List[Directions]:
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"
    start=(problem.getStartState(),None,0,None)#location,action,cost,previous
    frontier=util.PriorityQueue()
    frontier.push(start,0)
    explored=set()
    while True:
        if frontier.isEmpty():
            raise Exception("no solution")
        node=frontier.pop()
        if problem.isGoalState(node[0]):
            actions=[]
            while node!=start:
                actions.append(node[1])
                node=node[3]
            actions.reverse()
            return actions
        if not node[0] in explored:
            explored.add(node[0])
        else:
            continue
        for successor in problem.getSuccessors(node[0]):
            #in_frontier = lambda state: any((item[0] == state) if isinstance(item, (list, tuple)) else (item == state) for item in frontier.heap)
            if successor[0] not in explored and not successor[0] in frontier.heap:
                successor=(successor[0], successor[1], successor[2]+node[2], node)
                action=[successor[1]]
                frontier.push(successor,successor[2])

def nullHeuristic(state, problem=None) -> float:
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic) -> List[Directions]:
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"
    start=(problem.getStartState(),None,0,None)#location,action,cost,previous
    frontier=util.PriorityQueue()
    frontier.push(start,0)
    explored=set()
    while True:
        if frontier.isEmpty():
            raise Exception("no solution")
        node=frontier.pop()
        if problem.isGoalState(node[0]):
            actions=[]
            while node!=start:
                actions.append(node[1])
                node=node[3]
            actions.reverse()
            return actions
        if not node[0] in explored:
            explored.add(node[0])
        else:
            continue
        for successor in problem.getSuccessors(node[0]):
            #in_frontier = lambda state: any((item[0] == state) if isinstance(item, (list, tuple)) else (item == state) for item in frontier.heap)
            if successor[0] not in explored and not successor[0] in frontier.heap:
                successor=(successor[0], successor[1], successor[2]+node[2], node)
                action=[successor[1]]
                frontier.push(successor,successor[2]+1.0)

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
