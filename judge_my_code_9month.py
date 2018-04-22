import time
import math
import json
def concu_pos(w2, w1,w0 = '', t = 0.6, y = 0.3): # 两个汉字 
    global model1, model2, word_model, ALL, model3, file_score
  #  if (w1 + w2) in word_model:
   #     return 1 - (0.5) ** word_model[w1+w2]
#    print(w1 + w2)
    W0W1W2 = 0
    W0W1 = 0
    W1W2 = 0 
    W1 = 0
    W2 = 0
    distance = 0


#   eryuam 
    w0 = ''
#   eryuan

    if w0 != '':
        if (w0 + w1 + w2) in model3:
            W0W1W2 = model3[w0 + w1 + w2] 
        else: 
            W0W1W2 = 0
        if (w0+w1) in model2:
            W0W1 = model2[w0+w1]
        else:
            W0W1 = 9999999
        if (w1+w2) in model2:
            W1W2 = model2[w1+w2]
        else:
            W1W2 = 0
        if w1 in model1:
            W1 = model1[w1]
        else:
            W1 = 9999999
        if w2 in model1:
            W2 = model1[w2]
        else:
            W2 = 0
        distance = t * W0W1W2 / W0W1 + y * W1W2 / W1 + (1 - t - y)*W2 / ALL
    else:
        if (w1+w2) in model2:
            W1W2 = model2[w1+w2]
        else:
            W1W2 = 0
        if w1 in model1:
            W1 = model1[w1]
        else:
            W1 = 9999999
        if w2 in model1:
            W2 = model1[w2]
        else:
            W2 = 0
        distance = (t+y) * W1W2 / W1 + (1 - t - y) * W2 / ALL
    if distance == 0:
        return 1e-8
    else:
        return distance

file2 = open('part_two_model.json','r')
file1 = open('part_one_model.json','r')
file_th = open('part_three_model.json','r')

file3 = open('words_model.json', 'r')
filej = open('judge12.json','r')
t = 0.8
y = 0.1
now ='result_t='+str(t)+'y='+str(y)+ time.asctime( time.localtime(time.time()))+ 'news_judgement'+'_2yuan_partdata' 
file_score = open(now,'w')
word_model = json.load(file3)
model1 = json.load(file1)
model2 = json.load(file2) 
model3 = json.load(file_th)
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
        hanzibiao.append(ping_han_dict[item.lower()])
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
                distance = 0
                if i > 2:
                    distance = math.log(concu_pos(item1, item2, max_before[item2+str(i-1)],t,y)) + MAX[item2+str(i-1)]
                else:
                    distance = math.log(concu_pos(item1, item2,'',t,y)) + MAX[item2+str(i-1)]
                if (distance  > now_max):
                    now_max = distance
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
            #else:
                
              # file_score.write(ju['han'] + '\n')
               # file_score.write(out_str+'\n')

        i = i + 1
                
file_score.write(str(allwords) + ' ' + str(rightwords) + ' '+ str(rightwords / allwords)+'\n')
file_score.write(str(allsten)+' '+str(rightsten)+' '+str(rightsten / allsten)+'\n')
file_score.write(time.asctime( time.localtime(time.time()) ))
file_score.close()


