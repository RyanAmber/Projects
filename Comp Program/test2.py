tests=int(input())
for test in range(tests):
    n,m=input().split(" ")
    fav=input()
    same=input()
    others=tuple(dict() for _ in range(int(m)))
    words=[]
    for i in range(int(n)-1):
        s=input()
        for j in range(int(m)):
            if s[j] in others[j]:
                others[j][s[j]].append(i)
            else:
                others[j][s[j]]=[i]
        words.append(s)
    #print(others)
    sol=list()
    total=0
    for i in range(int(m)):
        if fav[i]==same[i]:
            continue
        for j in range(int(m)):
            if fav[i]==same[j] and fav[j]!=same[j]:
                total+=1
                temp=same[i]
                same=same[:i]+same[j]+same[i+1:]
                same=same[:j]+temp+same[j+1:]
                string="1 1 "+str(i)+" "+str(j)
                sol.append(string)
                #print("Close swap inside")
    for i in range(int(m)):
        if fav[i]==same[i]:
            continue
        if fav[i] in others[i]:
            total+=1
            val=others[i][fav[i]][0]
            val=str(val+2)
            string="2 1 "+val+" "+str(i+1)
            sol.append(string)
            #print("Swap in")
        else:
            for j in range(int(m)):
                if j==1:
                    continue
    for i in range(int(m)):
        if fav[i]==same[i]:
            continue
        for j in range(len(others)):
            dictionary=others[j]
            if fav[i] in dictionary:
                total+=2
                sol.append("1 "+str(j)+" "+str(i+1)+" "+str(words[dictionary[fav[i]][0]].index(fav[i])+1))
                #print("Far swap inside")
                sol.append("2 1 "+str(j+2)+" "+str(i+1))
                #print("Swap in")
    print(total)
    for string in sol:
        print(string)