import json
Dict = {} 
f = open('../pingyin12710172/pingyinhanzibiao.txt','r')
f12 = open('../pingyin12710172/yierji.txt','r')
all12 = f12.read()
lines = f.readline()
lines = lines.strip('\n')
while lines:
    words = lines.split(' ')
    hanlist = []
    for word in words:
            if word in all12:
                    hanlist.append(word)
    if len(hanlist) > 0:
            Dict[words[0]] = hanlist
    lines = f.readline()
    lines = lines.strip('\n')
    
p = open('ping_han_dict.json','w')
json.dump(Dict, p)


