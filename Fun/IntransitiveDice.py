import random as r
def getMoves(a,b,c,d,ai1,ai2):
    first=r.random()>0.5
    move1=0
    move2=0
    moves=[]
    if first:
        part=[0,0,0,0]
        for i in range(len(ai1.keys())):
            if i==0:
                part[i]=sum(ai1.get(ai1.keys()[i]))
            else:
                part[i]=part[i-1]+sum(ai1.keys()[i])
        num=r.randint(0,part[len(ai1.keys())-1])
        move1=0
        while(num<part[move1]):
            move1+=1
            if(move1>=len(ai1.keys())):
                break
        move1-=1
    else:
        best=float('-inf')
        for val in ai2.keys():
            if sum(ai2.get(val))>=best:
                if sum(ai2.get(val))>best:
                    moves=[]
                moves.append(val)
                best=sum(ai2.get(val))
        move2=r.choice(moves)
    if first:
        scores=ai2.get(move1)
        best=float('-inf')
        moves=[]
        for score in range(len(scores)):
            if scores[score]>=best:
                if scores[score]>best:
                    moves=[]
                best=scores[score]
                moves.append(score)
        move2=r.choice(moves)
    else:
        scores=ai1.get(move2)
        best=float('-inf')
        moves=[]
        for score in range(len(scores)):
            if scores[score]>=best:
                if scores[score]>best:
                    moves=[]
                best=scores[score]
                moves.append(score)
        move1=r.choice(moves)
    return str(move1)+str(move2)
def play(a,b,c,d,move1,move2,ai1,ai2):
    val1=0
    val2=0
    if move1==0:
        val1=r.choice(a)
    elif move1==1:
        val1=r.choice(b)
    elif move1==2:
        val1=r.choice(c)
    else:
        val1=r.choice(d)
    if move2==0:
        val2=r.choice(a)
    elif move2==1:
        val2=r.choice(b)
    elif move2==2:
        val2=r.choice(c)
    else:
        val2=r.choice(d)
    move1=int(move1)
    move2=int(move2)
    if val1>val2:
        print("Player 1 wins")
        ai1.get(move2)[move1]=ai1.get(move2)[move1]+1
    else:
        print("Player 2 wins")
        ai2.get(move1)[move2]=ai2.get(move1)[move2]+1
a=[4,4,4,4,0,0]
b=[3,3,3,3,3,3]
c=[6,6,2,2,2,2]
d=[5,5,5,1,1,1]
ai1={0:[0,0,0,0],1:[0,0,0,0],2:[0,0,0,0],3:[0,0,0,0]}
ai2={0:[0,0,0,0],1:[0,0,0,0],2:[0,0,0,0],3:[0,0,0,0]}
games=int(input())
for i in range(games):
    moves=getMoves(a,b,c,d,ai1,ai2)
    move1=moves[0]
    move2=moves[1]
    play(a,b,c,d,move1,move2,ai1,ai2)
    print(ai1)
    print(ai2)