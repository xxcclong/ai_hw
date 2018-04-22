import json
f = open('weried_data.txt','r')
pinyin = f.readline()
out_list = []
while pinyin:
    hanzi = f.readline()
    d = {}
    d['han'] = hanzi
    d['pinyin'] = pinyin.strip().split(' ')
    out_list.append(d)
    pinyin = f.readline()
    
f.close()
out_file = open('judge_provided.json','w')
json.dump(out_list, out_file)
out_file.close()

