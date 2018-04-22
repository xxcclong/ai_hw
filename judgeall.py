import time
import math
import json
def concu_pos(w2, w1,w0 = '', t = 0.6, y = 0.3): # 两个汉字 
    global model1, model2, word_model, ALL, model3, file_score,if_eryuan
    W0W1W2 = 0
    W0W1 = 0
    W1W2 = 0 
    W1 = 0
    W2 = 0
    distance = 0


#   eryuam 
    if if_eryuan == 1:
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

t = 0.0
y = 0.0
if_eryuan = 0
model1 = json.load(file1)
model2 = json.load(file2) 
model3 = json.load(file_th)
file1.close()
file2.close()
file_th.close()
file_dict = open('ping_han_dict.json','r')
ping_han_dict = json.load(file_dict)
file_dict.close()
ALL = 0
for k in model1:
    ALL += model1[k]
file_names = ['judge12.json', 'judge_provided.json']
rules = [[file_names[1], '0.5', '0.4','0'],
        [file_names[1],'0.6','0.3','0'],
        [file_names[1],'0.6','0.15','0'],
        [file_names[1],'0.7','0.2','0'], 
        [file_names[1],'0.7','0.1','0'], 
        [file_names[1],'0.8','0.1','0'],
        [file_names[1],'0.4','0.1','1'],
        [file_names[1],'0.6','0.1','1'],
        [file_names[1],'0.8','0.1','1']]

for rule in rules:
    filej = open(rule[0],'r')
    judge = json.load(filej)
    filej.close()
    t = float(rule[1])
    y = float(rule[2])
    if_eryuan = int(rule[3])
    time_before = time.clock()
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
            item = item.lower()
            if item not in ping_han_dict:
                if item == 'lve':
                    item = 'lue'
                elif item == 'nve':
                    item = 'nue'
                elif item ==' ':
                    continue
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

            i = i + 1
                    
    print(str(allwords) + ' ' + str(rightwords) + ' '+ str(rightwords / allwords)+'\n')
    print(str(allsten)+' '+str(rightsten)+' '+str(rightsten / allsten)+'\n')
    if if_eryuan == 1:
        print('2yuan\n')
    else:
        print('3yuan\n')
    print(time.clock() - time_before)
    print(time.asctime(time.localtime(time.time())))


