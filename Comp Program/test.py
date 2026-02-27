def update(found,val,mod):
    if found[val]==1:
        return 0
    extra=found[val]-1
    found[val]=1
    if val+mod in found:
        found[val+mod]+=extra
    else:
        found[val+mod]=extra
    return extra+update(found,val+mod,mod)
tests=int(input())
#print()
for i in range(tests):
    n,k=input().split(" ")
    found=dict()
    vals=input().split(" ")
    for j in range(int(n)):
        val=int(vals[j])
        if val in found:
            found[val]+=1
        else:
            found[val]=1
    total=0
    temp=found.copy()
    for val in temp:
        if found[val]==1:
            continue
        total+=update(found,val,int(k))
    print(total)
    #print()
    #print()
    