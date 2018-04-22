import json
model = json.load(open('model3_cut_300000.json','r'))
it = 0
for k in model:
    if it > 10:
        break
    it += 1
    print(k)
    print(model[k])

