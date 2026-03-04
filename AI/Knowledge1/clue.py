from logic import *
import random
mustard=Symbol("ColMustard")
plum=Symbol("ProfPlum")
scarlet=Symbol("MsScarlet")
characters=[mustard,plum,scarlet]

ballroom=Symbol("ballroom")
kitchen=Symbol("kitchen")
library=Symbol("library")
rooms=[ballroom,kitchen,library]

knife=Symbol("knife")
revolver=Symbol("revolver")
wrench=Symbol("wrench")
weapons=[knife,revolver,wrench]

symbols=characters+rooms+weapons
def check_knowledge(knowledge):
    done=True
    for symbol in symbols:
        if model_check(knowledge,symbol):
            print(str(symbol)+": YES")
        elif not model_check(knowledge,Not(symbol)):
            print(str(symbol)+": MAYBE")
            done=False
    return done
knowledge=And(Or(mustard,plum,scarlet),
              Or(ballroom,kitchen,library),
              Or(knife,revolver,wrench))
def get_Card(knowledge):
    poss=[]
    for symbol in symbols:
        if not model_check(knowledge,symbol) and not model_check(knowledge,Not(symbol)):
            poss.append(symbol)
    if len(poss)>0:
        card=random.choice(poss)
        print("Card Gotten:"+str(card))
        knowledge.add(Not(card))

def Guess(knowledge):
    poss=[]
    for char in characters:
        if not model_check(knowledge,char) and not model_check(knowledge,Not(char)):
            poss.append(char)
    if len(poss)>0:
        char=random.choice(poss)
    poss=[]
    for room in rooms:
        if not model_check(knowledge,room) and not model_check(knowledge,Not(room)):
            poss.append(room)
    if len(poss)>0:
        room=random.choice(poss)
    poss=[]
    for item in weapons:
        if not model_check(knowledge,item) and not model_check(knowledge,Not(item)):
            poss.append(item)
    if len(poss)>0:
        item=random.choice(poss)
    print("Failed Guess: "+str(char)+", "+str(room)+", "+str(item))
    knowledge.add(Or(Not(char),Not(room),Not(item)))
done=False
while not done:
    val=random.randint(0,3)
    if val==0:
        Guess(knowledge)
    else:
        get_Card(knowledge)
    done=check_knowledge(knowledge)
    print()
