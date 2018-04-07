import json
p = open('ping_han_dict.json','r')
k = json.load(p)
print(k['suan'])
#pp = open('pinghan.json','w')
#json.dump(k,pp)
