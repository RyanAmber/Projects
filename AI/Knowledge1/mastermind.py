from logic import *
import random
colors=["Red","Green","Blue","Yellow","White","Black"]
ans=[]
for i in range(4):
    ans.append(colors[random.randint(0,5)])
knowledge=And()
