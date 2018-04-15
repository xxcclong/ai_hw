import math
import json
def concu_pos(w1, w2, t = 0.5 ): # 两个汉字 
    global model1, model2, word_model, ALL
  #  if (w1 + w2) in word_model:
   #     return 1 - (0.5) ** word_model[w1+w2]
#    print(w1 + w2)
    if (w1 + w2) in model2:
        fenzi = model2[w1 + w2]
    else: fenzi = 0
    if w1 not in model1:
        fenmu = 9999999
    else:
        fenmu = model1[w1]
    moreover = 0
    if w2 in model1:
        moreover = model1[w2] / ALL
    if ((fenzi / fenmu)*t + moreover * (1- t) )== 0:
        return 1e-8
    return (fenzi / fenmu)*t + moreover*(1-t)




file2 = open('two_model.json','r')
file1 = open('one_model.json','r')
file3 = open('words_model.json', 'r')
file3yuan = open('three_model.json','r')
model3 = json.load(file3yuan)
word_model = json.load(file3)
model1 = json.load(file1)
model2 = json.load(file2)
file1.close()
file2.close()
file_dict = open('ping_han_dict.json','r')
ping_han_dict = json.load(file_dict)
file_dict.close()
ALL = 0
for k in model1:
    ALL = ALL + model1[k]
while True:
    input_str = input()
    input_list = input_str.split(' ')
    hanzibiao = []
    for item in input_list:
        hanzibiao.append(ping_han_dict[item])
    MAX = {}
    max_before = {}
    i = 0
    while i < len(hanzibiao):
        if i == 0:
            for item in hanzibiao[i]:
                if item in model1:
                    MAX[item + str(i)] = math.log(model1[item] / ALL)
                else:
                    MAX[item+str(i)] = -9999999
                max_before[item+str(i)] = ''
            i = i + 1
            continue
        for item1 in hanzibiao[i]:
            now_max = -9999999
            for item2 in hanzibiao[i-1]:
                if (MAX[item2+str(i-1)] + math.log(concu_pos(item2, item1)) > now_max):
                    now_max = MAX[item2+str(i-1)] + math.log(concu_pos(item2, item1))
                    max_before[item1+str(i)] = item2
            MAX[item1+str(i)] = now_max
        if i == len(hanzibiao) - 1:
            maxall = -9999999
            maxstr = ''
            for item in hanzibiao[i]:
                if MAX[item+str(i)] > maxall:
                    maxall = MAX[item+str(i)]
                    maxstr = item
            OutOut = []
            k = len(hanzibiao) - 1
            while k>=0:
                OutOut.append(maxstr)
                maxstr = max_before[maxstr+str(k)]
                k = k-1
            j = len(OutOut) - 1
            answer = []
            while j >= 0:
                answer.append(OutOut[j])
                j = j - 1
            print(answer)
        i = i + 1
                



