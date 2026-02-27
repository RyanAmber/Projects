tests=int(input())
for test in range(tests):
    n,m=input().split(" ")
    fav=input()
    same=input()
    words=[]
    for i in range(int(n)-1):
        s=input()
        words.append(s)
    total=0
    sol=[]
    for i in range(int(m)):
        done=False
        if fav[i]==same[i]:
            continue
        if fav[i] in same:
            go=False
            index=0
            for ind in range(i,int(m)):
                if fav[i]==same[ind]:
                    go=True
                    index=ind
            if fav[index]!=same[index] and go:
                same=same[:i]+same[index]+same[i+1:index]+same[i]+same[index+1:]
                #print(same)
                total+=1
                string="1 1 "+str(i+1)+" "+str(index+1)
                sol.append(string)
                done=True
        if not done:
            for j in range(len(words)):
                if done:
                    continue
                word=words[j]
                if fav[i] in word:
                    index=word.index(fav[i])
                    if index==i:
                        total+=1
                        string="2 1 "+str(j+2)+" "+str(i+1)
                        words[j]=word[:i]+same[i]+word[i+1:]
                        same=same[:i]+fav[i]+same[i+1:]
                        sol.append(string)
                        done=True
                    else:
                        total+=2
                        done=True
                        swap=False
                        if index<i:
                            temp=i
                            i=index
                            index=temp
                            swap=True
                        string="1 "+str(j+2)+" "+str(index+1)+" "+str(i+1)
                        words[j]=word[:i]+word[index]+word[i+1:index]+word[i]+word[index+1:]
                        word=words[j]
                        sol.append(string)
                        if not swap:
                            words[j]=word[:i]+same[i]+word[i+1:]
                            same=same[:i]+fav[i]+same[i+1:]
                            string="2 1 "+str(j+2)+" "+str(i+1)
                        else:
                            words[j]=word[:index]+same[index]+word[index+1:]
                            same=same[:index]+fav[index]+same[index+1:]
                            string="2 1 "+str(j+2)+" "+str(index+1)
                        sol.append(string)
        #print(fav)
        #print("Matcher:"+same)
        #print("Words:"words)


        
    print(total)
    for string in sol:
        print(string)