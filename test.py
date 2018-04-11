import json
p = open('judge12.json','r')
k = json.load(p)
for i in range(0,20):
    print(k[i])
#pp = open('pinghan.json','w')
#json.dump(k,pp)
