import time
import math
import json
def concu_pos(w1, w2, t = 0.5): # 两个汉字 
    global model1, model2, word_model, ALL, langmuda
  #  if (w1 + w2) in word_model:
   #     return 1 - (0.5) ** word_model[w1+w2]
#    print(w1 + w2)
   t = langmuda
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
filej = open('judge12.json','r')
langmuda = 0.5
now ='result'+ time.strftime('%c',time.localtime())
file_score = open(now,'w')
word_model = json.load(file3)
model1 = json.load(file1)
model2 = json.load(file2)
judge = json.load(filej)
file1.close()
file2.close()
file_dict = open('ping_han_dict.json','r')
ping_han_dict = json.load(file_dict)
file_dict.close()
ALL = 0
for k in model1:
    ALL = ALL + model1[k]


allwords = 0
rightwords = 0
allsten = 0
rightsten = 0


for ju in judge:
    input_list = ju['pinyin']
    allwords = allwords + len(input_list)
    allsten = allsten + 1
    hanzibiao = []
    nopy = 0
    for item in input_list:
        if item not in ping_han_dict:
            if item == 'lve':
                item = 'lue'
            else:
                print(item)
                print(ju)
                nopy = 1
                break
        hanzibiao.append(ping_han_dict[item])
    if nopy == 1:
        allwords =  allwords -  len(input_list)
        allsten = allsten - 1
        continue

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
            right_sig = 1
            out_str = ''
            for k in range(0,len(answer)):
                out_str = out_str + answer[k]
                if ju['han'][k] == answer[k]:
                    rightwords = rightwords + 1
                else:
                    right_sig = 0
            if right_sig == 1:
                rightsten = rightsten + 1
            else:
                file_score.write(ju['han'] + '\n')
                file_score.write(out_str+'\n')

        i = i + 1
                
file_score.write(str(allwords) + ' ' + str(rightwords) + ' '+ str(rightwords / allwords)+'\n')
file_score.write(str(allsten)+' '+str(rightsten)+' '+str(rightsten / allsten)+'\n')
file_score.write('lambda: '+ str(langmuda) + '\n')
file_score.close()


