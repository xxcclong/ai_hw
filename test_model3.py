import json
f = open('three_model.json','r')
model = json.load(f)
if '我们去' in model:
    print(model['我们去'])
