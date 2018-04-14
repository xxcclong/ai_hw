import json
file12 = open('yierji.txt','r')
all_str = file12.read()
model3 = {}
for i in range(1,12):
    F = open('sina/2016-%02d'%(i),'r')
    i = i + 1
    news_list = json.load(F)
    for news in news_list:
        former1 = ''
        former2 = ''
        for hanzi in news:
            if hanzi not in all_str:
                former1 = ''
                former2 = ''
                continue
            if former1 == '':
                former1 = hanzi
                continue
            if former2 == '':
                former2 = hanzi
                continue
            if (former1 + former2 + hanzi) in model3:
                model3[former1 + former2 + hanzi] += 1
            else:
                model3[former1 + former2 + hanzi] = 1
            former1 = former2
            former2 = hanzi
    F.close()
write_file = open('three_model.json','w')
json.dump(model3, write_file)
write_file.close()
