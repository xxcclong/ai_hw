import json
p = open('ping_han_dict.json','r')
k = json.load(p)
print(k['qing'])
print(k['hua'])
print(k['ai'][1][0])
#pp = open('pinghan.json','w')
#json.dump(k,pp)
