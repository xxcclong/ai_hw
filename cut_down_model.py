import json
model3 = json.load(open('part_three_model.json','r'))
model2 = json.load(open('part_two_model.json','r'))
it = 0
model2 = sorted(model2.items(), key=lambda item:item[1],reverse=True)
model3 = sorted(model3.items(), key=lambda item:item[1],reverse=True)
model2_cut = {}
model3_cut = {}
it = 0
for k in model2:
    model2_cut[k[0]] = int(k[1])
    it += 1
    if it > 300000:
        break
it = 0
for k in model3:
    model3_cut[k[0]] = int(k[1])
    it += 1
    if it > 3000000:
        break
#f = open('model2_cut_300000.json','w')
f3 = open('model3_cut_3000000.json','w')
#json.dump(model2_cut, f)
json.dump(model3_cut, f3)
#f.close()
f3.close()
